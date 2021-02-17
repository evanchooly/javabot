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
      docker pull mongo # note that docker-compose (see below) can do this for you.
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
   You can also use the `docker-compose-test.yml`:
   ```
   docker-compose -f docker-compose-test.yml up
   ```
1. You will need to download the JavaDoc *manually* thanks to licensing issues with Java. To do this, go to the [Java download page](https://www.oracle.com/java/technologies/javase-downloads.html) and navigate to the Java version of your choice; you'll see a "JDK Download" and a "Documentation Download" link. Download the documentation, accepting licenses as appropriate; this will give you a file named, for example, `jdk-11.0.10_doc-all.zip` if that's the version you chose. Copy that file to the `javabot` directory, under the name `jdk-javadoc.jar` - which is actually set in the `javabot.properties` file, under the key `javadoc.jdk.file`.
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

Testing on a Live Server
------
Freenode maintains a [test instance of their ircd](https://freenode.net/news/testing-the-nets).  This is ideal for quickly running javabot and testing out commands or verifying your configuration.

To quickly get javabot running on a live server, ensure you've followed the steps in the Building section and change javabot.properties as follows:

```
# Connect to freenode test
javabot.server=testnet.freenode.net 
javabot.port=9002

# Give your javabot a unique nick
javabot.nick=tk-421
```

Then, start javabot using maven
```
mvn clean compile exec:java -DmainClass=javabot.Javabot
```
The bot may take attempt to cycle through servers on testnet to connect if they are unavailable.  Once it has been connected, invite it to a channel (e.g. `/invite tk-421 ##tk421-javabot-test`) and issue commands to it.  

Note that usage of testnet is optional and any available irc server should be sufficient.  Javabot's server config can be changed after it has been initialized by connecting directly to mongodb as follows:

```
$ mongo
> use javabot-sample
switched to db javabot-sample
> db.configuration.update({}, {$set:{"server":"chat.freenode.net","port":6667}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 0 })
```
