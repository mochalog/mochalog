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

package io.mochalog.prolog.query;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over an ordered collection of solutions to a
 * Prolog query
 */
// Currently thinly wraps functionality which already exists in
// Query. Practically better than having Query serve as its own iterator
// from modularisation perspective
public class QuerySolutionIterator implements Iterator<QuerySolution>
{
    // Query to iterate over
    private Query query;

    /**
     * Constructor.
     * @param query Query to iterate over
     */
    public QuerySolutionIterator(Query query)
    {
        this.query = query;
    }

    /**
     * Check further solutions exist to given query
     * @return True if further solutions exist; false otherwise.
     */
    @Override
    public boolean hasNext()
    {
        return query.hasNext();
    }

    /**
     * Fetch next solution for given query if it exists
     * @return Next query solution
     * @throws NoSuchElementException Given no further solutions exist
     */
    @Override
    public QuerySolution next() throws NoSuchElementException
    {
        QuerySolution next = query.next();
        // Check next solution exists
        if (next == null)
        {
            throw new NoSuchElementException("No further solutions exist for given query.");
        }

        return next;
    }
}