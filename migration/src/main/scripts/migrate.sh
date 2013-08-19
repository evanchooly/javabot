#! /bin/sh

PROPS=$1
if [ -z "$PROPS" ]
then
    PROPS=javabot.properties
fi

if [ ! -f "$PROPS" ]
then
    echo Please create a javabot.properties file in the current directory or provide a path to one.
else
    java -jar `dirname $0`/javabot-migration.jar $1
fi
