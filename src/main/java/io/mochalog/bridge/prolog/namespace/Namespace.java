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
import java.util.Set;

/**
 * Namespace which maps named variables to Prolog
 * term values. Provides the capacity to read
 * variable values.
 */
public interface Namespace
{
    /**
     * Return mapping og name vars to their terms
     * @return List of names in namespace
     */
    Map<String, Term> getBinding();


    /**
     * Get the value associated with the given
     * variable name
     * @param name Variable name
     * @return Value associated
     * @throws NoSuchVariableException Variable with given
     * name does not exist
     */
    Term get(String name) throws NoSuchVariableException;

    /**
     * Check if variable with name is defined in
     * the namespace
     * @param name Variable name
     * @return True if defined, false otherwise.
     */
    boolean has(String name);

    /**
     * Return all names defined
     * @return List of names in namespace
     */
    Set<String> getNames();
}
