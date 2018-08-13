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
import java.util.Objects;
import java.util.Set;

/**
 * Namespace which is only capable of being read from.
 * Variable bindings are defined on construction and not
 * capable of being modified subequently.
 */
public class ReadOnlyNamespace implements Namespace
{
    // Bindings of variable names to term values
    private final Map<String, Term> bindings;

    /**
     * Constructor.
     * @param bindings Variable bindings
     */
    public ReadOnlyNamespace(Map<String, Term> bindings)
    {
        this.bindings = bindings;
    }


    @Override
    public Map<String, Term> getBinding() {
        return this.bindings;
    }

    @Override
    public Term get(String name) throws NoSuchVariableException
    {
        Term value = bindings.get(name);

        // Check variable is defined in the namespace
        if (value == null)
        {
            throw new NoSuchVariableException(name);
        }

        return value;
    }

    @Override
    public Set<String> getNames() {
        return bindings.keySet();
    }

    @Override
    public boolean has(String name)
    {
        return bindings.containsKey(name);
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
        if (o != null && o instanceof ReadOnlyNamespace)
        {
            ReadOnlyNamespace namespace = (ReadOnlyNamespace) o;
            // Field comparisons
            return Objects.equals(bindings, namespace.bindings);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(bindings);
    }
}
