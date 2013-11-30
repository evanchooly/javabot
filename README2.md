javabot
=======

factoid bot for irc channels

Building
--------
To build and test Javabot, you'll need to do a few things.

1. Install MongoDB.
   For Fedora, you can install it with 
   ```
      sudo yum install mongodb-server
   ```
   On OS X, you can use
   ```
      brew install mongo
   ```
1. Copy core/javabot.properties.sample to core/javabot.properties .
1. Copy migration/javabot.properties.sample to migration/javabot.properties .
1. For both new copies of javabot.properties, add two properties:
   database.host=localhost # or the hostname of your mongodb server
   database.port=27017 # or the port upon which mongodb is running
1. Start mongodb; one example command, which will locate the data files in the
   current directory, is this:  
```
    mongod --noauth --dbpath .
```
1. Build and test.
