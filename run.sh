#!/bin/bash

REPODIR=${HOME}/.m2/repo-externaldeps/
mvn clean p2:site jetty:run -Dmaven.repo.local=${REPODIR}
