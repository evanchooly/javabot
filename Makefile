SHELL=/bin/bash
export CLASSPATH=rickyclarkson.jar:pircbot.jar:.

all: rickyclarkson.jar pircbot.jar classfiles javadocs jar export

clean: cleanclassfiles cleanjar cleanjavadocs cleanexport

distclean: clean cleanjars

cleanjars: cleanjar cleanrickyclarksonjar cleanpircbotjar

cleanrickyclarksonjar:
	rm -f rickyclarkson.jar

cleanpircbotjar:
	rm -f pircbot.jar

rickyclarkson.jar:
	cvs co com.rickyclarkson
	cd com.rickyclarkson && ${MAKE} most
	cp com.rickyclarkson/rickyclarkson.jar .
	rm -Rf com.rickyclarkson
	
pircbot.jar:
	cvs co pircbot
	cp pircbot/pircbot.jar .
	rm -Rf pircbot

classfiles: cleanclassfiles
	find . | grep '\.java$$' | xargs javac

cleanclassfiles:
	find . | grep '\.class$$' | xargs rm -vf

jar: cleanjar
	#${MAKE} cleanrickyclarksonjar cleanpircbotjar
	ls -1 | grep -v "\.jar$$" | xargs jar cvfm javabot.jar Manifest
	#${MAKE} rickyclarkson.jar pircbot.jar

cleanjar:
	rm -vf javabot.jar

javadocs: cleanjavadocs
	find . | grep '\.java$$' | xargs javadoc -d docs/

cleanjavadocs:
	rm -Rfv docs/

export: cleanexport rickyclarkson.jar pircbot.jar
	mkdir -v javabot-all
	cp -v javabot.jar rickyclarkson.jar pircbot.jar map.serialized srclist.txt javabot-all
	zip -rv javabot-all javabot-all
	rm -Rfv javabot-all
	
cleanexport:
	rm -fv javabot-all.zip
