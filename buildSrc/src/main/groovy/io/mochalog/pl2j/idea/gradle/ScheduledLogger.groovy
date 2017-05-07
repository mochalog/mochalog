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

package io.mochalog.pl2j.idea.gradle

/*
 * The following code is sourced and modified from code by Hubert A. Klein Ikkink available at
 * http://mrhaki.blogspot.com.au/2009/11/groovy-goodness-run-code-at-specified.html
 * Copyright 2008-2017 Hubert A. Klein Ikkink
 * The original code is licensed under the terms of the Creative Commons Attribution 4.0
 * International Public License. A copy of the full text of this license can be found
 * at 'thirdparty/snippets/CCBY4_LICENSE' (available on Github at
 * https://github.com/mochalog/mochalog/tree/master/thirdparty/snippets/CCBY4_LICENSE).
 */

/**
 * Logging utility class which allows for the repeated
 * logging of a specified message over a certain interval
 */
class ScheduledLogger {
    // Delay to apply at initialisation of logger
    long initialDelay = 0
    // Delay to apply between each individual logging cycle
    long interval = 100
    // Message to print to log
    String message = ""

    // Timer and task to repeatedly invoke
    private Timer scheduler
    private ScheduledLoggingTask loggingTask

    /**
     * Constructor.
     */
    ScheduledLogger() {
        scheduler = new Timer()
    }

    /**
     * Start the logging cycle given the parameters
     * provided to the class
     */
    def start() {
        loggingTask = new ScheduledLoggingTask(message: message)
        // Start a new scheduled logging cycle
        scheduler.schedule(loggingTask, initialDelay, interval)
    }

    /**
     * Stop the logging cycle
     */
    def stop() {
        loggingTask.cancel()
        // Ensure next logs are started on next line
        println()
    }
}

/**
 * Task which prints a message to
 * standard output
 */
class ScheduledLoggingTask extends TimerTask {
    private String message
    @Override
    void run() {
        // Print on same line and flush to refresh
        // output buffer
        print(message)
        System.out.flush()
    }
}