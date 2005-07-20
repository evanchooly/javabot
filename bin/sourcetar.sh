#! /bin/sh

REVISION=$1
TAR_DIR=/home/javabot/public_html/src
TAR_NAME=javabot-src-${REVISION}.tar.bz2 

cd /tmp
/usr/bin/svn co http://www.cheeseronline.org/svn/repos/javabot
/usr/bin/rm  ${TAR_DIR}/javabot-src-*.tar*
/bin/tar --exclude=".svn" -cjvf ${TAR_DIR}/${TAR_NAME} javabot
rm -rf /tmp/javabot
cd ${TAR_DIR}
echo "<html><body><a href=\"${TAR_NAME}\">${TAR_NAME}</a></body></html>" > index.html

