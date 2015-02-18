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
      docker pull dockerfile/mongodb
   ```

   (Note that if you're on Windows, you may need to add network translation to handle port 27017; see `images/nat
   .png` for how this would look in VirtualBox.)

1. Copy core/javabot.properties.sample to core/javabot.properties and update any properties as needed.
(The actual location of javabot.properties depends on how you're running the tests; it needs to be in the
current working directory for the tests. In IDEA, that's *normally* the root of the project; your mileage may vary.)

1. Start mongodb; one example command, which will locate the data files in the
   current directory, is this:  
```
    mongod --noauth --dbpath .
```
   In Docker, you'd use:
```
    docker run -d -p 27017:27017 --name mongodb dockerfile/mongodb
```
1. Build and test.

Developing
------

If you use IDEA, make sure you have "Use plugin registry" enabled in your Maven configuration.