#!/bin/bash

TMPDIR=`mktemp -d`

mvn clean p2:site jetty:run -Dmaven.repo.local=$TMPDIR

rm -rf $TMPDIR
