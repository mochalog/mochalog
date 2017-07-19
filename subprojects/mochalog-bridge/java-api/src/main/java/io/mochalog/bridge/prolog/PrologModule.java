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

package io.mochalog.bridge.prolog;

import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.format.QueryFormatter;
import io.mochalog.util.format.Formatter;

/**
 * Sandboxed database of SWI-Prolog predicates and clauses which
 * can be manipulated and queried
 */
public class PrologModule
{
    // Module name
    private String name;

    /**
     * Constructor
     * @param name Module name
     */
    public PrologModule(String name)
    {
        this.name = name;
    }

    /**
     * Formulate a Prolog query to be declared to the
     * SWI-Prolog interpreter in the given module namespace
     * @param query Formatted query string
     * @param args Query arguments
     * @return Query object
     */
    public Query query(String query, Object... args)
    {
        Formatter<Query> formatter = new QueryFormatter();
        return formatter.format(query, args);
    }
}
