using inet
using [java] org.jibble.pircbot

const class FanBoy : PircBot {
    const static private Log logging := Log.get("javabot")
    const Actor actor
    const Str[]? startStrings

    new make(Actor a) {
        actor = a
        setVersion("${type.name} ${type.pod.version}")
        Config config := Config.getOrCreate

        loadOperationInfo(config)
        loadConfig(config)
        connectToServer(config)
        startStrings = [getNick()]

        Actor.sleep(Duration.maxVal) // Fan launcher exits if this thread exists
    }

    Void loadOperationInfo(Config config) {
        Str[] operationNodes := config.operations
        operationNodes.each | Str operation | {
            addOperation(operation)
        }
//        for (String operation : STANDARD_OPERATIONS) {
//            addOperation(operation, standardOperations);
//        }
    }

    Void loadConfig(Config config) {
        try {
            logging.debug("Running with configuration: " + config)
            setName(config.nick)
            setLogin(config.nick)
            startStrings = config.prefixes.split(' ')
        } catch (Err e) {
            logging.debug(e.message, e)
            throw Err.make(e.message, e)
        }
    }

    Void connectToServer(Config config) {
        while (!isConnected()) {
            try {
                connect(config.server, config.port)
                sendRawLine("PRIVMSG NickServ :identify " + config.password)
                Actor.sleep(1sec)
                logging.debug("loading channels")
                Channel[] channelList := Channel.list
                if (channelList.isEmpty) {
                    Channel chan := Channel.make
                    chan.name = "##${getNick()}"
                    logging.debug("No channels found.  Initializing to " + chan.name)
                    chan.save
                    chan.join(this)
                } else {
//                    receive := | Channel channel | { joinChannel(channel.name + (channel.key != null ? " ${channel.key}" : "")) }
                    channelList.each {
                        actor.send(it)
                    }
                }
            } catch (Err exception) {
                disconnect();
                logging.error(exception.message, exception);
            }
            Actor.sleep(1sec)
        }
    }

    Void addOperation( Str name) {
        try {
            operation := type.pod.findType("${name}Operation")->make(this)
        } catch (UnknownTypeErr e) {
            logging.debug("Operation not found: " + name)
        }
    }

    static Void main() {
        
                    
        logging.level = LogLevel.debug
        bot := FanBoy.make()
    }
}