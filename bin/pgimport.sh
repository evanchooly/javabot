#! /bin/sh

#dropdb javabot
#createdb javabot

for i in *.dump
do
	TABLE=`echo $i | cut -d\. -f1`
	echo TABLE=${TABLE}
	psql javabot -e -c "delete from ${TABLE}"
	psql javabot -f $i
done
