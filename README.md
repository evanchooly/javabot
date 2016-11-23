[![Build Status](https://travis-ci.org/evanchooly/javabot.svg?branch=master)](https://travis-ci.org/evanchooly/javabot)

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
   If you're using [Docker](https://www.docker.com/), you can run a Docker image for MongoDB:
   ```
      docker pull mongo
   ```

   (Note that if you're on Windows, you may need to add network translation to handle port 27017; see `images/nat
   .png` for how this would look in VirtualBox.)

1. Copy `javabot-sample.properties` to `javabot.properties` and update any properties as needed.  If you want to run the web application
 as well, copy `javabot-sample.yml` to `javabot.yml` and adjust as necessary. 

1. Start mongodb; one example command, which will locate the data files in the
   current directory, is this:  
```
    mongod --noauth --dbpath .
```
   In Docker, you'd use:
```
    docker run -d -p 27017:27017 --name mongodb mongo
```
1. Build and test.

Developing
------

If you use IDEA, make sure you have "Use plugin registry" enabled in your Maven configuration.

Note also that IDEA may not pick up the generated java source as part of the build path; if `Sofia` does not
resolve after running `mvn compile` at least once, then open up the javabot.iml file and add
`<sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />` under the `<content>` XML node.
`<content>` should look like this:

    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src/main/kotlin" isTestSource="false" />
      <sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />
      <sourceFolder url="file://$MODULE_DIR$/src/main/resources" type="java-resource" />
      <sourceFolder url="file://$MODULE_DIR$/src/test/kotlin" isTestSource="true" />
      <sourceFolder url="file://$MODULE_DIR$/src/test/resources" type="java-test-resource" />
      <sourceFolder url="file://$MODULE_DIR$/src/main" isTestSource="false" />
      <excludeFolder url="file://$MODULE_DIR$/target" />
    </content>

To test the web application aspects, you need to copy the `javabot-sample.yml` to `javabot.yml`; this will put the
web container on port 8081 by default.
