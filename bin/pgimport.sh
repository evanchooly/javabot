#! /bin/sh

import() {
	dropdb -e javabot
	createdb -e javabot

	for i in *.sql
	do
		TABLE=`echo $i | cut -d\. -f1`
		echo TABLE=${TABLE}
		#psql javabot -e -c "drop table ${TABLE};"
		psql -e javabot < $i
		[ $? -ne 0 ] && exit
	done
}

import 2>&1 | tee import.out
