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

package io.mochalog.bridge.prolog.namespace;

import io.mochalog.util.exception.UncheckedMochalogException;

/**
 * Exception indicating no variable with the given name
 * was found in a given namespace.
 */
public class NoSuchVariableException extends UncheckedMochalogException
{
    /**
     * Constructor. No custom message provided.
     * @param name Variable name
     */
    public NoSuchVariableException(String name)
    {
        this(name, "");
    }

    /**
     * Constructor. Resulting exception text will include
     * the variable name that was not found and a custom message.
     * @param name Variable name
     * @param message Custom messsage
     */
    public NoSuchVariableException(String name, String message)
    {
        super(getExceptionMessage(name, message));
    }

    /**
     * Construct the exception message from a given variable
     * name and a custom message.
     * @param name Variable name
     * @param message Custom message
     * @return Exception message
     */
    private static String getExceptionMessage(String name, String message)
    {
        String exceptionMessage = "Variable " + name + " undefined.";
        if (message != null && !message.equals(""))
        {
            exceptionMessage += " " + message;
        }

        return exceptionMessage;
    }
}
