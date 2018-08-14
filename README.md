 # Mochalog: A two-way bridge between Java and SWI-Prolog :coffee:

[![Travis-CI](https://img.shields.io/travis/mochalog/mochalog.svg)](https://travis-ci.org/ssardina/mochalog/builds)

**This is a FORK of the [original Mochalog](https://github.com/mochalog/mochalog) GitHub main repository.**

The current version (> 0.3.0) has been simplified significantly and migrated to Maven. 

## Overview

Mochalog is a rich bidirectional interface between the Java Runtime and the SWI-Prolog interpreter inspired and built on top of [JPL](http://jpl7.org/). 

In some sense, Mochalog is a further abstraction of JPL. Looking to stand on its own two feet, however, Mochalog is focused on achieving two core design objectives:

* **API simplicity:** Reduce code complexity and learning curve. Increase maintainability. Use modern Java and Prolog software design practices. Prolog calls to Java should look like ***Java***, Java calls to Prolog should look like ***Prolog***.
* **Performance:** Calls between the two languages should be blazingly fast (as fast or **faster** than other existing Java-Prolog interfaces).


## How to use it

### Use it as dependency in Maven

To use Mochalog in your application, add this dependecy and repository to your `pom.xml` to get it automatically via JitPack and GitHub.

To have access to [JitPack](https://jitpack.io/#ssardina/mochalog) service, add this repository:

        <!-- JitPack used for remote installation of dependencies from Github and Bitbucket -->
        <repository>
            <id>jitpack.io</id>
            <name>JitPack Repository</name>
            <url>https://jitpack.io</url>
        </repository>



#### Version 0.4.x

This version has further simplified the framework by removing modules and having a single flat system (and JAR file `mochalog.jar`).

        <dependency>
            <groupId>com.github.ssardina</groupId>
            <artifactId>mochalog</artifactId>
            <version>0.4.0</version>
        </dependency>
        
               

#### Version 0.3.x

This version has migrated Gradel to Maven and kept the main core, and dropped the ECLIPSE and IDEA plugins. However, it is still based on two modules and one `io.mochalog.all` aggregate module.

        <dependency>
            <groupId>com.github.ssardina.mochalog</groupId>
            <artifactId>io.mochalog.all</artifactId>
            <version>0.3.0</version>
        </dependency>
        
        
        
                
## Contact

Sebastian Sardina - ssardina@gmail.com
