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

import java.util.Iterator;

/**
 * Represents query provided to the SWI-Prolog
 * interpreter, managing localised query namespace.
 */
public class Query implements Iterable<QuerySolution>
{
    // String form of Prolog query
    private String text;
    // Local variable namespace
    private ScopedNamespace namespace;

    /**
     * Constructor.
     * @param text Query string
     * @param namespace Variable bindings (local to query)
     */
    public Query(String text, ScopedNamespace namespace)
    {
        this.text = text;
        this.namespace = namespace;
    }

    /**
     * Convert the query to string format
     * @return String format
     */
    @Override
    public String toString()
    {
        return text;
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
}
