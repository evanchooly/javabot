#! /bin/sh

BOTPID=`cat bot.pid`

echo $BOTPID
ps w | grep $BOTPID | grep javabot | grep -v grep
if [ "`ps w | grep $BOTPID | grep javabot | grep -v grep`" ] 
then
	kill $BOTPID
	rm bot.pid
fi

for i in lib/*.jar
do
	export CLASSPATH=$CLASSPATH:$i
done
export CLASSPATH=$CLASSPATH:build
echo $CLASSPATH

java javabot.Javabot &> javabot.log &
echo $! > bot.pid
#tail -f javabot.log
