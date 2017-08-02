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

package io.mochalog.bridge.prolog.query.exception;

import io.mochalog.util.exception.UncheckedMochalogException;

/**
 * Exception indicating that a given query has no
 * further solutions
 */
public class EndOfQueryException extends UncheckedMochalogException
{
    /**
     * Constructor.
     */
    public EndOfQueryException()
    {
        super();
    }

    /**
     * Constructor.
     * @param message Exception message
     */
    public EndOfQueryException(String message)
    {
        super(message);
    }
}
