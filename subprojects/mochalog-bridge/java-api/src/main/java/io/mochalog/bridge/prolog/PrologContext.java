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

import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.QuerySolution;
import io.mochalog.bridge.prolog.query.QuerySolutionList;
import io.mochalog.bridge.prolog.query.collectors.QuerySolutionCollector;

import java.nio.file.Path;
import java.io.IOException;

/**
 * Interface to the SWI-Prolog intepreter context, allowing
 * manipulation of a constructed knowledge base in a Java context
 */
public interface PrologContext
{
    /**
     * Load a given file into the current context
     * @param path Path to Prolog source file
     * @return True if file loading was successful, false otherwise.
     * @throws IOException File IO error occurred
     */
    boolean loadFile(Path path) throws IOException;

    /**
     * Wrapper of asserta/1 (add a new clause to the start of a predicate)
     * @param clause Clause to assert
     * @param args Substitution arguments to apply to clause
     * @return True if assertion succeeded, false otherwise.
     */
    boolean assertFirst(String clause, Object... args);

    /**
     * Wrapper of assertz/1 (add a new clause to the end of a predicate)
     * @param clause Clause to assert
     * @param args Substitution arguments to apply to clause
     * @return True if assertion succeeded, false otherwise.
     */
    boolean assertLast(String clause, Object... args);

    /**
     * Wrapper of retract/1 (remove first matching term in predicate)
     * @param term Term to retract
     * @param args Substitution arguments to apply to term
     * @return True if retraction succeeded, false otherwise.
     */
    boolean retract(String term, Object... args);

    /**
     * Wrapper of retractall/1 (remove all matching terms in predicate)
     * @param term Term to retract
     * @param args Substitution arguments to apply to term
     * @return True if retraction succeeded, false otherwise.
     */
    boolean retractAll(String term, Object... args);

    /**
     * Verify if unformatted textual query is provable
     * @param text Query text
     * @param args Substitution arguments to apply to text
     * @return True if provable, false otherwise.
     */
    boolean prove(String text, Object... args);

    /**
     * Verify if pre-constructed query is provable
     * @param query Query to prove
     * @return True if provable, false otherwise
     */
    boolean prove(Query query);

    /**
     * Ask for first solution to given unformatted query.
     * <p>
     * Useful when only single solution is necessary
     * @param text Query text to fetch solution of
     * @param args Substitution arguments to apply to text
     * @return Solution
     */
    QuerySolution askForSolution(String text, Object... args);

    /**
     * Ask for first solution to given query.
     * <p>
     * Useful when only single solution is necessary
     * @param query Query to fetch solution of
     * @return Solution
     */
    QuerySolution askForSolution(Query query);

    /**
     * Ask for solution in a given index to a query.
     * @param query Query to fetch solution of
     * @param index Index of solution to fetch
     * @return Solution
     */
    QuerySolution askForSolution(Query query, int index);

    /**
     * Ask for list view of all solutions to unformatted query.
     * @param text Query text to fetch solutions of
     * @param args Substitution arguments to apply to text
     * @return Solution list
     */
    QuerySolutionList askForAllSolutions(String text, Object... args);

    /**
     * Ask for list view of all solutions to given query.
     * @param query Query to fetch solutions to
     * @return Solution list
     */
    QuerySolutionList askForAllSolutions(Query query);

    /**
     * Open a new query session (unformatted query) in SWI-Prolog
     * interpreter, from which query solutions can be streamed and
     * manipulated
     * @param text Query text
     * @param args Substitution arguments to apply to text
     * @return Query solution collector for constructed query session
     */
    QuerySolutionCollector ask(String text, Object... args);

    /**
     * Open a new query session in SWI-Prolog interpreter,
     * from which query solutions can be streamed and manipulated
     * @param query Query to open
     * @return Query solution collector for constructed query session
     */
    QuerySolutionCollector ask(Query query);
}
