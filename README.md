 # Mochalog: A two-way bridge between Java and SWI-Prolog :coffee:

[![Travis-CI](https://img.shields.io/travis/mochalog/mochalog.svg)](https://travis-ci.org/ssardina/mochalog/builds)

**This is a FORK of the [original Mochalog](https://github.com/mochalog/mochalog) GitHub main repository.**

The current version (> 0.3.0) has been simplified significantly and migrated to Maven. 

## Overview

Mochalog is a rich bidirectional interface between the Java Runtime and the SWI-Prolog interpreter inspired and built on top of [JPL](http://jpl7.org/). 

In some sense, Mochalog is a further abstraction of JPL. Looking to stand on its own two feet, however, Mochalog is focused on achieving two core design objectives:

* **API simplicity:** Reduce code complexity and learning curve. Increase maintainability. Use modern Java and Prolog software design practices. Prolog calls to Java should look like ***Java***, Java calls to Prolog should look like ***Prolog***.
* **Performance:** Calls between the two languages should be blazingly fast (as fast or **faster** than other existing Java-Prolog interfaces).


Mochalog provides some high level API to consult, assert and retract, and a few query methods (just prove, one solution, all solutions, iterators). 

## How to use it

Check the [Wiki](https://github.com/ssardina/mochalog/wiki) for general installation instructions.

For various examples how to use it, see the [Mochalog Unit Test Examples](src/test/java/io/mochalog/bridge/MochaTest.java) 
        
        
                
## Contact

Sebastian Sardina - ssardina@gmail.com
