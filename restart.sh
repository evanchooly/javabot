#! /bin/sh

BOTPID=`cat killbot | cut -d" " -f2`

echo $BOTPID
ps w | grep $BOTPID | grep javabot | grep -v grep
if [ "`ps w | grep $BOTPID | grep javabot | grep -v grep`" ] 
then
	echo Old instance running
	./killbot
	rm killbot
fi

for i in lib/*.jar
do
	export CLASSPATH=$CLASSPATH:$i
done
export CLASSPATH=$CLASSPATH:build
echo $CLASSPATH

java javabot.Javabot &> javabot.log &
echo "kill $!" > killbot
chmod +x killbot
