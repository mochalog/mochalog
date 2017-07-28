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

package io.mochalog.bridge.prolog.query;

import io.mochalog.bridge.prolog.lang.Module;
import io.mochalog.bridge.prolog.namespace.ScopedNamespace;

import java.util.List;
import java.util.ArrayList;

import java.util.NoSuchElementException;

/**
 * Individual session in which Prolog query is
 * executed on the SWI-Prolog interpreter
 * <p>
 * Provides interface with query access controls
 */
public class QueryRun
{
    // Index of current solution in stream
    private int index;

    // Internal JPL query (facilitates
    // low-level connection to SWI-Prolog native interface)
    private org.jpl7.Query interpreterQuery;

    // Solutions which have been retrieved from SWI-Prolog
    // interpreter
    private List<QuerySolution> solutionStream;
    // Flag to indicate whether all query solutions have
    // been retrieved
    private boolean allSolutionsFetched;

    /**
     * Constructor.
     * @param query Query to run
     */
    public QueryRun(Query query)
    {
        this(query.toString());
    }

    /**
     * Constructor.
     * @param query Query to run
     * @param workingModule Module to run query from
     */
    public QueryRun(Query query, Module workingModule)
    {
        this(String.format("%s:(%s)", workingModule.getName(), query.toString()));
    }

    /**
     * Constructor.
     * @param text String format of query
     */
    private QueryRun(String text)
    {
        // Start stream at beginning
        index = 0;
        // Set up solution cache
        solutionStream = new ArrayList<>();

        // Open a new JPL query
        interpreterQuery = new org.jpl7.Query(text);
    }

    /**
     * Check if there is a solution available at
     * the current solution index
     * @return True if solution exists, false otherwise.
     */
    public boolean hasSolution()
    {
        return hasSolution(index);
    }

    /**
     * Check if further solutions to the query exist.
     * @return True if further solutions exist; false otherwise.
     */
    public boolean hasFurtherSolutions()
    {
        return hasSolution(index + 1);
    }

    /**
     * Check if there is a solution available at
     * the given solution index
     * @return True if solution exists, false otherwise.
     */
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
        else if (!allSolutionsFetched)
        {
            return index == this.index + 1 ?
                fetchNextSolution() :
                fetchSolution(index);
        }

        return false;
    }

    /**
     * Get the solution available at the current
     * solution index
     * @return Current solution
     * @throws NoSuchElementException No solution exists at this index
     */
    public QuerySolution getSolution() throws NoSuchElementException
    {
        return getSolution(index);
    }

    /**
     * Get the solution available at the given solution
     * index
     * @param index Solution index
     * @return Solution at given index
     * @throws NoSuchElementException No solution exists at this index
     */
    public QuerySolution getSolution(int index) throws NoSuchElementException
    {
        // Ensure a solution exists at this index
        if (!hasSolution(index))
        {
            throw new NoSuchElementException("Solution at index " + index + " does not exist.");
        }

        return solutionStream.get(index);
    }

    /**
     * Retrieve the next query solution (progresses stream onwards)
     * @return Next query solution
     * @throws NoSuchElementException If no further solutions exist
     */
    public QuerySolution nextSolution() throws NoSuchElementException
    {
        // Progress stream onwards
        ++index;
        return getSolution();
    }

    /**
     * Progress the query stream onwards to next solution
     * @return True if next solution exists, false otherwise.
     */
    public boolean next()
    {
        // Progress stream onwards
        ++index;
        return hasSolution();
    }

    /**
     * Retrieve all solutions from the interpreter
     * and add to stream
     */
    private void fetchAllSolutions()
    {
        // Iteratively fetch each additional solution
        // until none remain
        while (fetchNextSolution());
    }

    /**
     * Retrieve the solution at the given index from
     * interpreter.
     * <p>
     * Due to linearity of interpreter stream, each
     * solution prior to the requested solution will also be
     * fetched
     * @param index Solution index
     * @return True if solution exists, false otherwise.
     */
    private boolean fetchSolution(int index)
    {
        if (!isSolutionCached(index))
        {
            // Retrieve and cache each solution up to and
            // including the requested index
            int solutionsToFetch = index + 1 - solutionStream.size();
            for (int i = 0; i <= solutionsToFetch; ++i)
            {
                // Terminate the fetch operation if we
                // reach end of stream
                if (!fetchNextSolution())
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fetch the next solution in the stream
     * from the interpreter
     * @return True if solution exists, false otherwise.
     */
    private boolean fetchNextSolution()
    {
        // Check if further solutions exist
        if (interpreterQuery.hasMoreSolutions())
        {
            // Retrieve the next query solution and update
            // namespace values
            ScopedNamespace namespace =
                    new ScopedNamespace(interpreterQuery.nextSolution());
            solutionStream.add(new QuerySolution(namespace));

            return true;
        }

        // No further solutions to fetch
        allSolutionsFetched = true;
        return false;
    }

    /**
     * Check if the solution with the requested index
     * is in cache
     * @param index Solution index
     * @return True if solution is available, false otherwise.
     */
    private boolean isSolutionCached(int index)
    {
        return index < solutionStream.size();
    }

    /**
     * Get index of solution currently in view
     * @return Solution index
     */
    public int getIndex()
    {
        return index;
    }
}
