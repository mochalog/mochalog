/**
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

:- module(mochalog,
    [
        import_file/2,
        import_file/3
    ]).

/**
 * import_file(+File:string, +Module:atom) is semidet.

 * Import an instance of File into a given Module.
 * Same as mochalog:import_file/3 with empty Options list.
 */
import_file(File, Module) :- import_file(File, Module, []).

/**
 * import_file(+File:string, +Module:atom, +Options:list) is semidet.
 *
 * Import an instance of File into a given Module.
 * Surpasses the requirement that each Prolog source file should
 * only ever be associated with a single module. Options allows
 * for the specification of file read options.
 */
import_file(File, Module, Options) :-
    %! Create a unique file identifier from the module
    %  and file path
    atomic_list_concat([ Module, '_', File, '_import' ], FileID),
    open(File, read, FileStream),
    %! Load file into module from file stream
    Module:load_files(FileID,[ stream(FileStream) | Options ]),
    %! Ensure file stream is closed after use
    close(FileStream).

mlg_dump_kb(Name) :-
	(agentName(Name) -> true ; Name = default),
	(step(Step) ; Step = "none"), !,
	strings_concat(["kb-", Name, "-", Step, ".pl"], FileName),
	open(FileName, write, F),
	set_output(F),
	listing,
	close(F).