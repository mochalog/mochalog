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

import io.mochalog.bridge.prolog.api.PackLoader;
import io.mochalog.bridge.prolog.lang.Module;

import io.mochalog.bridge.prolog.query.Query;
import io.mochalog.bridge.prolog.query.QuerySolutionList;

import io.mochalog.bridge.prolog.query.collectors.QuerySolutionCollector;
import io.mochalog.bridge.prolog.query.collectors.SequentialQuerySolutionCollector;
import io.mochalog.util.io.PathUtils;

import java.io.IOError;
import java.io.IOException;
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
    public SandboxedPrologContext(String name) throws IOError
    {
        this(new Module(name));
    }

    /**
     * Constructor.
     * @param module Working module
     */
    public SandboxedPrologContext(Module module) throws IOError
    {
        this.workingModule = module;

        // Load the Mochalog Prolog bridge API into the given
        // Prolog context
        try
        {
            // Ignore output from pack_install routine
            // TODO: Remove need to force re-upgrade with upgrade(true) (currently in place due to error logging)
            // pack_install comes from library(prolog_pack): http://www.swi-prolog.org/pldoc/man?section=prologpack
            prove("pack_install('@A', [interactive(false), silent(true), upgrade(true)])",
                PackLoader.getPackResource("mochalog"));

            // Tell the prolog engine to use the mochalog library which was just installed
            prove("use_module(library(mochalog))");
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
    }

    @Override
    public boolean importFile(String path) throws IOException
    {
        String resolvablePath = PathUtils.getResolvableFilePath(path);
        return prove("import_file(@S, @A)", resolvablePath, workingModule.getName());
    }

    @Override
    public boolean importFile(Path path) throws IOException
    {
        String resolvablePath = PathUtils.getResolvableFilePath(path);
        return prove("import_file(@S, @A)", resolvablePath, workingModule.getName());
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
