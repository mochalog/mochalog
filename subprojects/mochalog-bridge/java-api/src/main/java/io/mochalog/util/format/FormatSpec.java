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

package io.mochalog.util.format;

import java.util.HashMap;
import java.util.Map;

import java.util.IllegalFormatException;
import java.util.UnknownFormatConversionException;

/**
 * Specification of bindings between string identifiers
 * and formatting rules for specific types of object arguments
 */
public class FormatSpec
{
    // Mapping between identifiers and formatting methods
    private Map<String, FormattingRule> formattingRules;

    /**
     * Constructor.
     */
    public FormatSpec()
    {
        formattingRules = new HashMap<>();
    }

    /**
     * Specify a rule to associate a string identifier with
     * a corresponding handling method
     * @param identifier Symbolic identifier following '$'
     * @param rule Function to delegate to
     */
    public void setRule(String identifier, FormattingRule rule)
    {
        formattingRules.put(identifier, rule);
    }

    /**
     * Apply formatting rule corresponding to given identifier
     * @param identifier Symbolic identifier following '$'
     * @param arg Object argument
     * @return Formatted string
     * @throws IllegalFormatException Unable to format given object argument
     * according to specified rule
     */
    public String applyRule(String identifier, Object arg) throws IllegalFormatException
    {
        FormattingRule rule = formattingRules.get(identifier);
        if (rule == null)
        {
            throw new UnknownFormatConversionException("Formatting rule not available corresponding " +
                "to given identifier " + identifier);
        }

        return rule.apply(identifier, arg);
    }
}
