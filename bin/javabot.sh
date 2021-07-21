#! /bin/sh

git pull --rebase
mvn package -DskipTests
java -cp .:target/javabot.jar javabot.Javabot
