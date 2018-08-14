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

import io.mochalog.bridge.prolog.namespace.NoSuchVariableException;
import io.mochalog.bridge.prolog.namespace.Namespace;

import org.jpl7.Term;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Solution to individual goal successfully proved
 * by the SWI-Prolog engine (with variable unification)
 */
public class QuerySolution
{
    // Solution-level namespace containing
    // variable unifications
    private final Namespace namespace;

    /**
     * Constructor.
     * @param namespace Unification namespace
     */
    public QuerySolution(Namespace namespace)
    {
        this.namespace = namespace;
    }

    /**
     * Get a value to which a variable corresponding to the
     * given name unified
     * @param name Variable name
     * @return Unified value
     * @throws NoSuchVariableException Variable with given name
     * is undefined
     */
    public Term get(String name) throws NoSuchVariableException
    {
        return namespace.get(name);
    }

    /**
     * Get a value to which a variable corresponding to the
     * given name unified
     * @param name Variable name
     * @return Unified value
     * @throws NoSuchVariableException Variable with given name
     * is undefined
     */
    public Set<String> getVars()
    {
        return namespace.getNames();
    }


    /**
     * Output the mapping of variables to their bindings, for a subset of variables
     * @param outVars List of variables to return
     * @return Map of unified values
     * @throws NoSuchVariableException Variable with given name
     * is undefined
     */
    public Map<String, Term> getMap(String[] outVars) throws NoSuchVariableException
    {
        HashMap<String, Term> bindings = new HashMap();
        for (String outVar : outVars) {
            bindings.put(outVar, namespace.get(outVar));
        }
        return bindings;
    }

    /**
     * Output the mapping of variables to their bindings, for all variables
     * @return Map of unified values
     */
    public Map<String, Term> getMap() throws NoSuchVariableException
    {
        return namespace.getBinding();
    }

    @Override
    public final boolean equals(Object o)
    {
        // Early termination for self-identity
        if (this == o)
        {
            return true;
        }

        // null/type validation
        if (o != null && o instanceof QuerySolution)
        {
            QuerySolution solution = (QuerySolution) o;
            // Field comparisons
            return Objects.equals(namespace, solution.namespace);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(namespace);
    }
}
