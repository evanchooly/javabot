#! /bin/sh

cat etc/temp.sql | grep -v "INSERT INTO configuration"  | grep -v "INSERT INTO admin" > etc/db.sql
rm etc/temp.sql
