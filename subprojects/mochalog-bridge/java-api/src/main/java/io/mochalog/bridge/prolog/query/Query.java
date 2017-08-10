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

package io.mochalog.bridge.prolog.query;

import io.mochalog.bridge.prolog.lang.Module;

import io.mochalog.util.format.AbstractFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents query provided to the SWI-Prolog
 * interpreter, managing localised query namespace.
 */
public class Query implements Iterable<QuerySolution>
{
    /**
     * Formatter of Prolog query strings using substitution rules
     * and domain-specific syntax
     */
    public static class Formatter extends AbstractFormatter
    {
        // Pattern corresponding to a Prolog compound structure
        // e.g. atom(arg1, arg2, ...)
        private final Pattern COMPOUND_PATTERN;
        // Syntactical representation of replacement of an
        // existing term for a new given term
        private final Pattern TERM_SETTER_PATTERN;

        /**
         * Constructor.
         */
        public Formatter()
        {
            super();

            // Prolog atom value
            setRule("A", String::valueOf);
            // Prolog strings are wrapped in quote characters
            setRule("S", o -> "\"" + String.valueOf(o) + "\"");

            // Basic regex pattern defining a Prolog compound
            COMPOUND_PATTERN = Pattern.compile("\\w+\\([^(]*\\)");

            // Basic regex capturing most Prolog term instances
            // (integers, floats, strings, atoms)
            // TODO: Not a robust grammar definition of a Prolog term
            final String TERM_DEFINITION = "\\w+|[0-9]+.[0-9]|\"[^\"]*\"";
            // Rule of the form <PREVIOUS_VALUE> <- <NEW_VALUE>
            // Allows Prolog term data (PREVIOUS_VALUE) to be replaced with NEW_VALUE within
            // a query
            TERM_SETTER_PATTERN = Pattern.compile(
                String.format("(%1$s)\\s*<-\\s*(%1$s)", TERM_DEFINITION)
            );
        }

        @Override
        public String format(String str, Object... args)
        {
            String formattedStr = super.format(str, args);

            StringBuffer queryBuffer = new StringBuffer();
            Matcher compoundMatcher = COMPOUND_PATTERN.matcher(formattedStr);

            // Query compound terms placed at end of resultant query
            // to facilitate replacement of specified values
            StringBuilder setterCompounds = new StringBuilder();

            // Find instances of compounds in the query string
            while (compoundMatcher.find())
            {
                String compound = compoundMatcher.group();
                // Run a regex query over the query compound, looking
                // for instances of setter syntax
                Matcher setterMatcher = TERM_SETTER_PATTERN.matcher(compound);

                // Check for instances of setter syntax
                if (setterMatcher.find())
                {
                    // Existing version of the compound term will be solely
                    // constructed of 'previousValue' terms
                    String previousCompound = setterMatcher.replaceAll("$1");
                    // New version will be solely constructed of 'newValue' terms
                    String newCompound = setterMatcher.replaceAll("$2");

                    compoundMatcher.appendReplacement(queryBuffer,
                        Matcher.quoteReplacement(previousCompound));

                    // Add the necessary setter constructs to the end of the query
                    setterCompounds.append(", retract(").append(previousCompound).append(")");
                    setterCompounds.append(", assertz(").append(newCompound).append(")");
                }
            }

            compoundMatcher.appendTail(queryBuffer);
            return queryBuffer.toString() + setterCompounds.toString();
        }
    }

    // String form of Prolog query
    private String text;

    /**
     * Constructor.
     * @param text Query string
     */
    public Query(String text)
    {
        this.text = text;
    }

    /**
     * Convert the query to string format
     * @return String format
     */
    @Override
    public String toString()
    {
        return text;
    }

    /**
     * Iterate over available solutions for the query
     * @return Iterator over query solutions
     */
    @Override
    public Iterator<QuerySolution> iterator()
    {
        return new QuerySolutionIterator(this);
    }

    /**
     * Formulate a query based on a format string
     * and substitution arguments
     * @param query Formatted query string
     * @param args Query arguments
     * @return Query object
     */
    public static Query format(String query, Object... args)
    {
        Formatter formatter = new Formatter();
        String formattedQuery = formatter.format(query, args);
        return new Query(formattedQuery);
    }

    public static String runnableInModule(Query query, Module module)
    {
        return module == null ?
            query.toString() :
            String.format("%s:(%s)", module.getName(), query.toString());
    }
}
