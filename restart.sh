#! /bin/sh

ant rebuild
if [ -f killbot ]
then
	BOTPID=`cat killbot | cut -d" " -f2`

	if [ "`ps auxw | grep $BOTPID | grep javabot | grep -v grep`" ] 
	then
		echo Old instance running
		./killbot
	fi
fi

for i in lib/*.jar
do
	export CLASSPATH=$CLASSPATH:$i
done
export CLASSPATH=$CLASSPATH:build:$JAVA_HOME/lib/tools.jar

java javabot.Javabot &> javabot.log &
PID=$!
echo "kill ${PID} ; rm \$0" > killbot
chmod +x killbot
