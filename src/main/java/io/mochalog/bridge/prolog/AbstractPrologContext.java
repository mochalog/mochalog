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

import io.mochalog.bridge.prolog.query.MQuery;
import io.mochalog.bridge.prolog.query.MQuerySolution;
import io.mochalog.bridge.prolog.query.MQuerySolutionIterator;
import io.mochalog.bridge.prolog.query.MQuerySolutionList;
import io.mochalog.bridge.prolog.query.collectors.MQuerySolutionCollector;

import io.mochalog.bridge.prolog.query.exception.NoSuchSolutionException;
import io.mochalog.util.format.Formatter;

import org.jpl7.Term;

/**
 * Abstract implementation of an interface to the SWI-Prolog interpreter.
 */
public abstract class AbstractPrologContext implements PrologContext
{
    @Override
    public Term get(String name)
    {
        return askForSolution("@A(Result)", name).get("Result");
    }

    @Override
    public boolean assertFirst(String term, Object... args)
    {
        return applyMetaPredicate("asserta", term, args);
    }

    @Override
    public boolean assertLast(String term, Object... args)
    {
        return applyMetaPredicate("assertz", term, args);
    }

    @Override
    public boolean retract(String term, Object... args)
    {
        return applyMetaPredicate("retract", term, args);
    }

    @Override
    public boolean retractAll(String term, Object... args)
    {
        return applyMetaPredicate("retractall", term, args);
    }

    @Override
    public boolean prove(String text, Object... args)
    {
        return prove(MQuery.format(text, args));
    }

    /**
     * Prove the query once for the first solution, no bindings, just true/false
     *
     * @param query Query to prove
     * @return if query was proved true
     */
    @Override
    public boolean prove(MQuery query)
    {
        MQuerySolutionCollector collector = ask(query);
        boolean result = collector.hasSolutions();
        // Ensure temporary collector closed
        collector.detach();
        return result;
    }

    /**
     * Get the results for the first solution only, input with format to replace
     *
     * @param text Query text to fetch solution of
     * @param args Substitution arguments to apply to text
     * @return one solution binding, the first one
     * @throws NoSuchSolutionException
     */
    @Override
    public MQuerySolution askForSolution(String text, Object... args)
            throws NoSuchSolutionException
    {
        return askForSolution(MQuery.format(text, args));
    }

    /**
     * Get the results for the first solution only
     *
     * @param query Query to fetch solution of
     * @return one solution binding, the first one
     * @throws NoSuchSolutionException
     */
    @Override
    public MQuerySolution askForSolution(MQuery query)
            throws NoSuchSolutionException
    {
        MQuerySolutionCollector collector = ask(query);
        try
        {
            return collector.fetchFirstSolution();
        }
        finally
        {
            // Ensure temporary collector closed
            // regardless of result
            collector.detach();
        }
    }

    @Override
    public MQuerySolution askForSolution(MQuery query, int index)
            throws NoSuchSolutionException
    {
        MQuerySolutionCollector collector = ask(query);
        try
        {
            return collector.fetchSolution(index);
        }
        finally
        {
            // Ensure temporary collector closed
            // regardless of result
            collector.detach();
        }
    }

    @Override
    public MQuerySolutionList askForAllSolutions(String text, Object... args)
    {
        return askForAllSolutions(MQuery.format(text, args));
    }

    @Override
    public MQuerySolutionCollector ask(String text, Object... args)
    {
        return ask(MQuery.format(text, args));
    }


    @Override
    public MQuerySolutionIterator askIter(String text, Object... args)
    {
        return askIter(MQuery.format(text, args));
    }
    @Override
    public MQuerySolutionIterator askIter(MQuery query)
    {
        return new MQuerySolutionIterator(ask(query));
    }


    /**
     * Perform a SWI-Prolog meta-predicate on a given
     * predicate.
     * @param outer Meta-predicate to perform
     * @param inner Predicate to perform meta-predicate on
     * @param args Substitution arguments to apply to inner predicate
     * @return True if meta-predicate succeeded, false otherwise.
     */
    protected boolean applyMetaPredicate(String outer, String inner, Object... args)
    {
        // Apply substitution arguments to inner predicate
        // before formatting meta-predicate
        Formatter formatter = new MQuery.Formatter();
        String formattedInner = formatter.format(inner, args);
        // Perform meta-predicate query on inner predicate
        return prove("@A(@A)", outer, formattedInner);
    }
}
