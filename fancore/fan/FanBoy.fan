using inet
using [java] org.jibble.pircbot

class FanBoy : PircBot {
    const static private Log logging := Log.get("javabot")
    ActorPool pool := ActorPool.make()
    Str[]? startStrings

    new make() {
        setVersion("${type.name} ${type.pod.version}")
        Config config := Config.getOrCreate

        loadOperationInfo(config)
        loadConfig(config)
//        connectToServer(config)

//        Actor.sleep(Duration.maxVal) // Fan launcher exits if this thread exists
    }

    Void loadOperationInfo(Config config) {
        Str[] operationNodes := config.operations
        operationNodes.each | Str operation | {
//            if (!STANDARD_OPERATIONS.contains(operation)) {
//                addOperation(operation, operations);
//            }
        }
//        for (final String operation : STANDARD_OPERATIONS) {
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
            logging.debug(e.message, e);
            throw Err.make(e.message, e);
        }
    }

    Void connectToServer(Config config) {
        while (!isConnected()) {
            try {
                connect(config.server, config.port)
                sendRawLine("PRIVMSG NickServ :identify " + config.password)
                Actor.sleep(3sec)
                joinChannel("#javabot-staging")
/*
                final List<Channel> channelList = channelDao.getChannels();
                if (channelList.isEmpty()) {
                    Channel chan = new Channel();
                    chan.setName("##" + getNick());
                    System.out.println("No channels found.  Initializing to " + chan.getName());
                    changeDao.save(chan);
                    chan.join(this);
                } else {
                    for (final Channel channel : channelList) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                channel.join(Javabot.this);
                            }
                        }).start();
                    }
                }
*/
            } catch (Err exception) {
                disconnect();
                logging.error(exception.message, exception);
            }
            Actor.sleep(1sec)
        }
    }

    static Void main() {
        bot := FanBoy.make()
    }
}