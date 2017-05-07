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

import org.gradle.api.Plugin
import org.gradle.api.Project

import org.xtext.gradle.idea.tasks.DownloadIdea

/*
 * The following code is sourced and modified from code by Hubert A. Klein Ikkink available at
 * http://mrhaki.blogspot.com.au/2016/03/gradle-goodness-adding-custom-extension.html
 * Copyright 2008-2017 Hubert A. Klein Ikkink
 * The original code is licensed under the terms of the Creative Commons Attribution 4.0
 * International Public License. A copy of the full text of this license can be found
 * at 'thirdparty/snippets/CCBY4_LICENSE' (available on Github at
 * https://github.com/mochalog/mochalog/tree/master/thirdparty/snippets/CCBY4_LICENSE).
 */

/**
 * Allows progress to be repeatedly logged during operation
 * of Xtext DownloadIdea task
 * <p>
 * Necessary to ensure Travis-CI builds do not timeout
 * (builds timeout after 10 minutes of log inactivity).
 * See https://docs.travis-ci.com/user/customizing-the-build#Build-Timeouts
 */
class DownloadIdeaLoggerPlugin implements Plugin<Project> {

    /**
     * Apply the scheduled logging extension to the DownloadIdea task
     * @param project Gradle project
     */
    @Override
    void apply(final Project project) {
        // Apply the extension to all DownloadIdea tasks
        project.tasks.withType(DownloadIdea) { task ->
            // Add a customisable scheduled logger to the task
            // with the extension name 'logEvery'
            ScheduledLogger scheduledLogger =
                task.extensions.create('logEvery', ScheduledLogger)

            task.doFirst {
                // Start the logging service
                println("Downloading Intellij IDEA ${task.ideaVersion} to ${task.ideaHome}")
                scheduledLogger.start()
            }

            task.doLast {
                // Stop the logging service
                scheduledLogger.stop()
                println("Intellij IDEA ${task.ideaVersion} downloaded to ${task.ideaHome}")
            }
        }
    }
}
