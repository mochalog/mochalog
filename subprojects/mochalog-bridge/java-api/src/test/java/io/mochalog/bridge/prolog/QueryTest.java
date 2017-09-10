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

import io.mochalog.bridge.prolog.namespace.NoSuchVariableException;
import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.QuerySolution;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;

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
    public void querySolutionsTest() throws IOException
    {
        // hello_world.pl test resource
        // Filepath relative to java-bridge directory
        PrologContext prolog = new SandboxedPrologContext("query_solution_test");
        // Ensure Prolog file was correctly loaded
        // by SWI-Prolog interpreter
        assert(prolog.importFile("src/test/resources/prolog/hello_world.pl"));

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

    /**
     * Test if query setter syntax (previousValue <- newValue)
     * is functional in a simulated scenario
     */
    @Test
    public void querySetterTest()
    {
        PrologContext prolog = new SandboxedPrologContext("query_setter_test");

        // student(:Name, :StudentId)
        assert(prolog.assertFirst("student(student_a, 0)"));
        assert(prolog.assertFirst("student(student_b, 1)"));

        // school(:StudentName, :SchoolName)
        String schoolName = "Simulated School";
        assert(prolog.assertFirst("school(student_a, @S)", schoolName));
        assert(prolog.assertFirst("school(student_b, @S)", schoolName));

        // Attempt to change the school of the student with ID 0 to 'New Simulated School'
        // and change their given student ID to 2
        String newSchoolName = "New Simulated School";
        assert(prolog.prove("student(Student, 0 <- 2), school(Student, CurrentSchool <- @S)",
            newSchoolName));

        // Check if values were correctly set
        QuerySolution solution =
            prolog.askForSolution("student(student_a, StudentId), school(student_a, School)");
        assertEquals(2, solution.get("StudentId").intValue());
        assertEquals("\'New Simulated School\'", solution.get("School").toString());
    }

    /**
     * Ensure all data is completely sandboxed through modules.
     */
    @Test
    public void ensureSandboxedModules()
    {
        PrologContext firstModule = new SandboxedPrologContext("module_1");
        PrologContext secondModule = new SandboxedPrologContext("module_2");

        // Ensure that removing data will not result in exceptions
        assert(firstModule.prove("dynamic module_2_data/0"));
        assert(secondModule.prove("dynamic module_1_data/0"));

        // Ensure data is not added/accessible by both modules
        assert(firstModule.assertFirst("module_1_data"));
        assert(secondModule.assertFirst("module_2_data"));

        assert(firstModule.prove("module_1_data"));
        assertFalse(firstModule.prove("module_2_data"));

        assert(secondModule.prove("module_2_data"));
        assertFalse(secondModule.prove("module_1_data"));

        assert(firstModule.assertFirst("shared_predicate"));
        assert(secondModule.assertFirst("shared_predicate"));

        // Ensure data is not removed from both modules
        assert(firstModule.retract("shared_predicate"));
        assert(secondModule.prove("shared_predicate"));
    }
}
