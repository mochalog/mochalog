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
import io.mochalog.bridge.prolog.query.MQuery;
import io.mochalog.bridge.prolog.query.MQuerySolution;
import io.mochalog.bridge.prolog.query.MQuerySolutionIterator;
import io.mochalog.bridge.prolog.query.MQuerySolutionList;
import io.mochalog.bridge.prolog.query.exception.NoSuchSolutionException;
import org.jpl7.*;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.jpl7.Util.textToTerm;
import static org.junit.Assert.*;

/**
 * Test suite for JPL (Java to Prolog direction).
 * <p>
 * Ensures SWI-Prolog code can be invoked from JVM via JPL.
 */
public class MochaTest2 {
    /**
     * Performs a Mochalog queries and validates whether
     * solutions retrieved match expected values
     */
    private static String testKBFilePath = "src/test/resources/prolog/testKB.pl";
    private boolean DEBUG = false;


    @Test
    public void compoundMochaJavaToPrologQuery() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        if (DEBUG) System.out.println("================= TEST: " + methodName);

        // hello_world.pl test resource
        // Filepath relative to java-api directory
        SandboxedPrologContext prolog = new SandboxedPrologContext("askForSolution_compound");

        boolean loaded = false;
        try {
            loaded = prolog.importFile(testKBFilePath);
        } catch (IOException e) {
            fail("My method throw when I expected not to");
            e.printStackTrace();
        }
        assert (loaded);

        // WORKS
//        MQuery query = MQuery.format("data(mother(age(@A,add(@I,1)),father(peter,@A),X))","john",32,"mark");
//        MQuerySolution solution = prolog.askForSolution(query);
//        Term valueX = solution.get("X");
//        assertEquals(23.222, valueX.floatValue(), 0.05);    // Check correctness

        if (DEBUG) System.out.println(String.format("################ TEST %s DONE!", methodName));
    }


    /**
     * Uses placeholders ? to inject terms
     */
    @Test
    public void QueryWithPlaceholders()
    {
        String queryText;
        Query query;
        boolean hasSolution;


        String queryString = String.format("consult('%s')", testKBFilePath);
        Query.hasSolution(queryString);



        Query q = new Query("person(john, ?, ?)", new Term[] {new Variable("N"), new Atom("melbourne")});
        int n = q.oneSolution().get("N").intValue();
        System.out.println("The integer value of JAVA object obj_integer returned from Prolog was " + n);


    }

}

