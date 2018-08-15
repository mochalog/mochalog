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

package io.mochalog.bridge;

//import org.jpl7.JPL;
//import org.jpl7.Query;
//import org.jpl7.Term;


import io.mochalog.bridge.prolog.PrologContext;
import io.mochalog.bridge.prolog.SandboxedPrologContext;
import io.mochalog.bridge.prolog.namespace.NoSuchVariableException;
import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.QuerySolution;
import io.mochalog.bridge.prolog.query.QuerySolutionIterator;
import io.mochalog.bridge.prolog.query.QuerySolutionList;
import io.mochalog.bridge.prolog.query.exception.NoSuchSolutionException;
import org.jpl7.Term;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.jpl7.Util.textToTerm;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test suite for JPL (Java to Prolog direction).
 * <p>
 * Ensures SWI-Prolog code can be invoked from JVM via JPL.
 */
public class MochaTest
{
    /**
     * Performs a Mochalog queries and validates whether
     * solutions retrieved match expected values
     */
    private static String helloWorldTestFile = "src/test/resources/prolog/hello_world.pl";


    @Test
    public void basicMochaJavaToPrologQuery()
    {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("================= TEST: " + methodName);

        // hello_world.pl test resource
        // Filepath relative to java-api directory
        SandboxedPrologContext prolog = new SandboxedPrologContext("askForSolution");

        boolean loaded = false;
        try {
            loaded = prolog.importFile(helloWorldTestFile);
        } catch (IOException e) {
            fail( "My method throw when I expected not to" );
            e.printStackTrace();
        }
        assert(loaded);

        // Fetch the Term object which gets bound to the specific variable
        Query query = new Query("get_hello_world(X)");
        QuerySolution solution = prolog.askForSolution(query);
        Term valueX = solution.get("X");
        // Ensure first solution string (hello) was correctly fetched from Prolog file
        assertEquals("hello", valueX.toString());

        // Now get the second solution ("world")
        solution = prolog.askForSolution(query, 1);
        assertEquals("world", solution.get("X").toString());

        // get_hello_world(X) has just two solutions, so this must throw exception
        try {
            solution = prolog.askForSolution(query, 2);
            fail( "My method didn't throw when I expected it to" );
        } catch (NoSuchSolutionException e) {
        }


        System.out.println(String.format("################ TEST %s DONE!", methodName));
    }

    /**
     * Check for a query that gets all solutions at once
     * @throws IOException
     */
    @Test
    public void queryAllSolutionsTest() throws IOException
    {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("================= TEST: " + methodName);

        // hello_world.pl test resource
        // Filepath relative to java-bridge directory
        PrologContext prolog = new SandboxedPrologContext("all_solution_test");
        // Ensure Prolog file was correctly loaded
        // by SWI-Prolog interpreter
        assert(prolog.importFile(helloWorldTestFile));

        // Solutions expected from get_hello_world query
        final String[] expectedSolutions = { "hello", "world" };

        // Perform all solution query
        Query query = Query.format("get_hello_world(X)");
        int solutionIndex = 0;
        QuerySolutionList solutionList = null;
        try {
            solutionList = prolog.askForAllSolutions(query);
            System.out.println("Number of solutions found in all test: " + solutionList.size());
        } catch (Exception e) {
            fail("Unexpected exception because there are solutions!");
            e.printStackTrace();
        }

        // Ensure we have received the amount of solutions we expected
        assertEquals(expectedSolutions.length, solutionList.size());

        for (QuerySolution solution : solutionList)
        {
            // Ensure we have not received more solutions than expected
            assert(solutionIndex < expectedSolutions.length);
            // Ensure the fetched solution corresponds to what was expected
            try
            {
                assertEquals(expectedSolutions[solutionIndex], solution.get("X").toString());
                solutionIndex++;
            }
            catch (NoSuchVariableException e)
            {
                fail();
            }
        }

        System.out.println(String.format("################ TEST %s DONE!", methodName));
    }


    /**
     * Check iterator query test. Solutions are generated by one one
     * @throws IOException
     */
    @Test
    public void queryIterativeTest() throws IOException
    {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("================= TEST: " + methodName);

        // hello_world.pl test resource
        // Filepath relative to java-bridge directory
        PrologContext prolog = new SandboxedPrologContext("iter_solution_test");
        // Ensure Prolog file was correctly loaded
        // by SWI-Prolog interpreter
        assert(prolog.importFile(helloWorldTestFile));

        // Solutions expected from get_hello_world query
        final int[] expectedSolutions = { 80, 2, 4, 6, 8 };

        // Perform iterative solution query (without delay)
        Query query = Query.format("test(X)");
        int solutionIndex = 0;
        QuerySolutionIterator solutionList = new QuerySolutionIterator(query);

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        while (solutionList.hasNext()) {
            QuerySolution solution = solutionList.next();
            assertEquals(expectedSolutions[solutionIndex], solution.get("X").intValue());
            solutionIndex++;
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }

        System.out.println(String.format("################ TEST %s DONE!", methodName));
    }

    /**
     * Check iterator query test, where each goal has a delay in the prolog kb. Solutions are generated by one one
     * @throws IOException
     */
    @Test
    public void queryIterativeTestWithDelay() throws IOException
    {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("================= TEST: " + methodName);

        // hello_world.pl test resource
        // Filepath relative to java-bridge directory
        PrologContext prolog = new SandboxedPrologContext("iter_solution_test");
        // Ensure Prolog file was correctly loaded
        // by SWI-Prolog interpreter
        assert(prolog.importFile(helloWorldTestFile));

        // Solutions expected from get_hello_world query
        final int[] expectedSolutions = { 80, 2, 4, 6, 8 };

        // Perform iterative solution query (without delay)
        Query query = Query.format("test_slow(Y)");
        int solutionIndex = 0;
        QuerySolutionIterator solutionList = new QuerySolutionIterator(query);

        QuerySolution solution;
        while (solutionList.hasNext()) {
            solution = solutionList.next();
            System.out.println("Value of X (delayed): " + solution.get("Y"));
            assertEquals(expectedSolutions[solutionIndex], solution.get("Y").intValue());
            solutionIndex++;
        }
        try {
            solution = solutionList.next();
            fail("There should have been a NoSuchElementException exception!");
        } catch (NoSuchElementException e) {
        }

        System.out.println(String.format("################ TEST %s DONE!", methodName));
    }


    /**
     * Check iterator query test, for an infinite goal.
     * All solutions will not return, but iterator works here because it gives one by one.
     * @throws IOException
     */
    @Test
    public void queryIterativeTestForever() throws IOException
    {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("================= TEST: " + methodName);

        final int noSolToTest = 15;

        // hello_world.pl test resource
        // Filepath relative to java-bridge directory
        PrologContext prolog = new SandboxedPrologContext("iter_solution_test");
        // Ensure Prolog file was correctly loaded
        // by SWI-Prolog interpreter
        assert(prolog.importFile(helloWorldTestFile));

        // Perform iterative solution query (without delay)
        Query query = Query.format("test_forever(Y)");
        int solutionIndex = 0;
        QuerySolutionIterator solutionList = new QuerySolutionIterator(query);

        QuerySolution solution;
        while (solutionList.hasNext() && solutionIndex < noSolToTest) {
            solution = solutionList.next();
            System.out.println("Value of X (forever): " + solution.get("Y"));
            solutionIndex++;
        }

        System.out.println(String.format("################ TEST %s DONE!", methodName));
    }


    /**
     * Test if query setter syntax (previousValue <- newValue)
     * is functional in a simulated scenario
     */
    @Test
    public void querySetterTest()
    {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("================= TEST: " + methodName);

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
        // Equivalent to:
        //  retract(student(Student,0)),
        //  assert(student(Student,2)),
        //  retract(school(Student, CurrentSchool)),
        //  assert(school(Student, newSchoolName)
        String newSchoolName = "New Simulated School";
        assert(prolog.prove("student(Student, 0 <- 2), school(Student, CurrentSchool <- @S)",
                newSchoolName));

        // Check if values were correctly set/exchanged in the previous query
        QuerySolution solution =
                prolog.askForSolution("student(student_a, StudentId), school(student_a, School)");
        assertEquals(2, solution.get("StudentId").intValue());
        assertEquals("\'New Simulated School\'", solution.get("School").toString());

        System.out.println(String.format("################ TEST %s DONE!", methodName));
    }

    /**
     * Ensure all data is completely sandboxed through modules.
     */
    @Test
    public void ensureSandboxedModules()
    {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("================= TEST: " + methodName);

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

        System.out.println(String.format("################ TEST %s DONE!", methodName));
    }



    /**
     * Check how to extract Map<String, Term> bindings from soluttions
     */
    @Test
    public void bindingMappingQuery()
    {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("================= TEST: " + methodName);

        // Solutions expected from get_hello_world query
        Map<String, Term> expectedSolutions = new HashMap<String,Term>();
        expectedSolutions.put("X",textToTerm("john"));
        expectedSolutions.put("Y",textToTerm("20"));
        expectedSolutions.put("Z",textToTerm("melbourne"));

        SandboxedPrologContext prolog = new SandboxedPrologContext("askForSolution");
        boolean loaded = false;
        try {
            loaded = prolog.importFile(helloWorldTestFile);
        } catch (IOException e) {
            fail( "My method throw when I expected not to" );
            e.printStackTrace();
        }
        assert(loaded);

        // Fetch the Term object which gets bound to the specified
        // variable

        Query query = new Query("person(X,Y,Z)");
        QuerySolution solution = prolog.askForSolution(query);

        Map<String, Term> bindings = solution.getMap();

        for(String varName : bindings.keySet()) {
            System.out.print(String.format("Value of variable %s is %s \n", varName, bindings.get(varName).toString()));
            assertEquals(bindings.get(varName), expectedSolutions.get(varName));

        }


        System.out.println(String.format("################ TEST %s DONE!", methodName));
    }


}
