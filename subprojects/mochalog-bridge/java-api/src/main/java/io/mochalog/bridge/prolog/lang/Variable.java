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

import org.jpl7.Term;

/**
 * Representation of a variable type
 * in SWI-Prolog
 * <p>
 * Currently similar to org.jpl7.Variable,
 * however allows value setting and getting
 * (crucial for automatic binding of terms)
 */
public class Variable
{
    // Name assigned to the variable
    private final String name;
    // Term value assigned to the variable
    private Term value;

    /**
     * Constructor.
     * @param name Name to assign
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Getter.
     * @return Variable name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the variable value
     * @return Variable value
     */
    public Term get()
    {
        return value;
    }

    /**
     * Set the variable value
     * @param value Value to set
     */
    public void set(Term value)
    {
        this.value = value;
    }

    /**
     * Convert to string format of variable value
     * @return Value string
     */
    @Override
    public String toString()
    {
        return value.toString();
    }
}
