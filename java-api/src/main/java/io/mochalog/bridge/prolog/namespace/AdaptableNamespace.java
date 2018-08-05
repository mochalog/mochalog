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
 * Namespace which is capable of being dynamically
 * modified
 */
public interface AdaptableNamespace extends Namespace
{
    /**
     * Set the variable with the given name to
     * a specified value. Create the variable
     * if it does not already exist.
     * @param name Variable name
     * @param value Value to set
     */
    void set(String name, Term value);

    /**
     * Set a collection of variables to
     * respectively mapped values. Create new variables
     * if they do not already exist.
     * @param bindings Variable bindings to set
     */
    void set(Map<String, Term> bindings);
}
