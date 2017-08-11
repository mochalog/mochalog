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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract implementation of string formatter interface
 */
public abstract class AbstractFormatter implements Formatter
{
    // Regex pattern to apply for substitution rule identifiers
    private final Pattern RULE_PATTERN;
    // Formatting rule specification
    private final FormatSpec FORMAT_SPEC;

    /**
     * Constructor.
     */
    protected AbstractFormatter()
    {
        // Precompile regex pattern (faster performance)
        RULE_PATTERN = Pattern.compile("@\\w+");
        FORMAT_SPEC = new FormatSpec();
    }

    @Override
    public String format(String str, Object... args) throws IllegalFormatException
    {
        final int RULE_BEGIN_INDEX = 1;

        // Apply rule pattern to input string to
        // find inline matches
        Matcher matcher = RULE_PATTERN.matcher(str);
        // Formatted string buffer
        StringBuffer formatBuffer = new StringBuffer();

        // Find all inline rules within the input string
        for (int i = 0; matcher.find() && i < args.length; ++i)
        {
            // Remove $ from the matched rule string
            String rule = matcher.group().substring(RULE_BEGIN_INDEX);
            Object arg = args[i];

            // Format the specified rule according to given specifications
            String replacement = FORMAT_SPEC.applyRule(rule, arg);
            // Ensure replacement string is converted into literal
            // string (ensure \ and $ characters are treated correctly)
            matcher.appendReplacement(formatBuffer, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(formatBuffer);
        return formatBuffer.toString();
    }

    @Override
    public void setRule(String identifier, FormattingRule rule)
    {
        FORMAT_SPEC.setRule(identifier, rule);
    }
}
