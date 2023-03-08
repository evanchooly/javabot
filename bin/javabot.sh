#! /bin/sh

git pull --rebase
mvn generate-resources
mvn package -DskipTests
java -cp .:target/javabot.jar javabot.Javabot
