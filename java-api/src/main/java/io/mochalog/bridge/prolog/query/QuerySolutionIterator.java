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
import io.mochalog.bridge.prolog.query.collectors.QuerySolutionCollector;
import io.mochalog.bridge.prolog.query.collectors.SequentialQuerySolutionCollector;
import io.mochalog.bridge.prolog.query.exception.NoSuchSolutionException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over an ordered collection of solutions to a
 * Prolog query
 */
public class QuerySolutionIterator implements Iterator<QuerySolution>
{
    // Index of currently viewed solution
    private int index;
    // Interface for the accumulation of query solutions
    private QuerySolutionCollector collector;

    /**
     * Constructor.
     * @param query Query to iterate over
     */
    public QuerySolutionIterator(Query query)
    {
        this(
            new SequentialQuerySolutionCollector.Builder(query)
                .build()
        );
    }

    /**
     * Constructor.
     * @param query Query to iterate over
     * @param workingModule Module to operate query from
     */
    public QuerySolutionIterator(Query query, Module workingModule)
    {
        this(
            new SequentialQuerySolutionCollector.Builder(query)
                .setWorkingModule(workingModule)
                .build()
        );
    }

    /**
     * Constructor.
     * @param collector Existing solution collector
     */
    public QuerySolutionIterator(QuerySolutionCollector collector)
    {
        this.collector = collector;
        index = 0;
    }

    /**
     * Check further solutions exist to given query
     * @return True if further solutions exist; false otherwise.
     */
    @Override
    public boolean hasNext()
    {
        return collector.hasSolution(index);
    }

    /**
     * Fetch next solution for given query if it exists
     * @return Next query solution
     * @throws NoSuchElementException Given no further solutions exist
     */
    @Override
    public QuerySolution next() throws NoSuchElementException
    {
        try
        {
            QuerySolution solution = collector.fetchSolution(index);
            ++index;
            return solution;
        }
        catch (NoSuchSolutionException e)
        {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    /**
     * Used for Java 7 compatibility
     * @throws UnsupportedOperationException Solutions should not be
     * able to be 'removed' from a query
     */
    @Override
    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Solutions cannot be removed from a query.");
    }
}