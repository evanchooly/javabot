#!/bin/sh
mvn install:install-file -Dfile=lib/rickyclarkson.jar -DgroupId=rickyclarkson -DartifactId=rickyclarkson -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jweather-0.2.5.jar -DgroupId=net.sf.jweather -DartifactId=jweather -Dversion=0.2.5 -Dpackaging=jar

