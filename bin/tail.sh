#! /bin/sh

rm -f /tmp/javabot.log
while [ ! -f /tmp/javabot.log ]
do
	sleep 1
done
echo Found file.  Tailing.
tail -F /tmp/javabot.log
