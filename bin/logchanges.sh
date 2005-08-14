#! /bin/sh

STARTDATE=`date -d "1 week ago"`
echo `date` : Looking for new factoids after ${STARTDATE}
psql javabot -Ht -c "select changeDate, message from changes where changeDate > date('${STARTDATE}') order by changeDate desc"
