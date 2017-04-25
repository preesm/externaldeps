# externaldeps

Maven project to **centralize and "Eclipsify"** plain java dependencies.

## Overview

As Preesm (and Graphiti and DFTools) is bundled as a set of Eclipse plugins, its dependencies should be Eclipse plugins (OSGi bundles)
hosted on an update site ([P2 repository](https://dzone.com/articles/understanding-eclipse-p2-provi)). However one may wish to use plain java libraries bundled as non OSGi jars, like slf4j, guava, jgrapht, etc. The Maven plugin responsible for the Eclipse dependencies is Tycho. It does not handle non OSGi dependencies very well. Therefore the non OSGi jars have to be re-bundled as OSGi jars.

This simple maven configuration file does the job in 3 steps:
 - first it fetches plain java dependencies, specified in the `<artifacts>` configuration of the `p2-maven-plugin` Maven plugin. Those dependencies are fetched from Maven repositories defined in the `<repository>` section at the begining (the dependencies that are not available in Maven repositories are installed from local jars using the maven-install-plugin);
 - then `p2-maven-plugin` converts (or re-bundle) the jars as OSGi bundles (see https://github.com/reficio/p2-maven-plugin);
 - finaly an update site (P2 repository) is generated with all the re-bundled jars and their transitive dependencies.

This project is distributed under the CeCILL-C license (see [LICENSE file](LICENSE)).

## Details

