#!/bin/sh
mvn install:install-file -Dfile=openlaszlo.xml -DgroupId=org.openlaszlo -DartifactId=openlaszlo -Dversion=4.0.2 -Dpackaging=pom
mvn install:install-file -Dfile=lib/jweather-0.2.5.jar -DgroupId=net.sf.jweather -DartifactId=jweather -Dversion=0.2.5 -Dpackaging=jar
mvn install:install-file -Dfile=lib/wicket-contrib-datepicker-1.3-SNAPSHOT.jar -DgroupId=org.apache.wicket -DartifactId=wicket-contrib-datepicker -Dversion=1.3.0-SNAPSHOT -Dpackaging=jar

