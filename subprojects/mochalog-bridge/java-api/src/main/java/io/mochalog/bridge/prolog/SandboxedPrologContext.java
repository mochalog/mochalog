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

package io.mochalog.bridge.prolog;

import io.mochalog.bridge.prolog.lang.Module;

import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.QuerySolutionList;

import io.mochalog.bridge.prolog.query.collectors.QuerySolutionCollector;
import io.mochalog.bridge.prolog.query.collectors.SequentialQuerySolutionCollector;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Interface to <i>sandboxed</i> SWI-Prolog interpreter context.
 * Context provides access controls via SWI-Prolog modules allowing
 * knowledge bases to be segregated in a concurrent environment
 */
public class SandboxedPrologContext extends AbstractPrologContext
{
    // Module from which queries will be scoped
    private final Module workingModule;

    /**
     * Constructor.
     * @param name Working module name
     */
    public SandboxedPrologContext(String name)
    {
        this(new Module(name));
    }

    /**
     * Constructor.
     * @param module Working module
     */
    public SandboxedPrologContext(Module module)
    {
        this.workingModule = module;
        prove("use_module('prolog/mochalog.pl')");
    }

    @Override
    public boolean importFile(Path path) throws IOException
    {
        // Ensure valid file path was supplied
        if (!Files.exists(path))
        {
            throw new IOException("Specified file path does not exist.");
        }

        try
        {
            // Convert to absolute path with forward-slash file
            // separators
            Path absolutePath = path.toAbsolutePath();
            String escapedPath = absolutePath.toString().replace('\\', '/');
            // Import file into the current module
            return prove("import_file(@S, @A)", escapedPath, workingModule.getName());
        }
        catch (IOError e)
        {
            throw new IOException("File path could not be converted " +
                "to absolute file path.");
        }
    }

    @Override
    public QuerySolutionList askForAllSolutions(Query query)
    {
        return new QuerySolutionList(query, workingModule);
    }

    @Override
    public QuerySolutionCollector ask(Query query)
    {
        SequentialQuerySolutionCollector.Builder builder =
            new SequentialQuerySolutionCollector.Builder(query);
        builder.setWorkingModule(workingModule);
        return builder.build();
    }

    /**
     * Get module queries are being scoped from
     * in current Prolog context
     * @return Working module
     */
    public Module getWorkingModule()
    {
        return workingModule;
    }

    @Override
    public String toString()
    {
        return workingModule.getName();
    }
}
