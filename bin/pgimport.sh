#! /bin/sh

#dropdb javabot
#createdb javabot

for i in *.sql
do
	TABLE=`echo $i | cut -d\. -f1`
	echo TABLE=${TABLE}
	psql javabot -e -c "drop table ${TABLE};"
	psql javabot < $i
	[ $? -ne 0 ] && exit
done
