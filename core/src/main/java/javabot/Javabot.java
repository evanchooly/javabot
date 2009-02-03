package javabot;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javabot.dao.ApiDao;
import javabot.dao.ChangeDao;
import javabot.dao.ChannelDao;
import javabot.dao.ClazzDao;
import javabot.dao.ConfigDao;
import javabot.dao.FactoidDao;
import javabot.dao.KarmaDao;
import javabot.dao.LogsDao;
import javabot.dao.ShunDao;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.operations.AddFactoidOperation;
import javabot.operations.AdminOperation;
import javabot.operations.AolBonicsOperation;
import javabot.operations.BotOperation;
import javabot.operations.DaysToChristmasOperation;
import javabot.operations.DaysUntilOperation;
import javabot.operations.DictOperation;
import javabot.operations.ForgetFactoidOperation;
import javabot.operations.GetFactoidOperation;
import javabot.operations.GoogleOperation;
import javabot.operations.IgnoreOperation;
import javabot.operations.InfoOperation;
import javabot.operations.JSROperation;
import javabot.operations.JavadocOperation;
import javabot.operations.KarmaChangeOperation;
import javabot.operations.KarmaReadOperation;
import javabot.operations.LeaveOperation;
import javabot.operations.LiteralOperation;
import javabot.operations.Magic8BallOperation;
import javabot.operations.NickometerOperation;
import javabot.operations.QuitOperation;
import javabot.operations.SayOperation;
import javabot.operations.SeenOperation;
import javabot.operations.ShunOperation;
import javabot.operations.SpecialCasesOperation;
import javabot.operations.StatsOperation;
import javabot.operations.TellOperation;
import javabot.operations.TimeOperation;
import javabot.operations.UnixCommandOperation;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Javabot extends PircBot implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(Javabot.class);
    public static final ArrayList<String> STANDARD_OPERATIONS;
    private final Map<String, BotOperation> operations = new LinkedHashMap<String, BotOperation>();
    private final Map<String, BotOperation> standardOperations = new LinkedHashMap<String, BotOperation>();
    private String host;
    private int port;
    private String[] startStrings;
    private int authWait;
    private String password;
    private final List<String> ignores = new ArrayList<String>();
    @Autowired
    private FactoidDao factoidDao;
    @Autowired
    private ChangeDao changeDao;
    @Autowired
    private LogsDao logsDao;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private ClazzDao clazzDao;
    @Autowired
    private KarmaDao karmaDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private ShunDao shunDao;
    private ApplicationContext context;
    public static final List<String> OPERATIONS = Arrays.asList(
        BotOperation.getName(AolBonicsOperation.class),
        BotOperation.getName(DaysToChristmasOperation.class),
        BotOperation.getName(DaysUntilOperation.class),
        BotOperation.getName(DictOperation.class),
        BotOperation.getName(ForgetFactoidOperation.class),
        BotOperation.getName(GoogleOperation.class),
        BotOperation.getName(IgnoreOperation.class),
        BotOperation.getName(InfoOperation.class),
        BotOperation.getName(JavadocOperation.class),
        BotOperation.getName(JSROperation.class),
        BotOperation.getName(KarmaChangeOperation.class),
        BotOperation.getName(KarmaReadOperation.class),
        BotOperation.getName(LeaveOperation.class),
        BotOperation.getName(LiteralOperation.class),
        BotOperation.getName(Magic8BallOperation.class),
        BotOperation.getName(NickometerOperation.class),
        BotOperation.getName(QuitOperation.class),
        BotOperation.getName(SayOperation.class),
        BotOperation.getName(SeenOperation.class),
        BotOperation.getName(SpecialCasesOperation.class),
        BotOperation.getName(StatsOperation.class),
        BotOperation.getName(TellOperation.class),
        BotOperation.getName(TimeOperation.class),
        BotOperation.getName(ShunOperation.class),
        BotOperation.getName(UnixCommandOperation.class)
    );
    private final ExecutorService executors;

    static {
        STANDARD_OPERATIONS = new ArrayList<String>();
        STANDARD_OPERATIONS.add(BotOperation.getName(AdminOperation.class));
        STANDARD_OPERATIONS.add(BotOperation.getName(AddFactoidOperation.class));
        STANDARD_OPERATIONS.add(BotOperation.getName(GetFactoidOperation.class));
    }

    public Javabot(final ApplicationContext applicationContext) {
        context = applicationContext;
        context.getAutowireCapableBeanFactory().autowireBean(this);
        setVersion("Javabot 3.0.1");
        final Config config = configDao.get();
        executors = Executors.newCachedThreadPool(new JavabotThreadFactory());
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
    }

    public void shutdown() {
        if (!executors.isShutdown()) {
            executors.shutdown();
            try {
                executors.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public final void loadConfig(final Config config) {
        try {
            log.debug("Running with configuration: " + config);
            host = config.getServer();
            port = config.getPort();
            setName(config.getNick());
            setLogin(config.getNick());
            setNickPassword(config.getPassword());
            authWait = 3000;
            startStrings = config.getPrefixes().split(" ");
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private void loadOperationInfo(final Config config) {
        final List<String> operationNodes = config.getOperations();
        for (final String operation : operationNodes) {
            if (!STANDARD_OPERATIONS.contains(operation)) {
                addOperation(operation, operations);
            }
        }
        for (final String operation : STANDARD_OPERATIONS) {
            addOperation(operation, standardOperations);
        }

    }

    @SuppressWarnings({"unchecked"})
    public boolean addOperation(final String name) {
        return addOperation(name, operations);
    }

    @SuppressWarnings({"unchecked"})
    public boolean addOperation(final String name, final Map<String, BotOperation> operations) {
        boolean added = false;
        try {
            final Class<BotOperation> clazz = (Class<BotOperation>) Class.forName(
                String.format("%s.%sOperation", BotOperation.class.getPackage().getName(), name));
            if (!operations.containsKey(name)) {
                final Constructor<BotOperation> ctor = clazz.getConstructor(Javabot.class);
                final BotOperation operation = ctor.newInstance(this);
                context.getAutowireCapableBeanFactory().autowireBean(operation);
                operations.put(name, operation);
                added = true;
                if (log.isDebugEnabled()) {
                    log.debug(operation.getClass().getCanonicalName());
                }
            }
        } catch (ClassNotFoundException e) {
            log.debug("Operation not found: " + name);
        } catch (Exception e) {
            processReflectionException(e);
        }
        return added;
    }

    public boolean remove(final Class<BotOperation> clazz) {
        return removeOperation(BotOperation.getName(clazz));
    }

    public boolean removeOperation(final String name) {
        return operations.remove(name) != null;
    }

    public List<String> listActiveOperations() {
        return new ArrayList<String>(operations.keySet());
    }

    private void processReflectionException(final Exception e) {
        log.error(e.getMessage(), e);
        throw new RuntimeException(e.getMessage());
    }

    public static void main(final String[] args) {
        if (log.isInfoEnabled()) {
            log.info("Starting Javabot");
        }
        new Javabot(new ClassPathXmlApplicationContext("classpath:applicationContext.xml"));
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    private void sleep(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
        }
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    public final void connect() {
        while (!isConnected()) {
            try {
                connect(host, port);
                sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
                sleep(authWait);
                for (final Channel channel : channelDao.getChannels()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            channel.join(Javabot.this);
                        }
                    }).start();
                }
            } catch (Exception exception) {
                disconnect();
                log.error(exception.getMessage(), exception);
            }
            sleep(1000);
        }
    }

    @Override
    public void onMessage(final String channel, final String sender, final String login, final String hostname,
        final String message) {
/*
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!done[0]) {
                    sleep(500);
                }
            }
        });
        thread.setDaemon(true);
*/
        executors.execute(new Runnable() {
            @Override
            public void run() {
                processMessage(channel, message, sender, login, hostname);
            }
        });
    }

    @Override
    public void onPrivateMessage(final String sender, final String login, final String hostname, final String message) {
        if (isOnSameChannelAs(sender)) {
            //The bot always replies with a privmessage...
            if (log.isDebugEnabled()) {
                log.debug("PRIVMSG Sender:" + sender + " Login" + login);
            }
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    logsDao.logMessage(Logs.Type.MESSAGE, sender, sender, message);
                    getResponses(sender, sender, login, hostname, message);
                }
            });
        }
    }

    @Override
    public void onJoin(final String channel, final String sender, final String login, final String hostname) {
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.JOIN, sender, channel, ":" + hostname + " joined the channel");
        }
    }

    @Override
    public void onQuit(final String channel, final String sender, final String login, final String hostname) {
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.QUIT, sender, channel, "quit");
        }
    }

    @Override
    public void onInvite(final String targetNick, final String sourceNick, final String sourceLogin,
        final String sourceHostname,
        final String channel) {
        if (log.isDebugEnabled()) {
            log.debug("Invited to " + channel + " by " + sourceNick);
        }
        if (!channel.equals(channelDao.get(channel).getName())) {
            return;
        }
        joinChannel(channel);
    }

    @Override
    public void onDisconnect() {
        if (!executors.isShutdown()) {
            connect();
        }
    }

    @Override
    public void onPart(final String channel, final String sender, final String login, final String hostname) {
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.PART, sender, channel, "parted the channel");
        }

    }

    @Override
    public void onAction(final String sender, final String login, final String hostname, final String target,
        final String action) {
        if (log.isDebugEnabled()) {
            log.debug("Sender " + sender + " Message " + action);
        }
        if (channelDao.get(target).getLogged()) {
            logsDao.logMessage(Logs.Type.ACTION, sender, target, action);
        }
    }

    @Override
    public void onKick(final String channel, final String kickerNick, final String kickerLogin,
        final String kickerHostname,
        final String recipientNick, final String reason) {
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.KICK, kickerNick, channel, " kicked " + recipientNick + " (" + reason + ")");
        }
    }

    @Override
    public void onOp(final String channel, final String sourceNick, final String sourceLogin,
        final String sourceHostname, final String recipient) {
    }

    private void processMessage(final String channel, final String message, final String sender, final String login,
        final String hostname) {
        final Channel chan = channelDao.get(channel);
        try {
            if (log.isDebugEnabled()) {
                log.debug("onMessage " + message + " Sender " + sender);
            }
            if (isValidSender(sender)) {
                boolean handled = false;
                for (final String startString : startStrings) {
                    if (!handled && message.startsWith(startString)) {
                        handled = getResponses(channel, sender, login, hostname,
                            message.substring(startString.length()).trim());
                    }
                }
                if (!handled) {
                    getChannelResponses(channel, sender, login, hostname, message);
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("ignoring " + sender);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (chan != null && chan.getLogged()) {
                logsDao.logMessage(Logs.Type.MESSAGE, sender, channel, message);
            }
        }
    }

    public boolean getResponses(final String channel, final String sender, final String login,
        final String hostname, final String message) {
        if (log.isDebugEnabled()) {
            log.debug("getResponses " + message);
        }
        final Iterator<BotOperation> iterator = getOperations();
        boolean handled = false;
        final BotEvent event = new BotEvent(channel, sender, login, hostname, message);
        while (!handled && iterator.hasNext()) {
            handled = iterator.next().handleMessage(event);
        }
        return handled;
    }

    private Iterator<BotOperation> getOperations() {
        final List<BotOperation> list = new ArrayList<BotOperation>(operations.values());
        list.addAll(standardOperations.values());
        return list.iterator();
    }

    public boolean getChannelResponses(final String channel, final String sender, final String login,
        final String hostname, final String message) {
        if (log.isDebugEnabled()) {
            log.debug("getChannelResponses " + message);
        }
        final Iterator<BotOperation> iterator = getOperations();
        boolean handled = false;
        while (!handled && iterator.hasNext()) {
            handled = iterator.next().handleChannelMessage(new BotEvent(channel, sender, login, hostname, message));
        }
        return handled;
    }

    public boolean isOnSameChannelAs(final String nick) {
        for (final String channel : getChannels()) {
            if (userIsOnChannel(nick, channel)) {
                return true;
            }
        }
        return false;
    }

    public boolean userIsOnChannel(final String nick, final String channel) {
        for (final User user : getUsers(channel)) {
            if (user.getNick().toLowerCase().equals(nick.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void setNickPassword(final String value) {
        password = value;
    }

    public String getNickPassword() {
        return password;
    }

    private boolean isValidSender(final String sender) {
        return !ignores.contains(sender) && !isShunnedSender(sender);
    }

    private boolean isShunnedSender(final String sender) {
        return shunDao.isShunned(sender);
    }

    public void addIgnore(final String sender) {
        ignores.add(sender);
    }

    @Override
    public void log(final String string) {
        if (log.isInfoEnabled()) {
            log.info(string);
        }
    }

    public String[] getStartStrings() {
        return startStrings;
    }

    public void postMessage(final Message message) {
        sendMessage(message.getDestination(), message.getMessage());
        logMessage(message);
    }

    public void postAction(final Message message) {
        sendAction(message.getDestination(), message.getMessage());
        logMessage(message);
    }

    protected final void logMessage(final Message message) {
        if (log.isDebugEnabled()) {
            log.debug("Message " + message.getMessage());
        }
        final BotEvent event = message.getEvent();
        final String sender = getNick();
        final String channel = event.getChannel();
        if (!channel.equals(sender)) {
            if (channelDao.isLogged(channel)) {
                logsDao.logMessage(Logs.Type.MESSAGE, sender, message.getDestination(), message.getMessage());
            }
        }
    }

    public BotOperation getOperation(final String name) {
        return operations.get(name);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private static class JavabotThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix = "javabot-thread-";

        public Thread newThread(final Runnable runnable) {
            final Thread t = new Thread(runnable, namePrefix + threadNumber.getAndIncrement());
            t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

}
