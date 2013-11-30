To build and test Javabot, you'll need to do a few things.

1. Install MongoDB.
   For Fedora, you can install it with "sudo yum install mongodb-server".

2. Copy core/javabot.properties.sample to core/javabot.properties .

3. Copy migration/javabot.properties.sample to migration/javabot.properties .

4. For both new copies of javabot.properties, add two properties:
   database.host=localhost # or the hostname of your mongodb server
   database.port=27017 # or the port upon which mongodb is running

5. Start mongodb; one example command, which will locate the data files in the
   current directory, is this:
   mongod --noauth --dbpath .

6. Build and test.