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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class AbstractFormatter<T> implements Formatter<T>
{
    private final Pattern RULE_PATTERN;

    /**
     * Constructor.
     */
    public AbstractFormatter()
    {
        // Precompile regex pattern (faster performance)
        RULE_PATTERN = Pattern.compile("\\$\\w+");
    }

    public String formatInput(String str, Object... args)
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

            // Format the specified rule and perform
            // validation
            String replacement = formatRule(rule, arg);
            if (replacement == null)
            {
                return null;
            }

            // Replace matched rule with resulting
            // string
            matcher.appendReplacement(formatBuffer, replacement);
        }

        matcher.appendTail(formatBuffer);
        return formatBuffer.toString();
    }

    /**
     * Format a given rule with the specified object argument
     * @param rule Substitution rule
     * @param arg Object argument
     * @return Replacement string
     */
    protected abstract String formatRule(String rule, Object arg);
}
