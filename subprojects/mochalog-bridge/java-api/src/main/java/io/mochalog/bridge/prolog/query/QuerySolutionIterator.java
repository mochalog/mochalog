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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over an ordered collection of solutions to a
 * Prolog query
 */
public class QuerySolutionIterator implements Iterator<QuerySolution>
{
    // Query session being manipulated
    private QueryRun queryRun;

    /**
     * Constructor.
     * @param query Query to iterate over
     */
    public QuerySolutionIterator(Query query)
    {
        queryRun = new QueryRun(query);
    }

    /**
     * Check further solutions exist to given query
     * @return True if further solutions exist; false otherwise.
     */
    @Override
    public boolean hasNext()
    {
        return queryRun.hasSolution();
    }

    /**
     * Fetch next solution for given query if it exists
     * @return Next query solution
     * @throws NoSuchElementException Given no further solutions exist
     */
    @Override
    public QuerySolution next() throws NoSuchElementException
    {
        QuerySolution solution = queryRun.getSolution();

        try
        {
            queryRun.progressToNextSolution();
        }
        catch (NoSuchElementException e) {}
        
        return solution;
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