using inet
using [java] org.jibble.pircbot

class FanBoy : PircBot {
    new make() {
        setVersion("${type.name} ${type.pod.version}");
        Config config := Config.getOrCreate
/*
        try {
            config = configDao.get();
        } catch (NoResultException e) {
            config = configDao.create();
        }
        executors = new ThreadPoolExecutor(15, 40, 10L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new JavabotThreadFactory(true, "javabot-handler-thread-"));
        final Thread hook = new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });
        hook.setDaemon(false);
        Runtime.getRuntime().addShutdownHook(hook);
        loadOperationInfo(config);
        loadConfig(config);
        connect();
*/
    }

}