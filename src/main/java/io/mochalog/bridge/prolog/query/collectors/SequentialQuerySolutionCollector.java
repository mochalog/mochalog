/*
 * Copyright 2017 The Mochalog Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mochalog.bridge.prolog.query.collectors;

import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.QuerySolution;
import io.mochalog.bridge.prolog.query.exception.EndOfQueryException;
import io.mochalog.bridge.prolog.query.exception.NoSuchSolutionException;

import io.mochalog.bridge.prolog.namespace.Namespace;
import io.mochalog.bridge.prolog.namespace.ReadOnlyNamespace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Solution collector implementation which iterates through
 * the interpreter query and accumulates solutions consecutively
 * (as is the case when a query is executed from the SWI-Prolog CLI)
 */
public class SequentialQuerySolutionCollector extends AbstractQuerySolutionCollector
{
    /**
     * Facilitates the building of SequentialQuerySolutionCollector
     * instances
     */
    public static class Builder
        extends AbstractQuerySolutionCollector.Builder<SequentialQuerySolutionCollector>
    {
        /**
         * Constructor.
         * @param query Query to collect solutions from
         */
        public Builder(Query query)
        {
            super(query);
        }

        @Override
        public SequentialQuerySolutionCollector build()
        {
            // Change working module for query if necessary
            String text = Query.runnableInModule(query, workingModule);
            return new SequentialQuerySolutionCollector(text);
        }
    }

    // Flag indicating whether solution collector
    // remains attached to interpreter
    private boolean isAttached;
    // Internal JPL query (facilitates
    // low-level connection to SWI-Prolog native interface)
    private org.jpl7.Query interpreterQuery;

    // Solutions which have been retrieved from SWI-Prolog
    // interpreter
    private List<QuerySolution> solutionCache;
    // Flag to indicate whether all query solutions have
    // been retrieved
    private boolean allSolutionsFetched;

    /**
     * Private constructor.
     * @param text String format of query
     */
    private SequentialQuerySolutionCollector(String text)
    {
        // Set up solution cache
        solutionCache = new ArrayList<>();

        // Open a new JPL query
        interpreterQuery = new org.jpl7.Query(text);
        // Signal attachment to intepreter
        isAttached = true;
    }

    @Override
    public int solutionCount()
    {
        return fetchAllSolutions().length;
    }

    @Override
    public boolean hasSolutions()
    {
        return hasSolution(0);
    }

    @Override
    public boolean hasSolution(int index)
    {
        // Solution has already been fetched, must exist
        if (isSolutionCached(index))
        {
            return true;
        }
        // Solution not yet fetched, verify whether
        // interpreter has valid solution available
        // at this index
        try
        {
            fetchSolution(index);
            return true;
        }
        catch (NoSuchSolutionException e)
        {
            return false;
        }
    }

    @Override
    public boolean hasSolution(QuerySolution solution)
    {
        fetchAllSolutions();
        return solutionCache.contains(solution);
    }

    @Override
    public boolean hasAllSolutions(Collection<QuerySolution> solutions)
    {
        fetchAllSolutions();
        return solutionCache.containsAll(solutions);
    }

    @Override
    public QuerySolution fetchSolution(int index) throws NoSuchSolutionException
    {
        if (isSolutionCached(index))
        {
            // Solution has already been retrieved from
            // the interpreter
            return solutionCache.get(index);
        }
        else
        {
            // Sequentially retrieve and cache each solution up to and
            // including the requested index
            try
            {
                int precedingSolutionsToFetch = index - solutionCache.size();
                for (int i = 0; i < precedingSolutionsToFetch; ++i)
                {
                    fetchNextSolution();
                }

                return fetchNextSolution();
            }
            catch (EndOfQueryException e)
            {
                throw new NoSuchSolutionException(e.getMessage());
            }
        }
    }

    @Override
    public QuerySolution fetchFirstSolution() throws NoSuchSolutionException
    {
        return fetchSolution(0);
    }

    @Override
    public QuerySolution fetchLastSolution() throws NoSuchSolutionException
    {
        QuerySolution[] allSolutions = fetchAllSolutions();
        return allSolutions[allSolutions.length - 1];
    }

    @Override
    public QuerySolution[] fetchAllSolutions()
    {
        while (!allSolutionsFetched)
        {
            try {
                fetchNextSolution();    // just fetch it to put it in the cache
            } catch (EndOfQueryException e) {
                // ignore this exception as we do want to go to the end of solutions
            }
        }

        // Now collect the whole cache
        QuerySolution[] solutions = new QuerySolution[solutionCache.size()];
        solutionCache.toArray(solutions);
        return solutions;
    }

    /**
     * Fetch the next solution in the interpreter stream.
     * @throws EndOfQueryException No further solutions remain
     */
    private QuerySolution fetchNextSolution() throws EndOfQueryException
    {
        // Check if further solutions exist
        if (isAttached && !allSolutionsFetched && interpreterQuery.hasMoreSolutions())
        {
            // Retrieve the next query solution and update
            // namespace values
            Namespace namespace =
                    new ReadOnlyNamespace(interpreterQuery.nextSolution());
            QuerySolution solution = new QuerySolution(namespace);
            solutionCache.add(solution);

            return solution;
        }

        // No further solutions to fetch
        allSolutionsFetched = true;
        // Close the collector when all solutions
        // are fetched
        detach();

        throw new EndOfQueryException("No further SWI query solutions remain.");
    }

    /**
     * Check if the solution with the requested index
     * is in cache
     * @param index Solution index
     * @return True if solution is available, false otherwise.
     */
    private boolean isSolutionCached(int index)
    {
        return index < solutionCache.size();
    }

    @Override
    public boolean detach()
    {
        if (isAttached)
        {
            // Close the underlying query
            // Necessary as neglecting to close
            // JPL queries can resulting in Prolog
            // engine deadlocks
            interpreterQuery.close();
            isAttached = false;
        }

        return true;
    }
}
