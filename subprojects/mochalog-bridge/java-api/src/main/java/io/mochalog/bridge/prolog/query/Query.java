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

import io.mochalog.bridge.prolog.namespace.ScopedNamespace;

import org.jpl7.Term;

import java.util.Iterator;
import java.util.Map;

/**
 * Represents query provided to the SWI-Prolog
 * interpreter. Allows for management of query context,
 * including local namespace and solution stepping.
 */
public class Query implements Iterable<QuerySolution>
{
    // String form of Prolog query
    private String queryString;
    // Internal JPL query (facilitates
    // actual connection to Prolog interpreter)
    private org.jpl7.Query jplQuery;

    // Local variable namespace
    private ScopedNamespace namespace;

    // Current query state
    // Used for JPL workaround - Query.hasMoreSolutions()
    // advances state each invocation (undesirable)
    // State maintained locally to allow for arbitrary checking
    // for further solution existence
    private enum State
    {
        // State unchecked with SWI-Prolog interpreter
        // Not yet known whether further solutions exist
        NOT_VALIDATED,
        // Prolog failed to satisfy query
        FAILED,
        // Prolog succeeded in satisfying query goals (solution exists)
        SUCCEEDED
    }
    private State state;

    /**
     * Constructor.
     * @param query Query string
     * @param namespace Variable bindings (local to query)
     */
    public Query(String query, ScopedNamespace namespace)
    {
        queryString = query;
        jplQuery = new org.jpl7.Query(queryString);

        this.namespace = namespace;

        setState(State.NOT_VALIDATED);
    }

    /**
     * Set current local query state
     * @param state New state
     */
    private void setState(State state)
    {
        this.state = state;
    }

    /**
     * Check if further solutions to the query exist.
     * @return True if further solutions exist; false otherwise.
     */
    public boolean hasNext()
    {
        // Need to recheck for further solutions
        if (state == State.NOT_VALIDATED)
        {
            setState(jplQuery.hasMoreSolutions() ? State.SUCCEEDED : State.FAILED);
        }

        return state == State.SUCCEEDED;
    }

    /**
     * Fetch the next solution in the
     * current query context
     * @return Next solution if exists; otherwise null.
     */
    public QuerySolution next()
    {
        // Check further solutions exist
        if (!hasNext())
        {
            return null;
        }

        // Retrieve the next query solution and update
        // namespace values
        Map<String, Term> bindings = jplQuery.nextSolution();
        namespace.set(bindings);
        QuerySolution solution = new QuerySolution(namespace);

        // Successive queries should check for satisfaction
        setState(State.NOT_VALIDATED);

        return solution;
    }

    /**
     * Iterate over available solutions for the query
     * @return Iterator over query solutions
     */
    @Override
    public Iterator<QuerySolution> iterator()
    {
        return new QuerySolutionIterator(this);
    }

    /**
     * Convert the query to string format
     * @return String format
     */
    @Override
    public String toString()
    {
        return queryString;
    }
}
