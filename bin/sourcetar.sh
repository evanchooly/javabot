#! /bin/sh

REVISION=$1
cd /tmp
svn co http://www.cheeseronline.org/svn/repos/javabot
#REVISION="`svn info javabot | grep Revision: | cut -d: -f2 | cut -c2-`"
TAR_DIR=/home/javabot/public_html/
rm  ${TAR_DIR}/javabot-src-*.tar*
tar --exclude=".svn" -cjvf ${TAR_DIR}/javabot-src-${REVISION}.tar.bz2 javabot

#rm -r javabot
