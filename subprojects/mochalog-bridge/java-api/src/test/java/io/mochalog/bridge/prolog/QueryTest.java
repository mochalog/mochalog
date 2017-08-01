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

package io.mochalog.bridge.prolog;

import io.mochalog.bridge.prolog.lang.Variable;
import io.mochalog.bridge.prolog.namespace.NoSuchVariableException;
import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.QuerySolution;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for Java to Prolog queries
 * using abstracted Mochalog query interface
 */
public class QueryTest
{
    /**
     * Performs a Mochalog query and validates whether
     * solutions retrieved match expected values
     */
    @Test
    public void checkQuerySolutions()
    {
        // hello_world.pl test resource
        // Filepath relative to java-bridge directory
        final String helloWorldPrologFilePath = "src/test/resources/prolog/hello_world.pl";

        // Ensure Prolog file was correctly loaded
        // by SWI-Prolog interpreter
        boolean loaded = org.jpl7.Query.hasSolution("consult('" + helloWorldPrologFilePath + "')");
        assert(loaded);

        // Solutions expected from get_hello_world query
        final String[] expectedSolutions = { "hello", "world" };

        // Perform query to Prolog file
        Query query = Query.format("get_hello_world(X)");

        int solutionIndex = 0;
        int expectedSolutionCount = expectedSolutions.length;

        for (QuerySolution solution : query)
        {
            // Ensure we have not received more solutions than
            // expected
            assert(solutionIndex < expectedSolutionCount);
            // Ensure the fetched solution corresponds to
            // what was expected
            try
            {
                assertEquals(expectedSolutions[solutionIndex++], solution.get("X").toString());
            }
            catch (NoSuchVariableException e)
            {
                fail();
            }
        }

        // Ensure we have received the amount of solutions
        // we expected
        assertEquals(expectedSolutionCount, solutionIndex);
    }
}
