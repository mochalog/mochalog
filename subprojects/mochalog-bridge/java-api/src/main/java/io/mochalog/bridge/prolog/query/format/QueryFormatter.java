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

package io.mochalog.bridge.prolog.query.format;

import io.mochalog.bridge.prolog.lang.Variable;
import io.mochalog.bridge.prolog.namespace.ScopedNamespace;

import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.util.format.AbstractFormatter;

/**
 * Format a Prolog query string with inline substitution
 * rules
 */
public class QueryFormatter extends AbstractFormatter<Query>
{
    // Namespace instance to apply to generated queries
    private ScopedNamespace currentNamespace;

    public QueryFormatter()
    {
        currentNamespace = new ScopedNamespace();

        formatSpec.setRule("V", this::formatVariable);
    }

    @Override
    public Query format(String str, Object... args)
    {
        // Parse query input
        String formattedQueryString = formatString(str, args);

        // Create query from input and ensure formatter
        // namespace instance cleared for further format runs
        Query query = new Query(formattedQueryString, currentNamespace);
        currentNamespace = new ScopedNamespace();

        return query;
    }

    /**
     * Format a Prolog variable into an appropriate
     * string representation and add query namespace binding
     * @param arg Object arg
     * @return Formatted variable string
     */
    private String formatVariable(Object arg) throws ClassCastException
    {
        if (arg instanceof Variable)
        {
            Variable variable = (Variable) arg;
            currentNamespace.define(variable);
            return variable.getName();
        }

        throw new ClassCastException("Failed to convert provided object argument " +
            "to variable term.");
    }

    protected ScopedNamespace getNamespace()
    {
        return currentNamespace;
    }
}
