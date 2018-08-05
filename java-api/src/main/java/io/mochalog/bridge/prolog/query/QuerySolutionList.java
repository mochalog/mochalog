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
import io.mochalog.util.collections.UnmodifiableList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * List representation of a sequence of solutions.
 * Allows random-access and facilitates query-on-demand.
 * List itself is unmodifiable (QuerySolutionList is a <i>view</i>
 * of query solutions resulting from a query execution).
 */
public class QuerySolutionList extends UnmodifiableList<QuerySolution>
{
    // Interface for the accumulation of query solutions
    private QuerySolutionCollector collector;

    /**
     * Constructor.
     * @param query Query to transform into solution list
     */
    public QuerySolutionList(Query query)
    {
        this(
            new SequentialQuerySolutionCollector.Builder(query)
                    .build()
        );
    }

    /**
     * Constructor.
     * @param query Query to transform into solution list
     * @param workingModule Module to operate query from
     */
    public QuerySolutionList(Query query, Module workingModule)
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
    public QuerySolutionList(QuerySolutionCollector collector)
    {
        this.collector = collector;
    }

    @Override
    public int size()
    {
        return collector.solutionCount();
    }

    @Override
    public boolean isEmpty()
    {
        return !collector.hasSolutions();
    }

    @Override
    public boolean contains(Object o)
    {
        return o instanceof QuerySolution && collector.hasSolution((QuerySolution) o);
    }

    @Override
    public Iterator<QuerySolution> iterator()
    {
        return new QuerySolutionIterator(collector);
    }

    @Override
    public Object[] toArray()
    {
        return collector.fetchAllSolutions();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
       // TODO: Not currently implemented
       return null;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        // TODO: Not currently implemented
        return false;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public QuerySolution get(int index)
    {
        try
        {
            return collector.fetchSolution(index);
        }
        catch (NoSuchSolutionException e)
        {
            throw new IndexOutOfBoundsException("No solutions available at index " + index);
        }
    }

    /**
     * Convenience method. Allows retrieval of the first
     * solution in the solution listing.
     * @return First solution
     * @throws NoSuchSolutionException No solutions exist
     * in the list
     */
    public QuerySolution getFirst() throws NoSuchSolutionException
    {
        return collector.fetchFirstSolution();
    }

    /**
     * Convenience method. Allows retrieval of the last
     * solution in the solution listing.
     * @return Last solution
     * @throws NoSuchSolutionException No solutions exist
     * in the list
     */
    public QuerySolution getLast() throws NoSuchSolutionException
    {
        return collector.fetchLastSolution();
    }

    @Override
    public int indexOf(Object o)
    {
        // TODO: Not currently implemented
        return 0;
    }

    @Override
    public int lastIndexOf(Object o)
    {
        // TODO: Not currently implemented
        return 0;
    }

    @Override
    public ListIterator<QuerySolution> listIterator()
    {
        // TODO: Not currently implemented
        return null;
    }

    @Override
    public ListIterator<QuerySolution> listIterator(int index)
    {
        // TODO: Not currently implemented
        return null;
    }

    @Override
    public List<QuerySolution> subList(int fromIndex, int toIndex)
    {
        // TODO: Not currently implemented
        return null;
    }
}