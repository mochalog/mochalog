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

import org.jpl7.JPL;
import org.jpl7.Query;
import org.jpl7.Term;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

/**
 * Test suite for JPL (Java to Prolog direction).
 * This does not uses Mochalog facilities
 * <p>
 * Ensures SWI-Prolog code can be invoked from JVM via JPL.
 */
public class JPLTest
{
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
            // specified java.library.path
            JPL.loadNativeLibrary();
        }
        catch (UnsatisfiedLinkError e)
        {
            String swiPrologHomeDir = System.getenv("SWI_HOME_DIR");

            // Check if SWI_HOME_DIR is set
            String message = "jpl library or any one of its dependencies failed to be found.";
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
        final String helloWorldPrologFilePath = "src/test/resources/prolog/hello_world.pl";

        boolean loaded = consultKnowledgeBase(helloWorldPrologFilePath);
        assert(loaded);

        // Fetch the Term object which gets bound to the specifie variable
        Query query = new Query("get_hello_world(X)");
        Map<String, Term> binding = query.oneSolution();

        Term result = binding.get("X");
        // Ensure first solution string (hello) was correctly fetched
        // from Prolog file
        assertEquals("hello", result.toString());
    }

    /**
     * Load the Prolog file at filepath into the SWI-Prolog interpreter
     * @param filePath Path of Prolog file
     * @return Success status
     */
    private boolean consultKnowledgeBase(String filePath)
    {
        String queryString = "consult('" + filePath + "')";
        return Query.hasSolution(queryString);
    }



    /**
     * Check that basic string is correctly returned from
     * SWI-Prolog via JPL query
     */
    @Test
    public void bindingMappingQuery()
    {
        // hello_world.pl test resource
        // Filepath relative to java-api directory
        final String helloWorldPrologFilePath = "src/test/resources/prolog/hello_world.pl";

        boolean loaded = consultKnowledgeBase(helloWorldPrologFilePath);
        assert(loaded);

        // Fetch the Term object which gets bound to the specified
        // variable

        Query query = new Query("person(X,Y,Z)");
        Map<String, Term> binding = query.oneSolution();

        Term resultX = binding.get("X");
        Term resultY = binding.get("Y");
        Term resultZ = binding.get("Z");
        // Ensure first solution string (hello) was correctly fetched
        // from Prolog file
        assertEquals("john", resultX.toString());
        assertEquals(20, resultY.intValue());
        assertEquals("melbourne", resultZ.toString());
    }


}
