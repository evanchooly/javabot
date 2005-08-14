#! /bin/sh

STARTDATE=`date -d "1 day ago" +"%Y-%m-%d 00:00:00"`
echo `date` : Looking for new factoids between ${STARTDATE}
psql -U javabot javabot -c "select username, name, value from factoids where updated > date('${STARTDATE}') order by name"
