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

import java.util.IllegalFormatException;

/**
 * Formatter of strings using substitution rules
 */
public interface Formatter
{
    /**
     * Format a string into given object type based on
     * specified substitution rules
     * @param str String input
     * @param args Arguments to substitute
     * @return Formatted string
     * @throws IllegalFormatException Unable to format the given
     * input string using the provided substitution arguments
     */
    String format(String str, Object... args) throws IllegalFormatException;

    /**
     * Define the rule to apply when a given rule
     * identifier is matched during in-progress string
     * formatting
     * @param identifier Rule identifier to match
     * @param rule Rule to apply
     */
    void setRule(String identifier, FormattingRule rule);
}
