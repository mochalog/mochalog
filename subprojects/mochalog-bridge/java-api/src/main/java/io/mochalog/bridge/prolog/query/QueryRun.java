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

package io.mochalog.bridge.prolog.query;

import io.mochalog.bridge.prolog.namespace.ScopedNamespace;
import org.jpl7.Term;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Individual session in which Prolog query is
 * executed on the SWI-Prolog interpreter
 * <p>
 * Provides interface with query access controls
 */
public class QueryRun
{
    /**
     * Response to a request for further query
     * solutions made to the SWI-Prolog interpreter. Can
     * either fail (indicating no more solutions) or succeed
     * and provide additional solutions.
     */
    private class SolutionResponse
    {
        // Index of solution which was requested
        private int index;
        // Requested solution (may not exist)
        private QuerySolution solution;

        /**
         * Constructor.
         * @param index Index of solution being requested
         */
        SolutionResponse(int index)
        {
            this.index = index;
            // Retrieve response
            fetchSolution();
        }

        /**
         * Retrieve response data from SWI-Prolog interpreter
         * <p>
         * Arrangement in which single library calls are enforced
         * each request cycle is due to fact that JPL changes state each time
         * hasMoreSolutions() is called
         */
        private void fetchSolution()
        {
            // Check if further solutions exist
            if (internalQuery.hasMoreSolutions())
            {
                // Retrieve the next query solution and update
                // namespace values
                Map<String, Term> bindings = internalQuery.nextSolution();
                ScopedNamespace namespace = new ScopedNamespace(bindings);
                solution = new QuerySolution(namespace);
            }
        }

        /**
         * Get the index for solution being requested
         * @return Solution index
         */
        public int getIndex()
        {
            return index;
        }

        /**
         * Check if solution request was successful
         * @return True if solution exists, false otherwise.
         */
        boolean hasSolution()
        {
            return solution != null;
        }

        /**
         * Get the solution which was fetched from SWI-Prolog,
         * if it exists.
         * @return Solution to query
         * @throws NoSuchElementException No further solutions exist
         */
        QuerySolution getSolution() throws NoSuchElementException
        {
            if (!hasSolution())
            {
                throw new NoSuchElementException("No further solutions exist for given query.");
            }

            return solution;
        }
    }

    // Prolog query data
    private Query query;
    // Internal JPL query (facilitates
    // low-level connection to SWI-Prolog native interface)
    private org.jpl7.Query internalQuery;

    // Index of current solution in broader query scope
    private int index;
    // Current solution and next solution retrieved are maintained
    // in order to ensure state is consistent
    private SolutionResponse currentSolutionResponse, nextSolutionResponse;

    /**
     * Constructor.
     * @param query Query to run
     */
    public QueryRun(Query query)
    {
        this.query = query;
        // Open a new JPL query
        internalQuery = new org.jpl7.Query(query.toString());

        // Start immediately with a solution available
        // Standard JPL query would require that nextSolution()
        // be called first in order to retrieve first solution
        currentSolutionResponse = new SolutionResponse(0);
        nextSolutionResponse = new SolutionResponse(1);
    }

    /**
     * Check if there is a solution available at
     * the current solution index
     * @return True if solution exists, false otherwise.
     */
    public synchronized boolean hasSolution()
    {
        return currentSolutionResponse.hasSolution();
    }

    /**
     * Get the solution available at the current
     * solution index
     * @return Current solution
     * @throws NoSuchElementException No solution exists at this index
     */
    public synchronized QuerySolution getSolution() throws NoSuchElementException
    {
        return currentSolutionResponse.getSolution();
    }

    /**
     * Check if further solutions to the query exist.
     * @return True if further solutions exist; false otherwise.
     */
    public synchronized boolean hasFurtherSolutions()
    {
        return nextSolutionResponse.hasSolution();
    }

    /**
     * Retrieve the next query solution (progesses query state onwards)
     * @return Next query solution
     * @throws NoSuchElementException If no further solutions exist
     */
    public QuerySolution progressToNextSolution() throws NoSuchElementException
    {
        // Request the next solution be retrieved given it exists
        // Moves query state onwards
        requestNextSolution();
        // Pass requested solution back to caller
        return getSolution();
    }

    /**
     * Update solution response queue to include
     * next solution
     */
    private void requestNextSolution()
    {
        // Increment currently viewed solution
        ++index;
        // Move next solution into view and ensure we have
        // requested following solution (ensure state remains consistent)
        currentSolutionResponse = nextSolutionResponse;
        nextSolutionResponse = new SolutionResponse(index + 1);
    }

    /**
     * Get index of solution currently in view
     * @return Solution index
     */
    public int getIndex()
    {
        return index;
    }
}
