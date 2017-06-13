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

import io.mochalog.bridge.prolog.lang.Variable;
import io.mochalog.bridge.prolog.namespace.ScopedNamespace;

import io.mochalog.util.format.AbstractFormatter;

/**
 * Format a Prolog query string with inline substitution
 * rules
 */
public class QueryFormatter extends AbstractFormatter<Query>
{
    // Namespace instance to apply to generated queries
    private ScopedNamespace queryNamespace = null;

    @Override
    public Query format(String str, Object... args)
    {
        queryNamespace = new ScopedNamespace();
        // Parse query input
        String formattedQuery = formatInput(str, args);

        // Create query from input and ensure formatter
        // namespace instance cleared for further format runs
        Query query = new Query(formattedQuery, queryNamespace);
        queryNamespace = null;

        return query;
    }

    @Override
    protected String formatRule(String rule, Object arg)
    {
        // Format invidual substitution rules
        // TODO: Currently only supports variables
        switch (rule)
        {
            case "V":
                return formatVariable(arg);
            default:
                return null;
        }
    }

    /**
     * Format a Prolog variable into an appropriate
     * string representation and add query namespace binding
     * @param o Object argument
     * @return Formatted variable string
     */
    private String formatVariable(Object o)
    {
        if (o instanceof Variable)
        {
            Variable variable = (Variable) o;

            if (queryNamespace == null)
            {
                queryNamespace = new ScopedNamespace();
            }

            queryNamespace.define(variable);
            return variable.getName();
        }

        return null;
    }
}
