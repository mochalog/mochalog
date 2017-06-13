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

/**
 * Solution to individual goal successfully proved
 * by the SWI-Prolog engine
 * <p>
 * Provides manual access to variable bindings
 * (as opposed to automatic rebinding)
 */
public class QuerySolution
{
    // Snapshot of the parent query namespace
    // after goal successfully proved
    private final ScopedNamespace namespaceSnapshot;

    /**
     * Constructor.
     * @param namespaceSnapshot Snapshot of query namespace
     */
    public QuerySolution(ScopedNamespace namespaceSnapshot)
    {
        this.namespaceSnapshot = namespaceSnapshot;
    }

    /**
     * Get a variable binding corresponding to the
     * given name
     * @param name Variable name
     * @return Associated variable binding
     */
    public Variable get(String name)
    {
        return namespaceSnapshot.get(name);
    }
}
