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

package io.mochalog.bridge.prolog.lang;

import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.collectors.QuerySolutionCollector;
import io.mochalog.bridge.prolog.query.collectors.SequentialQuerySolutionCollector;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import java.io.IOError;
import java.io.IOException;

/**
 * Representation of a SWI-Prolog module (predicate namespace)
 */
public class Module
{
    // Identifier for the module
    private final String name;

    /**
     * Constructor.
     * @param name Module name
     */
    public Module(String name)
    {
        this.name = name;
    }

    /**
     * Get the name associated with the given module
     * @return Module name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get string format of module
     * @return String format
     */
    @Override
    public String toString()
    {
        return name;
    }

    /**
     * Load a given file into the current module
     * @param filepath Path to Prolog source file
     * @return True if file loading was successful, false otherwise.
     * @throws IOException File IO error occurred
     */
    public boolean importFile(Path filepath) throws IOException
    {
        // Ensure valid file path was supplied
        if (!Files.exists(filepath))
        {
            throw new IOException("Specified file path does not exist.");
        }

        try
        {
            // Formulate query to load Prolog files into
            // a given module
            String absolutePath = filepath.toAbsolutePath().toString();
            // Escape \ characters in the given file path to
            // prevent SWI-Prolog escaping subsequent characters
            // TODO: Provide facility to enable this behaviour through
            // QueryFormatter
            String escapedAbsolutePath = absolutePath.replace("\\", "\\\\");
            Query query = Query.format("load_files(@S, [module(@S)])",
                escapedAbsolutePath, this);

            SequentialQuerySolutionCollector.Builder builder =
                new SequentialQuerySolutionCollector.Builder(query);
            QuerySolutionCollector collector = builder.build();
            // Ensure files were successfully loaded
            return collector.hasSolutions();
        }
        catch (IOError e)
        {
            throw new IOException("File path could not be converted " +
                "to absolute file path.");
        }
    }

    @Override
    public final boolean equals(Object o)
    {
        // Early termination for self-identity
        if (this == o)
        {
            return true;
        }

        // null/type validation
        if (o != null && o instanceof Module)
        {
            Module module = (Module) o;
            // Field comparisons
            return Objects.equals(name, module.name);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(name);
    }
}
