 # Mochalog: A two-way bridge between Java and SWI-Prolog :coffee:

[![Travis-CI](https://img.shields.io/travis/mochalog/mochalog.svg)](https://travis-ci.org/ssardina/mochalog/builds)

**This is a FORK of the [original Mochalog](https://github.com/mochalog/mochalog) GitHub main repository.**

The current version (> 0.3.0) has been simplified significantly and migrated to Maven. 

Mochalog can be obtained automatically via Maven automatically from Github repository via JitPack: <https://jitpack.io/#ssardina/mochalog>. See below for more details for your POM.


## Overview

Mochalog is a rich bidirectional interface between the Java Runtime and the SWI-Prolog interpreter inspired and built on top of [JPL](http://jpl7.org/). 

In some sense, Mochalog is a further abstraction of JPL. Looking to stand on its own two feet, however, Mochalog is focused on achieving two core design objectives:

* **API simplicity:** Reduce code complexity and learning curve. Increase maintainability. Use modern Java and Prolog software design practices. Prolog calls to Java should look like ***Java***, Java calls to Prolog should look like ***Prolog***.
* **Performance:** Calls between the two languages should be blazingly fast (as fast or **faster** than other existing Java-Prolog interfaces).

Mochalog provides some high level API to consult, assert and retract, and a few query methods (just prove, one solution, all solutions, iterators). 

## How to use it

For various examples how to use it, see the [Mochalog Unit Test Examples](src/test/java/io/mochalog/bridge/MochaTest.java). Different tests show how to consult/load a KB, assert and retract, update facts (with new arguments), prove a query, ask for one solution, for all solutions, or iterate through solutions. 

Below we describe how to set Mochalog up.

### Prerequisites

* [SWI Prolog](http://www.swi-prolog.org/) (>7.4.x) with [JPL](http://www.swi-prolog.org/pldoc/doc_for?object=section(%27packages/jpl.html%27)) Bidirectional interface with Java:
	* This is package `swi-prolog-java` in Linux.
	* In Windows, the Java-SWI interface it can be installed as part of the main install.
	* Main Page for JPL: https://jpl7.org/ 

Also, depending on the system being used:

* If in **Windows**:
	* Tested successfully in Windows 7 with SWI 7.6.4.
	* Make sure SWI is installed with the JPL Java-SWI connectivity. You should have a `jpl.dll` (in the SWI `bin/` subdir) and a `jpl.jar` (in the SWI `lib/` subdir).
	* Define a _system_ environment variable `SWI_HOME_DIR` and set it to the root directory of your installed version of SWI-Prolog (e.g., to `C:\Program Files\swipl`).
	* Extend `Path` system environment variable with the following two components:
		* `%SWI_HOME_DIR%\bin`
		* `%SWI_HOME_DIR%\lib\jpl.jar`
	* No changes to `CLASSPATH` are needed.
* If in **Linux**:
	* Latest package versions at <http://www.swi-prolog.org/build/PPA.txt> 
	* JPL is provided via package `swi-prolog-java` (interface between Java and SWI) installed. This will include library `libjpl.so` (e.g., `/usr/lib/swi-prolog/lib/amd64/libjpl.so`)
	* Extend environment library `LD_PRELOAD` for system to pre-load `libswipl.so`: `export LD_PRELOAD=libswipl.so:$LD_PRELOAD`
		* Check [this post](https://answers.ros.org/question/132411/unable-to-load-existing-owl-in-semantic-map-editor/) and [this one](https://blog.cryptomilk.org/2014/07/21/what-is-preloading/) about library preloading.
		* Also, check [this](https://bugs.debian.org/cgi-bin/bugreport.cgi?bug=690734) and [this](https://github.com/yuce/pyswip/issues/10) posts.
	* Extend environment variable `LD_LIBRARY_PATH`  to point to the directory where `libjpl.so` is located (e.g., `export LD_LIBRARY_PATH=/usr/lib/swi-prolog/lib/amd64/`)
	* If using RUN AS configuration in ECLIPSE, remember to set up these two variables `LD_LIBRARY_PATH` and `LD_PRELOAD` too (and check "Append environment to native environment").


## Dependency in POM file

Mochalog can be configured automatically in your application as dependency via JitPack:

```xml
    <repositories>
        <!-- JitPack used for remote installation of dependencies from Github and Bitbucket -->
        <repository>
            <id>jitpack.io</id>
            <name>JitPack Repository</name>
            <url>https://jitpack.io</url>
        </repository>
   </repositories>

   <dependencies>
        <!--  Mochalog interface for SWI-Prolog -->
        <dependency>
            <groupId>com.github.ssardina</groupId>
            <artifactId>mochalog</artifactId>
            <version>0.4.1</version>
        </dependency>
        ...
   </dependencies>
```

### A Simple Example of Mochalog in SARL

Once again, a more complete set of examples in Java can be found in the [Mochalog Unit Test Examples](src/test/java/io/mochalog/bridge/MochaTest.java). It has tests showing how to consult/load a KB, assert and retract, update facts (with new arguments), prove a query, ask for one solution, for all solutions, or iterate through solutions. 

Here is some very simple example code of its use within a [SARL](http://www.sarl.io/) agent system:

```java
import io.mochalog.bridge.prolog.PrologContext
import io.mochalog.bridge.prolog.SandboxedPrologContext
import io.mochalog.bridge.prolog.query.Query

// Set-up Prolog knowledgebase
var prolog_kb : PrologContext
val beliefSpace = String.format("swiplayer")
prolog_kb = new SandboxedPrologContext(beliefSpace)
prolog_kb.importFile("src/main/prolog/masssim_coordinator.pl") // newest version

// Assert percepts in the KB
prolog_kb.assertFirst("percepts(@A, @I, @S)", agentName, agents.get(agentName).step, percepts.toString)

// Querying one solution - Tell the KB to process last percept
agents.keySet().forEach([ agentName : String |
	prolog_kb.askForSolution(Query.format("process_last_percepts(" + agentName + ")"))
])

// Querying all solutions - Report percepts available in the KB
val query = Query.format("percepts(Agent, Step, Percepts)")
for (solution : prolog_kb.askForAllSolutions(query))
{
	System.out.format("Information for agent %s on step %d\n", solution.get("Agent").toString(),  
                   solution.get("Step").intValue)
}
```        
        
                
## Contact

Sebastian Sardina - ssardina@gmail.com
