#! /bin/sh

#dropdb javabot
#createdb javabot


psql javabot -f changes.dump
psql javabot -f classes.dump
psql javabot -f factoids.dump
psql javabot -f karma.dump
psql javabot -f methods.dump
