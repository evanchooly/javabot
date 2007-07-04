#! /bin/sh

echo "delete from changes" | mysql --user=javabot --password=javabot javabot
echo "delete from factoids" | mysql --user=javabot --password=javabot javabot
cat db.sql | while read LINE
do
	echo $LINE | mysql --user=javabot --password=javabot javabot
done
echo "select count(*) from factoids" | mysql --user=javabot --password=javabot javabot

echo "insert into karma select * from factoids f where f.name like 'karma%'" | mysql --user=javabot --password=javabot javabot
echo "delete from factoids where name like 'karma%'" | mysql --user=javabot --password=javabot javabot
