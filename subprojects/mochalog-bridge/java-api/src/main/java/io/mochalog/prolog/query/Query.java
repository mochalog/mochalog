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

import io.mochalog.prolog.namespace.ScopedNamespace;

import org.jpl7.Term;

import java.util.Map;

/**
 * Represents query provided to the SWI-Prolog
 * interpreter. Allows for management of query context,
 * including local namespace and solution stepping.
 */
public class Query
{
    // String form of Prolog query
    private String queryString;
    // Internal JPL query (facilitates
    // actual connection to Prolog interpreter)
    private org.jpl7.Query jplQuery;

    // Local variable namespace
    private ScopedNamespace namespace;

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
    }

    /**
     * Fetch the next solution in the
     * current query context
     * @return True if query goals could be proved; false
     * otherwise.
     */
    public boolean next()
    {
        // Check further solutions exist
        if (jplQuery.hasMoreSolutions())
        {
            // Retrieve the next query solution and update
            // namespace values
            Map<String, Term> bindings = jplQuery.nextSolution();
            namespace.set(bindings);
            return true;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return queryString;
    }
}
