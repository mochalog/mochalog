 # Mochalog: A two-way bridge between Java and SWI-Prolog :coffee:

[![Travis-CI](https://img.shields.io/travis/mochalog/mochalog.svg)](https://travis-ci.org/mochalog/mochalog/builds)

**This is a FORK of the [original Mochalog](https://github.com/mochalog/mochalog) GitHub main repository.**

The current version (> 0.3.0) has been simplified (code for ECLIPSE and IDEA removed) and migrated to Maven (instead of Gradel). 

## Overview

Mochalog is a rich bidirectional interface between the Java Runtime and the SWI-Prolog interpreter inspired and built on top of [JPL](http://jpl7.org/). 

In some sense, Mochalog is a further abstraction of JPL. Looking to stand on its own two feet, however, Mochalog is focused on achieving two core design objectives:

* **API simplicity:** Reduce code complexity and learning curve. Increase maintainability. Use modern Java and Prolog software design practices. Prolog calls to Java should look like ***Java***, Java calls to Prolog should look like ***Prolog***.
* **Performance:** Calls between the two languages should be blazingly fast (as fast or **faster** than other existing Java-Prolog interfaces).


## How to use it

### Use it as dependency in Maven

To use Mochalog in your application, add this dependecy and repository to your `pom.xml` to get it automatically via JitPack and GitHub:

        <dependency>
            <groupId>com.github.ssardina.mochalog</groupId>
            <artifactId>mochalog.all</artifactId>
            <version>0.3.0</version>
        </dependency>
        
        
        <!-- JitPack used for remote installation of dependencies from Github and Bitbucket -->
        <repository>
            <id>jitpack.io</id>
            <name>JitPack Repository</name>
            <url>https://jitpack.io</url>
        </repository>
        
        
        
## Contact

Sebastian Sardina - ssardina@gmail.com
