package javabot;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.persistence.NoResultException;

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
import javabot.operations.StatsOperation;
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
        BotOperation.getName(StatsOperation.class),
        BotOperation.getName(TimeOperation.class),
        BotOperation.getName(ShunOperation.class),
        BotOperation.getName(UnixCommandOperation.class)
    );
    private final ExecutorService executors;
    public static final int THROTTLE_TIME = 5 * 1000;

    static {
        STANDARD_OPERATIONS = new ArrayList<String>();
        STANDARD_OPERATIONS.add(BotOperation.getName(AdminOperation.class));
        STANDARD_OPERATIONS.add(BotOperation.getName(AddFactoidOperation.class));
        STANDARD_OPERATIONS.add(BotOperation.getName(ForgetFactoidOperation.class));
        STANDARD_OPERATIONS.add(BotOperation.getName(GetFactoidOperation.class));
    }

    public Javabot(final ApplicationContext applicationContext) {
        context = applicationContext;
        context.getAutowireCapableBeanFactory().autowireBean(this);
        setVersion("Javabot 3.0.5");
        Config config;
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
            startStrings = new String[]{getName(), "~"};
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
                    for (final Message response : getResponses(sender, sender, login, hostname, message)) {
                        response.send(Javabot.this);
                    }
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
        final Channel chan = channelDao.get(channel);
        if (chan != null && chan.getLogged()) {
            logsDao.logMessage(Logs.Type.QUIT, sender, channel, "quit");
        } else if (chan == null) {
            log.debug("not logging " + channel);
        }
    }

    @Override
    public void onInvite(final String targetNick, final String sourceNick, final String sourceLogin,
        final String sourceHostname, final String channel) {
        if (log.isDebugEnabled()) {
            log.debug("Invited to " + channel + " by " + sourceNick);
        }
        if (channel.equals(channelDao.get(channel).getName())) {
            joinChannel(channel);
        }
    }

    @Override
    public void onDisconnect() {
        if (!executors.isShutdown()) {
            connect();
        }
    }

    @Override
    public void onPart(final String channel, final String sender, final String login, final String hostname) {
        final Channel chan = channelDao.get(channel);
        if (chan != null && chan.getLogged()) {
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

    private void processMessage(final String channel, final String message, final String sender, final String login,
        final String hostname) {
        try {
            logsDao.logMessage(Logs.Type.MESSAGE, sender, channel, message);
            if (isValidSender(sender)) {
                List<Message> responses = new ArrayList<Message>();
                for (final String startString : startStrings) {
                    if (responses != null && message.startsWith(startString)) {
                        String content = message.substring(startString.length()).trim();
                        while (content.charAt(0) == ':' || content.charAt(0) == ',') {
                            content = content.substring(1).trim();
                        }
                        responses.addAll(getResponses(channel, sender, login, hostname, content));
                    }
                }
                if (responses.isEmpty()) {
                    responses.addAll(getChannelResponses(channel, sender, login, hostname, message));
                }
                for (Message response : responses) {
                    response.send(this);
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("ignoring " + sender);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Message> getResponses(final String channel, final String sender, final String login,
        final String hostname, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        List<Message> responses = new ArrayList<Message>();
        final BotEvent event = new BotEvent(channel, sender, login, hostname, message);
        while (responses.isEmpty() && iterator.hasNext()) {
            responses.addAll(iterator.next().handleMessage(event));
        }
//        System.out.println("responses = " + responses);
        return responses;
    }

    private Iterator<BotOperation> getOperations() {
        final List<BotOperation> list = new ArrayList<BotOperation>(operations.values());
        list.addAll(standardOperations.values());
        return list.iterator();
    }

    public List<Message> getChannelResponses(final String channel, final String sender, final String login,
        final String hostname, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        List<Message> reponse = new ArrayList<Message>();
        while (reponse == null && iterator.hasNext()) {
            reponse
                .addAll(iterator.next().handleChannelMessage(new BotEvent(channel, sender, login, hostname, message)));
        }
        return reponse;
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
        return new String[]{getNick(), "~"};
    }

    public void setStartStrings(final String[] startStrings) {
        this.startStrings = startStrings;
    }

    void postMessage(final Message message) {
        sendMessage(message.getDestination(), message.getMessage());
        logMessage(message);
    }

    void postAction(final Message message) {
        sendAction(message.getDestination(), message.getMessage());
        logMessage(message);
    }

    protected final void logMessage(final Message message) {
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
        final BotOperation operation = operations.get(name);
        return operation == null ? standardOperations.get(name) : operation;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}