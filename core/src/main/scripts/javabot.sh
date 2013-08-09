#! /bin/sh

if [ ! -f javabot.properties ]
then
    echo Please create a javabot.properties file to configure your bot.
else
    java -jar javabot.jar
fi
