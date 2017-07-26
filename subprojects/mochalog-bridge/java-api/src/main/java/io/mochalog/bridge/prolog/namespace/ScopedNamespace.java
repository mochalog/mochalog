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

import io.mochalog.bridge.prolog.lang.Variable;

import org.jpl7.Term;

import java.util.Map;
import java.util.HashMap;

/**
 * Namespace binding names to SWI-Prolog
 * variables within a given scope
 */
public class ScopedNamespace
{
    // Bindings between variable names and variables
    private Map<String, Variable> definitions;

    /**
     * Constructor.
     */
    public ScopedNamespace()
    {
        definitions = new HashMap<>();
    }

    /**
     * Constructor.
     * @param bindings Variable bindings to initialise with
     */
    public ScopedNamespace(Map<String, Term> bindings)
    {
        this();
        set(bindings);
    }

    /**
     * Fetch the Prolog variable bound to the given
     * name
     * @param name Variable name
     * @return Bound variable or null if not defined
     */
    public Variable get(String name)
    {
        // Check variable exists
        if (name != null)
        {
            return definitions.get(name);
        }

        return null;
    }

    /**
     * Set the Prolog variable bound to the given
     * name to a specified value. Define a new variable
     * and set its value if name not defined.
     * @param name Variable name
     * @param value Value to set
     */
    public void set(String name, Term value)
    {
        Variable variable = get(name);
        // Check variable exists
        if (variable == null)
        {
            // Define if variable does not exist
            variable = define(name);
        }

        variable.set(value);
    }

    /**
     * Set the values of multiple variables.
     * @param bindings Binding between names and values
     */
    public void set(Map<String, Term> bindings)
    {
        if (bindings == null)
        {
            return;
        }

        for (Map.Entry<String, Term> binding : bindings.entrySet())
        {
            // Set the value of the variable corresponding
            // to each name in turn
            String name = binding.getKey();
            Term value = binding.getValue();

            set(name, value);
        }
    }

    /**
     * Define new Prolog variable with given name
     * @param name Variable name
     * @return Defined variable
     */
    public Variable define(String name)
    {
        Variable variable = new Variable(name);
        if (define(variable))
        {
            return variable;
        }

        return null;
    }

    /**
     * Define new Prolog variable
     * @param variable Variable to define
     * @return Check whether variable with given name
     * is already defined
     */
    public boolean define(Variable variable)
    {
        // Ensure name has not already been defined
        // within the namespace
        if (variable != null && !has(variable.getName()))
        {
            definitions.put(variable.getName(), variable);
            return true;
        }

        return false;
    }

    /**
     * Check if Prolog variable with given name
     * exists
     * @param name Variable name
     * @return Status of variable existence
     */
    public boolean has(String name)
    {
        return name != null && definitions.containsKey(name);
    }
}
