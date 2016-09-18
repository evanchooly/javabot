#! /bin/sh

mvn package -DskipTests
java -cp .:target/javabot.jar javabot.Javabot
