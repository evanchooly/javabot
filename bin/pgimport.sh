#! /bin/sh

import() {
	#dropdb -U javabot -e javabot
	#createdb -U javabot -e javabot
	psql -U javabot javabot -c "\dt" | grep public | while read LINE
	do
		TABLE=`echo $LINE | cut -d\| -f2`
		psql -U javabot javabot -q -c "drop table $TABLE cascade"
	done
	psql -U javabot javabot -q -c "drop sequence hibernate_sequence cascade"
	RESULT="`psql javabot -c "\d" | grep public`"
	echo RESULT=${RESULT}

	cd dumps
	psql -q javabot < schema.sql
	for i in *.sql
	do
		TABLE=`echo $i | cut -d\. -f1`
		[ "${TABLE}" == "schema" ] || psql -U javabot -q javabot -c "alter table $TABLE disable trigger all"
		[ $? -ne 0 ] && exit
	done
	for i in *.sql
	do
		TABLE=`echo $i | cut -d\. -f1`
		[ "${TABLE}" == "schema" ] || psql -U javabot -e javabot < $i
		[ $? -ne 0 ] && exit
	done
	for i in *.sql
	do
		TABLE=`echo $i | cut -d\. -f1`
		[ "${TABLE}" == "schema" ] || psql -U javabot -q javabot -c "alter table $TABLE enable trigger all"
		[ $? -ne 0 ] && exit
	done
	psql -U javabot -q javabot -c "ALTER SEQUENCE hibernate_sequence RESTART WITH 10000000";
}

[ ! -d dumps ] && ./dldumps.sh

import #2>&1 | tee import.out
