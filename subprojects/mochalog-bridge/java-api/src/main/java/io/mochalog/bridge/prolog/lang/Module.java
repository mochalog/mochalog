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

package io.mochalog.bridge.prolog.lang;

import java.util.Objects;

/**
 * Representation of a SWI-Prolog module (predicate namespace)
 */
public class Module
{
    // Identifier for the module
    private final String name;

    /**
     * Constructor.
     * @param name Module name
     */
    public Module(String name)
    {
        this.name = name;
    }

    /**
     * Get the name associated with the given module
     * @return Module name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get string format of module
     * @return String format
     */
    @Override
    public String toString()
    {
        return name;
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
        if (o != null && o instanceof Module)
        {
            Module module = (Module) o;
            // Field comparisons
            return Objects.equals(name, module.name);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(name);
    }
}
