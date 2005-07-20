#! /bin/sh

REVISION=$1
TAR_DIR=/home/javabot/public_html/

cd /tmp
/usr/bin/svn co http://www.cheeseronline.org/svn/repos/javabot
/usr/bin/rm  ${TAR_DIR}/javabot-src-*.tar*
/bin/tar --exclude=".svn" -cjvf ${TAR_DIR}/javabot-src-${REVISION}.tar.bz2 javabot
