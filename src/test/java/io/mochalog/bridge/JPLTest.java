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

package io.sarl.extras;

import org.jpl7.*;

import org.junit.Test;

import java.lang.Integer;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test suite for JPL (Java to Prolog direction).
 * <p>
 * Ensures SWI-Prolog code can be invoked from JVM via JPL.
 *
 */
public class JPLTest
{
    final String testKBFilePath = "src/test/resources/prolog/testKB.pl";


    /**
     * Ensure jpl.dll or libjpl.so and dependencies are able to be loaded
     * by the JVM. All subsequent tests which rely on JPL functionality
     * will fail given this test fails.
     */
    @Test
    public void isJPLNativeBinaryLoadable()
    {
        try
        {
            // Attempt to load JPL native library given
            // specified java.librry.path
            JPL.loadNativeLibrary();
        }
        catch (UnsatisfiedLinkError e)
        {
            String swiPrologHomeDir = System.getenv("SWI_HOME_DIR");

            // Check if SWI_HOME_DIR is set
            String message = "jpl library or any one of its dependencies " +
                    "failed to be found.";
            if (swiPrologHomeDir == null)
            {
                fail(message + " SWI_HOME_DIR system environment variable not set.");
            }
            else
            {
                fail(message + " Ensure that the SWI_HOME_DIR/bin directory " +
                        "has been added to the system path.");
            }
        }
    }

    /**
     * Check that basic string is correctly returned from
     * SWI-Prolog via JPL query
     */
    @Test
    public void basicJavaToPrologQuery()
    {
        // hello_world.pl test resource
        // Filepath relative to java-api directory

        boolean loaded = consultKnowledgeBase(testKBFilePath);
        assert(loaded);

        // Fetch the Term object which gets bound to the specified
        // variable
        Query query = new Query("get_hello_world(X)");
        Map<String, Term> binding = query.oneSolution();

        Term result = binding.get("X");
        // Ensure first solution string (hello) was correctly fetched
        // from Prolog file
        assertEquals("hello", result.toString());
    }


    /**
     * Check a query against a string
     */
    @Test
    public void stringPrologQuery()
    {
        String queryText;
        Query query;
        boolean hasSolution;

        // hello_world.pl test resource
        // Filepath relative to java-api directory

        boolean loaded = consultKnowledgeBase(testKBFilePath);
        assertTrue("Test KB was not consulted successfully", loaded);

        // This query should succeed in Prolog, but not from JPL as a string is coverted into a Prolog text atom
        // Similarly, one cannot query a Prolog predicate that yields as String: build a wrapper to give atoms
        queryText = "data_string(\"string0\")";
        hasSolution = Query.hasSolution(queryText);
        assertFalse(String.format("No solution was found for query **%s**! Java String are mapped to Atoms of type text", queryText), hasSolution);

        /// This is how the above query should be done via a wrapper in Prolog that converts Strings <-> Atoms
        queryText = "data_string_wrapper(string0)";
        hasSolution = Query.hasSolution(queryText);
        assertTrue(String.format("Solution was NOT found for query **%s**!", queryText), hasSolution);


    }



    /**
     * Check a query against a string
     */
    @Test
    public void JRefQuery()
    {
        String queryText;
        Query query;
        boolean hasSolution;

        // hello_world.pl test resource
        // Filepath relative to java-api directory

        boolean loaded = consultKnowledgeBase(testKBFilePath);
        assertTrue("Test KB was not consulted successfully", loaded);


        Integer obj_integer = new Integer(232);
        System.out.println("The integer value of JAVA object obj_integer is " + obj_integer.intValue());
        Query q = new Query("print_integer", new Term[] {new JRef(obj_integer), new Variable(("N"))});
//        hasSolution = q.hasSolution();
        int n = q.oneSolution().get("N").intValue();
        System.out.println("The integer value of JAVA object obj_integer returned from Prolog was " + n);
        assertEquals("Query print_integer(JREF(x)) did not suceed and it should have!", 232, n);



        // This query should succeed in Prolog, but not from JPL as a string is coverted into a Prolog text atom
        // Similarly, one cannot query a Prolog predicate that yields as String: build a wrapper to give atoms
//        queryText = "data_string(\"string0\")";
//        hasSolution = Query.hasSolution(queryText);
//        assertFalse(String.format("Solution was found for query **%s**, it should not as Java String are mapped " +
//                "to Atoms of type text", queryText), hasSolution);

    }


    /**
     * Load the Prolog file at filepath into the SWI-Prolog interpreter
     * @param filePath Path of Prolog file
     * @return Success status
     */
    private boolean consultKnowledgeBase(String filePath)
    {
        String queryString = String.format("consult('%s')", filePath);
        return Query.hasSolution(queryString);
    }
}
