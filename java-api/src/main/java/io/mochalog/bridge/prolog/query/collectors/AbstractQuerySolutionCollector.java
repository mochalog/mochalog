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

package io.mochalog.bridge.prolog.query.collectors;

import io.mochalog.bridge.prolog.lang.Module;
import io.mochalog.bridge.prolog.query.Query;

/**
 * Abstract implementation of a query solution
 * collector, including builder definition.
 */
public abstract class AbstractQuerySolutionCollector implements QuerySolutionCollector
{
    /**
     * Builder of subtypes of AbstractQuerySolutionCollector. Intended to be
     * extended and allow for builder heirarchy.
     * @param <T> Type of solution collector to build
     */
    public static abstract class Builder<T extends AbstractQuerySolutionCollector>
    {
        // Query to collect solutions from
        protected Query query;
        // Working module to operate query from
        protected Module workingModule;

        /**
         * Constructor.
         * @param query Query to collect solutions from
         */
        public Builder(Query query)
        {
            this.query = query;
        }

        /**
         * Set the module to operate the query from
         * @param module Working module
         * @return Current builder
         */
        public Builder setWorkingModule(Module module)
        {
            this.workingModule = module;
            return this;
        }

        /**
         * Build the QuerySolutionCollector of type T based
         * on the builder parameters specified
         * @return Constructed QuerySolutionCollector
         */
        public abstract T build();
    }
}
