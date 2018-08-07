 # Mochalog :coffee:

[![Travis-CI](https://img.shields.io/travis/mochalog/mochalog.svg)](https://travis-ci.org/mochalog/mochalog/builds)

**This is a CLONE of the GitHub main repository [here](https://github.com/mochalog/mochalog). It has a few additions/fixes but nothing important.**

This version is based on [Gradle](https://gradle.org/) build tool and comes directly from Matt McNally's Honours work in 2017. Future versions will be simplified and based on Maven build tool.

### A two-way bridge between Java and SWI-Prolog

Mochalog is a rich bidirectional interface between the Java Runtime and the SWI-Prolog interpreter inspired by [JPL](http://jpl7.org/). Looking to stand on its own two feet, however, Mochalog is focused on achieving two core design objectives:

* **API simplicity:** Reduce code complexity and learning curve. Increase maintainability. Use modern Java and Prolog software design practices. Prolog calls to Java should look like ***Java***, Java calls to Prolog should look like ***Prolog***.
* **Performance:** Calls between the two languages should be blazingly fast (as fast or **faster** than other existing Java-Prolog interfaces).

### Use it as dependency in Maven

To use Mochalog in your application, add this dependecy and repository to your `pom.xml` to get it automatically via JitPack and GitHub:

        <dependency>
            <groupId>com.github.ssardina</groupId>
            <artifactId>mochalog</artifactId>
            <version>${mochalog.version}</version>
        </dependency>
        
        
        <!-- JitPack used for remote installation of dependencies from Github and Bitbucket -->
        <repository>
            <id>jitpack.io</id>
            <name>JitPack Repository</name>
            <url>https://jitpack.io</url>
        </repository>
        
        
