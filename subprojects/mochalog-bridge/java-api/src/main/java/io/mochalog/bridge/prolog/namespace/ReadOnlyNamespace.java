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

package io.mochalog.bridge.prolog.namespace;

import org.jpl7.Term;

import java.util.Map;

/**
 *
 */
public class ReadOnlyNamespace implements Namespace
{
    private final Map<String, Term> bindings;

    public ReadOnlyNamespace(Map<String, Term> bindings)
    {
        this.bindings = bindings;
    }

    @Override
    public Term get(String name) throws NoSuchVariableException
    {
        Term value = bindings.get(name);

        if (value == null)
        {
            throw new NoSuchVariableException("Variable " + name + " undefined.");
        }

        return value;
    }

    @Override
    public boolean has(String name)
    {
        return bindings.containsKey(name);
    }
}
