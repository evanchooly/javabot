#!/bin/sh
mvn install:install-file -Dfile=openlaszlo.xml -DgroupId=org.openlaszlo -DartifactId=openlaszlo -Dversion=4.0.2 -Dpackaging=pom
mvn install:install-file -Dfile=lib/rickyclarkson.jar -DgroupId=rickyclarkson -DartifactId=rickyclarkson -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jweather-0.2.5.jar -DgroupId=net.sf.jweather -DartifactId=jweather -Dversion=0.2.5 -Dpackaging=jar
mvn install:install-file -Dfile=lib/openlaszlo-4.0.2-servlet.war -DgroupId=org.openlaszlo -DartifactId=openlaszlo -Dversion=4.0.2 -Dpackaging=war
mvn install:install-file -Dfile=lib/jdom.jar -DgroupId=org.openlaszlo -DartifactId=openlaszlo-jdom -Dversion=4.0.2 -Dpackaging=jar
mvn install:install-file -Dfile=lib/saxon-6.5.3-lz-p1.jar -DgroupId=org.openlaszlo -DartifactId=openlaszlo-saxon -Dversion=4.0.2 -Dpackaging=jar
mvn install:install-file -Dfile=lib/swflib.jar -DgroupId=org.openlaszlo -DartifactId=openlaszlo-swflib -Dversion=4.0.2 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jing.jar -DgroupId=org.openlaszlo -DartifactId=openlaszlo-thaiopensource-jing -Dversion=4.0.2 -Dpackaging=jar
mvn install:install-file -Dfile=lib/lps-4.0.2.jar -DgroupId=org.openlaszlo -DartifactId=openlaszlo-lps -Dversion=4.0.2 -Dpackaging=jar
mvn install:install-file -Dfile=lib/activation.jar -DgroupId=javax.activation -DartifactId=activation -Dversion=1.0.2 -Dpackaging=jar


