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

import io.mochalog.bridge.prolog.query.exception.NoSuchSolutionException;
import io.mochalog.util.format.Formatter;
import org.jpl7.Term;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface to <i>sandboxed</i> SWI-Prolog interpreter context.
 * Context provides access controls via SWI-Prolog modules allowing
 * knowledge bases to be segregated in a concurrent environment
 */
public class SandboxedPrologContext implements PrologContext
{
    // Module from which queries will be scoped
    private Module workingModule;

    /**
     * Constructor.
     * @param name Working module name
     */
    public SandboxedPrologContext(String name)
    {
        this(new Module(name));
    }

    /**
     * Constructor.
     * @param module Working module
     */
    public SandboxedPrologContext(Module module)
    {
        setWorkingModule(module);
    }

    /**
     * Get module queries are being scoped from
     * in current Prolog context
     * @return Working module
     */
    public Module getWorkingModule()
    {
        return workingModule;
    }

    /**
     * Set module queries are being scoped from
     * in current Prolog context
     * @param module Working module
     */
    public void setWorkingModule(Module module)
    {
        this.workingModule = module;
    }

    @Override
    public boolean loadFile(Path path) throws IOException
    {
        return workingModule.importFile(path);
    }

    @Override
    public Term get(String name)
    {
        return askForSolution("@A(Result)", name).get("Result");
    }

    @Override
    public boolean assertFirst(String clause, Object... args)
    {
        return modifyDynamicPredicate("asserta", clause, args);
    }

    @Override
    public boolean assertLast(String clause, Object... args)
    {
        return modifyDynamicPredicate("assertz", clause, args);
    }

    @Override
    public boolean retract(String clause, Object... args)
    {
        return modifyDynamicPredicate("retract", clause, args);
    }

    @Override
    public boolean retractAll(String clause, Object... args)
    {
        return modifyDynamicPredicate("retractall", clause, args);
    }

    /**
     * Perform a modification of the SWI-Prolog clause database
     * (e.g. asserta, assertz, retract, retractall, etc.)
     * @param modifier Modification meta-predicate to apply
     * @param clause Clause to modify dynamic predicate with
     * @param args Substitution arguments to apply to clause
     * @return True if modification succeeded, false otherwise.
     */
    // TODO: The existence of this method makes the case for either an
    // AbstractPrologContext or recursive query formatting
    private boolean modifyDynamicPredicate(String modifier, String clause, Object... args)
    {
        // Apply substitution arguments to internal clause argument
        // before formatting metapredicate
        Formatter formatter = new Query.Formatter();
        String formattedClause = formatter.format(clause, args);
        // Perform meta-predicate query allowing for modification
        // of dynamic predicates
        return prove("@A(@A)", modifier, formattedClause);
    }

    @Override
    public boolean prove(String text, Object... args)
    {
        return prove(Query.format(text, args));
    }

    @Override
    public boolean prove(Query query)
    {
        return ask(query).hasSolutions();
    }

    @Override
    public QuerySolution askForSolution(String text, Object... args)
        throws NoSuchSolutionException
    {
        return askForSolution(Query.format(text, args));
    }

    @Override
    public QuerySolution askForSolution(Query query)
        throws NoSuchSolutionException
    {
        return ask(query).fetchFirstSolution();
    }

    @Override
    public QuerySolution askForSolution(Query query, int index)
        throws NoSuchSolutionException
    {
        return ask(query).fetchSolution(index);
    }

    @Override
    public QuerySolutionList askForAllSolutions(String text, Object... args)
    {
        return askForAllSolutions(Query.format(text, args));
    }

    @Override
    public QuerySolutionList askForAllSolutions(Query query)
    {
        return new QuerySolutionList(query, workingModule);
    }

    @Override
    public QuerySolutionCollector ask(String text, Object... args)
    {
        return ask(Query.format(text, args));
    }

    @Override
    public QuerySolutionCollector ask(Query query)
    {
        SequentialQuerySolutionCollector.Builder builder =
            new SequentialQuerySolutionCollector.Builder(query);
        builder.setWorkingModule(workingModule);
        return builder.build();
    }

    @Override
    public String toString()
    {
        return workingModule.getName();
    }
}
