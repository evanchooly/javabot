#! /bin/sh

STARTDATE=`date -d "1 day ago" +"%Y-%m-%d 00:00:00"`
STARTDATE=`date -d "1 week ago" +"%Y-%m-%d 00:00:00"`
ENDDATE=`date -d "+1 day" +"%Y-%m-%d 00:00:00"`
echo `date` : Looking for new factoids between ${STARTDATE} and ${ENDDATE}
psql javabot -Ht -c "select message from changes where changeDate between date('${STARTDATE}') and date('${ENDDATE}') order by changeDate"
