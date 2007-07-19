#! /bin/sh

psql javabot delete from changes;
delete from factoids;
delete from admin;
echo "insert into karma select * from factoids f where f.name like 'karma%'" | mysql --user=javabot --password=javabot javabot
echo "update karma set name=substring(name, 7)" | mysql --user=javabot --password=javabot javabot
echo "delete from factoids where name like 'karma%'" | mysql --user=javabot --password=javabot javabot
