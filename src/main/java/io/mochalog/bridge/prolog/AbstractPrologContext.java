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
        return prove(Query.format(text, args));
    }

    @Override
    public boolean prove(Query query)
    {
        QuerySolutionCollector collector = ask(query);
        boolean result = collector.hasSolutions();
        // Ensure temporary collector closed
        collector.detach();
        return result;
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
        QuerySolutionCollector collector = ask(query);
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
    public QuerySolution askForSolution(Query query, int index)
            throws NoSuchSolutionException
    {
        QuerySolutionCollector collector = ask(query);
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
    public QuerySolutionList askForAllSolutions(String text, Object... args)
    {
        return askForAllSolutions(Query.format(text, args));
    }

    @Override
    public QuerySolutionCollector ask(String text, Object... args)
    {
        return ask(Query.format(text, args));
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
        Formatter formatter = new Query.Formatter();
        String formattedInner = formatter.format(inner, args);
        // Perform meta-predicate query on inner predicate
        return prove("@A(@A)", outer, formattedInner);
    }
}
