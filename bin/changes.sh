#! /bin/sh

STARTDATE=`date -d "1 day ago" +"%Y-%m-%d 00:00:00"`
ENDDATE=`date +"%Y-%m-%d 00:00:00"`
echo `date` : Looking for new factoids between ${STARTDATE} and ${ENDDATE}
psql -U javabot javabot -c "select username, name, value from factoids where updated between date('${STARTDATE}') and date('${ENDDATE}') order by name"
