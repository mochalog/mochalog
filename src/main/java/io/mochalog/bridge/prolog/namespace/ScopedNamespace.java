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
import java.util.Objects;
import java.util.Set;

/**
 * Namespace binding names to Prolog
 * variables within a given scope
 */
public class ScopedNamespace implements AdaptableNamespace
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

    @Override
    public Term get(String name) throws NoSuchVariableException
    {
        Variable variable = definitions.get(name);
        if (variable == null)
        {
            throw new NoSuchVariableException("Variable " + name + " undefined.");
        }

        return variable.value();
    }

    @Override
    public Map<String, Term> getBinding() {

        Map<String,Term> binding = new HashMap<String, Term>();
        for (Map.Entry<String, Variable> e : this.definitions.entrySet()) {
            binding.put(e.getKey(), e.getValue().value());
        }
        return binding;
    }


    @Override
    public Set<String> getNames() {
        return definitions.keySet();
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
        Variable variable = definitions.get(name);
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
    @Override
    public void set(Map<String, Term> bindings)
    {
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
        define(variable);
        return variable;
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
        String name = variable.getName();
        if (!has(name))
        {
            definitions.put(name, variable);
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
    @Override
    public boolean has(String name)
    {
        return definitions.containsKey(name);
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
        if (o != null && o instanceof ScopedNamespace)
        {
            ScopedNamespace namespace = (ScopedNamespace) o;
            // Field comparisons
            return Objects.equals(definitions, namespace.definitions);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(definitions);
    }
}
