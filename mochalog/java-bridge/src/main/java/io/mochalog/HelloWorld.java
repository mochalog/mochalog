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

package io.mochalog;

import org.jpl7.Query;
import org.jpl7.Term;

import java.util.Map;

/**
 * Simple test for JPL bridge from Java to SWI-Prolog
 * <p>
 * Currently not implemented as unit test as Travis-CI
 * would fail (need to pre-install SWI-Prolog dependencies)
 */
public class HelloWorld
{
    public static void main(String[] args)
    {
        queryHelloWorld(args[0]);
    }

    /**
     * Perform a query on the hello_world Prolog file
     * @param filepath Path to hello_world Prolog file
     */
    public static void queryHelloWorld(String filepath)
    {
        // Consult the given Prolog file
        if (!consultKnowledgeBase(filepath))
        {
            System.err.println("File with path " + filepath + "does not exist.");
            return;
        }

        // Query the 'hello_world' predicate
        Term result = get_hello_world("X");
        if (result != null)
        {
            System.out.println("SWI-Prolog returned: " + result);
        }
        else
        {
            System.err.println("Query on given Prolog knowledge base failed.");
        }
    }

    /**
     * Load the Prolog file at filepath into the SWI-Prolog interpreter
     * @param filepath Path of Prolog file
     * @return Success status
     */
    public static boolean consultKnowledgeBase(String filepath)
    {
        String queryString = "consult('" + filepath + "')";
        return Query.hasSolution(queryString);
    }

    /**
     * Query the hello_world predicate
     * @param variable Variable string to bind to
     * @return
     */
    public static Term get_hello_world(String variable)
    {
        // Fetch the Term object which gets bound to the specified
        // variable
        Query query = new Query("get_hello_world(" + variable + ")");
        Map<String, Term> binding = query.oneSolution();
        return binding.get(variable);
    }
}
