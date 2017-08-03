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

import io.mochalog.bridge.prolog.lang.Module;
import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.QuerySolution;
import io.mochalog.bridge.prolog.query.QuerySolutionList;
import io.mochalog.bridge.prolog.query.collectors.QuerySolutionCollector;
import io.mochalog.bridge.prolog.query.collectors.SequentialQuerySolutionCollector;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface to sandboxed SWI-Prolog interpreter context
 */
public class Prolog
{
    private Module workingModule;

    /**
     * Constructor.
     * @param name Working module name
     */
    public Prolog(String name)
    {
        this(new Module(name));
    }

    /**
     * Constructor.
     * @param module Working module
     */
    public Prolog(Module module)
    {
        setWorkingModule(module);
    }

    /**
     * Get module queries are being routed to
     * in current Prolog context
     * @return Working module
     */
    public Module getWorkingModule()
    {
        return workingModule;
    }

    /**
     * Set module queries are being routed to
     * in current Prolog context
     * @param module Working module
     */
    public void setWorkingModule(Module module)
    {
        this.workingModule = module;
    }

    /**
     * Load a given file into the current context
     * @param path Path to Prolog source file
     * @return True if file loading was successful, false otherwise.
     * @throws IOException File IO error occurred
     */
    public boolean loadFile(Path path) throws IOException
    {
        return workingModule.importFile(path);
    }

    /**
     * Helper method wrapping assertz/1 (add a new clause to the end of a predicate)
     * @param term Term to assert
     * @param args Substitution arguments to apply to term
     * @return True if assertion succeeded, false otherwise.
     */
    public boolean assertLast(String term, Object... args)
    {
        Query query = Query.format("assertz(" + term + ")", args);
        return prove(query);
    }

    /**
     * Helper method wrapping retract/1 (remove first clause in predicate)
     * @param term Term to retract
     * @return True if retraction succeeded, false otherwise.
     */
    public boolean retract(String term)
    {
        return prove("retract(" + term + ")");
    }

    /**
     * Helper method wrapping retractall/1 (remove all clauses in predicate)
     * @param term Term to retract
     * @return True if retraction succeeded, false otherwise
     */
    public boolean retractAll(String term)
    {
        return prove("retractall(" + term + ")");
    }

    /**
     * Verify if textual query is provable
     * @param text Query text
     * @return True if provable, false otherwise.
     */
    public boolean prove(String text)
    {
        return prove(new Query(text));
    }

    /**
     * Verify if query is provable
     * @param query Query to prove
     * @return True if provable, false otherwise
     */
    public boolean prove(Query query)
    {
        return ask(query).hasSolutions();
    }

    /**
     * Ask for solution to given query.
     * <p>
     * Useful when only single solution is necessary
     * @param query Query to fetch solution to
     * @return Solution
     */
    public QuerySolution askForSolution(Query query)
    {
        return ask(query).fetchFirstSolution();
    }

    /**
     * Ask for list view of solutions to given query.
     * @param query Query to fetch solutions to
     * @return Solution list
     */
    public QuerySolutionList askForSolutions(Query query)
    {
        return new QuerySolutionList(query, workingModule);
    }

    /**
     * Open a new query session in SWI-Prolog interpreter
     * @param query Query to open
     * @return Query session
     */
    public QuerySolutionCollector ask(Query query)
    {
        SequentialQuerySolutionCollector.Builder builder =
            new SequentialQuerySolutionCollector.Builder(query);
        builder.setWorkingModule(workingModule);
        return builder.build();
    }
}
