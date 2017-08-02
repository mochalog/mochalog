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

import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.util.format.AbstractFormatter;

/**
 * Format a Prolog query string with inline substitution
 * rules
 */
public class QueryFormatter extends AbstractFormatter<Query>
{
    public QueryFormatter()
    {
        super();

        // Prolog strings are wrapped in quote characters
        formatSpec.setRule("S", o -> "\"" + String.valueOf(o) + "\"");
    }

    @Override
    public Query format(String str, Object... args)
    {
        // Parse query input
        String formattedQueryString = formatString(str, args);

        // Create query from input and ensure formatter
        // namespace instance cleared for further format runs
        return new Query(formattedQueryString);
    }
}
