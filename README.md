# externaldeps

Maven project to **centralize and "Eclipsify"** plain java dependencies.

## Overview

As Preesm (and Graphiti and DFTools) is bundled as a set of Eclipse plug-ins, its dependencies should be Eclipse plug-ins (OSGi bundles)
hosted on an update site ([P2 repository](https://dzone.com/articles/understanding-eclipse-p2-provi)). However one may wish to use plain java libraries bundled as non OSGi jars, like slf4j, guava, jgrapht, etc. The Maven plug-in responsible for the Eclipse dependencies is Tycho. It does not handle non OSGi dependencies very well. Therefore the non OSGi jars have to be re-bundled as OSGi jars.

This simple maven configuration file does the job in 3 steps:
 - first it fetches plain java dependencies, specified in the `<artifacts>` configuration of the `p2-maven-plugin` Maven plug-in. Those dependencies are fetched from Maven repositories defined in the `<repository>` section at the beginning (the dependencies that are not available in Maven repositories are installed from local jars using the `maven-install-plugin`);
 - then `p2-maven-plugin` converts (or re-bundle) the jars as OSGi bundles (see https://github.com/reficio/p2-maven-plugin);
 - finally an update site (P2 repository) is generated with all the re-bundled jars and their transitive dependencies.

This project is distributed under the CeCILL-C license (see [LICENSE file](LICENSE)).

## Details

Everything is controlled from the [Project Object Model file](pom.xml) (Maven configuration). The following sections detail how to understand, tune, refactor, or extend this POM file,

### Getting Dependencies

This section describes how to declare a dependency to some Java library, either one from a Maven repository (preferred) or using a local Jar file (should be avoided if possible).

#### Declare a Dependency

The dependencies that should be re-bundled as OSGi jars are declared under the `<configuration>` section of the `org.reficio:p2-maven-plugin` Maven plug-in. Each dependency corresponds to one `<artifact>` entry, using the following format:

```xml
<artifact>
	<id>groupId:artifactId:version</id>
	<source>true</source>
</artifact>
```

The *groupId*, *artifactId* and *version* should be copied from a Maven repository. For instance, to add a dependency to jgrapht, one would first look for the available version on an artifact search engine (say https://mvnrepository.com/), find the last stable version (say https://mvnrepository.com/artifact/org.jgrapht/jgrapht-core/1.0.0), and copy the different values to a new `<artifact>` section in the POM file:

```xml
<artifact>
	<id>org.jgrapht:jgrapht-core:1.0.0</id>
	<source>true</source>
</artifact>
```

Note: The source option should be enabled always. The dependency resolver will not crash in case it does not find the source bundle.

#### Declare a new Maven Repository

The Maven artifact search engines results mostly points to artifacts hosted on the [Maven Central repository](http://repo1.maven.org/maven2/), which is included by default (see line 10 of [the default super POM](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html#Super_POM)). However, one may want to add dependencies to artifacts hosted on other repositories.

For instance, the version 0.8.2 of jgrapht is not available on Maven Central, but it is on the [Clojars repository](https://clojars.org/org.clojars.gilesc/jgrapht). To access to artifacts hosted on a repository different than Maven Central, simply add `<repository>` declarations in the `<repositories>` section of the POM file:

```xml
<repository>
	<id>clojars.org</id>
	<url>http://clojars.org/repo</url>
</repository>
```


#### Cannot find it on repositories? Convert a Jar

### Generate the P2 Repository

#### Convert to OSGi Bundle with `p2-maven-plugin`

#### Extension: Create a Feature

### Deploy

####