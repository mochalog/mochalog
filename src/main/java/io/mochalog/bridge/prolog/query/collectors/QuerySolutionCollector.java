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

import io.mochalog.bridge.prolog.query.QuerySolution;
import io.mochalog.bridge.prolog.query.exception.NoSuchSolutionException;

import java.util.Collection;

/**
 * Collect the solutions to a given query at the
 * time of initialisation.
 * <p>
 * Provides a <i>best effort</i> view of the
 * solutions to a query, as collector may be forcibly
 * killed before all solutions could be retrieved.
 */
public interface QuerySolutionCollector
{
    /**
     * Number of solutions to the given query
     * @return Number of solutions
     */
    int solutionCount();

    /**
     * Check if any solutions to the given query exist
     * @return True if solutions exist, false otherwise.
     */
    boolean hasSolutions();

    /**
     * Check if a solution exists at the given index
     * @param index Solution index
     * @return True if solution exists, false otherwise.
     */
    boolean hasSolution(int index);

    /**
     * Check if the given solution is a solution
     * to the currently open query
     * @param solution Solution to check for
     * @return True if the query has the specified solution,
     * false otherwise.
     */
    boolean hasSolution(QuerySolution solution);

    /**
     * Check if all solutions in the given collection
     * are also solutions to the currently open query
     * (subset)
     * @param solutions Collection of query solutions
     * @return True if collection is a subset, false
     * otherwise.
     */
    boolean hasAllSolutions(Collection<QuerySolution> solutions);

    /**
     * Fetch the solution at the specified index from
     * the interpreter.
     * @param index Solution index
     * @return Query solution
     * @throws NoSuchSolutionException No further solutions exist
     * in the query
     */
    QuerySolution fetchSolution(int index) throws NoSuchSolutionException;

    /**
     * Fetch the first solution to the query.
     * @return First solution
     * @throws NoSuchSolutionException Query has no solutions
     */
    QuerySolution fetchFirstSolution() throws NoSuchSolutionException;

    /**
     * Fetch the last solution to the query.
     * @return Last solution
     * @throws NoSuchSolutionException Query has no solutions
     */
    QuerySolution fetchLastSolution() throws NoSuchSolutionException;

    /**
     * Fetch every solution available from the interpreter
     * @return Array of query solutions
     */
    QuerySolution[] fetchAllSolutions();

    /**
     * Detach the collector from the interpreter
     * and close any unlerlying query resources.
     * @return True if detach operation was successful,
     * false otherwise.
     */
    boolean detach();
}
