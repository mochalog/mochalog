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

package io.mochalog.util.io;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility functions for accessing validated file paths from various
 * resource description formats.
 */
public class PathUtils
{
    /**
     * Convert path string to validated file path string.
     * @param path File path
     * @return Validated path string
     * @throws IOException IO error occurred.
     */
    public static String getResolvableFilePath(String path) throws IOException
    {
        return getResolvableFilePath(Paths.get(path));
    }

    /**
     * Convert unvalidated path object to fully validated
     * path string.
     * @param path Unvalidated file path
     * @return Validated path string
     * @throws IOException IO error occurred.
     */
    public static String getResolvableFilePath(Path path) throws IOException
    {
        // Ensure valid file path was supplied
        if (!Files.exists(path))
        {
            throw new IOException("Specified file path could not be resolved");
        }

        try
        {
            // Convert to absolute path with forward-slash file
            // separators
            Path absolutePath = path.toAbsolutePath();
            return absolutePath.toString().replace('\\', '/');
        }
        catch (IOError e)
        {
            throw new IOException("File path could not be converted " +
                "to absolute file path.");
        }
    }
}
