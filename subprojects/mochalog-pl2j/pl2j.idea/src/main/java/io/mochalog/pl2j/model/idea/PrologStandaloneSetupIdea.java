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

package io.mochalog.pl2j.model.idea;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.mochalog.pl2j.model.PrologRuntimeModule;
import io.mochalog.pl2j.model.PrologStandaloneSetupGenerated;
import org.eclipse.xtext.util.Modules2;

/**
 *
 */
public class PrologStandaloneSetupIdea extends PrologStandaloneSetupGenerated
{
    @Override
    public Injector createInjector()
    {
        PrologRuntimeModule runtimeModule = new PrologRuntimeModule();
        PrologIdeaModule ideaModule = new PrologIdeaModule();
        Module mergedModule = Modules2.mixin(runtimeModule, ideaModule);
        return Guice.createInjector(mergedModule);
    }
}
