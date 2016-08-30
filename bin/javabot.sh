#! /bin/sh

mvn package -DskipTests
java -jar target/javabot.jar