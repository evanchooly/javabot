package javabot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.persistence.NoResultException;

import ca.grimoire.maven.ArtifactDescription;
import ca.grimoire.maven.NoArtifactException;
import ca.grimoire.maven.ResourceProvider;
import javabot.commands.AdminCommand;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.LogsDao;
import javabot.dao.ShunDao;
import javabot.database.UpgradeScript;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.operations.BotOperation;
import javabot.operations.OperationComparator;
import javabot.operations.StandardOperation;
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
    public static final Logger log = LoggerFactory.getLogger(Javabot.class);
    public static final int THROTTLE_TIME = 5 * 1000;
    private ApplicationContext context;
    private Config config;
    private Map<String, BotOperation> operations;
    private String host;
    private String nick;
    private String password;
    private String[] startStrings;
    protected final Channels channels = new Channels();
    private final ExecutorService executors;
    private final List<BotOperation> standard = new ArrayList<BotOperation>();
    private final List<String> ignores = new ArrayList<String>();
    private final Set<BotOperation> activeOperations = new TreeSet<BotOperation>(new OperationComparator());
    private int authWait;
    private int port;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private LogsDao logsDao;
    @Autowired
    private ShunDao shunDao;
    @Autowired
    private AdminDao adminDao;
    private SynchronousQueue<Runnable> queue;

    public Javabot(final ApplicationContext applicationContext) {
        context = applicationContext;
        setVersion(loadVersion());
        queue = new SynchronousQueue<Runnable>();
        executors = new ThreadPoolExecutor(15, 40, 10L, TimeUnit.SECONDS, queue,
            new JavabotThreadFactory(true, "javabot-handler-thread-"));
        final Thread hook = new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });
        hook.setDaemon(false);
        Runtime.getRuntime().addShutdownHook(hook);
        inject(this);
        try {
            config = configDao.get();
        } catch (NoResultException e) {
            config = configDao.create();
        }
        loadOperations(config);
        loadConfig();
        applyUpgradeScripts();
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

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    public void connect() {
        while (!isConnected()) {
            try {
                connect(host, port);
                sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
                sleep(authWait);
                final List<Channel> channelList = channelDao.getChannels();
                for (final Channel channel : channelList) {
                    channel.join(this);
                    sleep(500);
                }
            } catch (Exception exception) {
                disconnect();
                queue.clear();
                log.error(exception.getMessage(), exception);
            }
            sleep(1000);
        }
    }

    public static void validateProperties() {
        final Properties props = new Properties();
        InputStream stream = null;
        try {
            try {
                stream = new FileInputStream("javabot.properties");
                props.load(stream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Please define a javabot.properties file to configure the bot");
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        check(props, "javabot.server");
        check(props, "javabot.port");
        check(props, "jdbc.url");
        check(props, "jdbc.username");
        check(props, "jdbc.password");
        check(props, "jdbc.driver");
        check(props, "hibernate.dialect");
        check(props, "javabot.nick");
        check(props, "javabot.password");
        check(props, "javabot.admin.nick");
        check(props, "javabot.admin.hostmask");
        System.getProperties().putAll(props);
    }

    static void check(final Properties props, final String key) {
        if (props.get(key) == null) {
            throw new RuntimeException(String.format("Please specify the property %s in javabot.properties", key));
        }
    }

    public final void inject(final Object object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
    }

    protected final void applyUpgradeScripts() {
        for (final UpgradeScript script : UpgradeScript.loadScripts()) {
            script.execute(this);
        }
    }

    public final String loadVersion() {
        ArtifactDescription javabot;
        try {
            javabot = ArtifactDescription.locate("javabot", "core");
            return javabot.getVersion();
        } catch (NoArtifactException nae) {
            try {
                javabot = ArtifactDescription.locate("javabot", "core", new ResourceProvider() {
                    @Override
                    public InputStream getResourceAsStream(final String resource) {
                        try {
                            return new FileInputStream("target/maven-archiver/pom.properties");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    }
                });
                return javabot.getVersion();
            } catch (NoArtifactException e) {
                return "UNKNOWN";
            }
        }
    }

    public void loadConfig() {
        try {
            setName(config.getNick());
            setLogin(config.getNick());
            host = config.getServer();
            port = config.getPort();
            nick = config.getNick();
            setNickPassword(config.getPassword());
            authWait = 3000;
            startStrings = new String[]{getNick(), "~"};
            log.debug("Running with configuration: " + config);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings({"unchecked"})
    protected final void loadOperations(final Config config) {
        operations = new TreeMap<String, BotOperation>();
        for (final BotOperation op : BotOperation.list()) {
            inject(op);
            op.setBot(this);
            operations.put(op.getName(), op);
        }
        try {
            for (final String name : config.getOperations()) {
                enableOperation(name);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        if (config.getOperations().isEmpty()) {
            for (final BotOperation operation : operations.values()) {
                enableOperation(operation.getName());
            }
        }
        addDefaultOperations(ServiceLoader.load(AdminCommand.class));
        addDefaultOperations(ServiceLoader.load(StandardOperation.class));
        Collections.sort(standard, new Comparator<BotOperation>() {
            @Override
            public int compare(final BotOperation o1, final BotOperation o2) {
                if ("GetFactoid".equals(o1.getName())) {
                    return 1;
                }
                if ("GetFactoid".equals(o2.getName())) {
                    return -1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    private void addDefaultOperations(final ServiceLoader<? extends BotOperation> loader) {
        for (final BotOperation operation : loader) {
            inject(operation);
            operation.setBot(this);
            standard.add(operation);
        }
    }

    public boolean disableOperation(final String name) {
        boolean disabled = false;
        if (operations.get(name) != null) {
            activeOperations.remove(operations.get(name));
            final Config c = configDao.get();
            c.getOperations().remove(name);
            configDao.save(c);
            disabled = true;
        }
        return disabled;
    }

    public boolean enableOperation(final String name) {
        boolean enabled = false;
        final Config c = configDao.get();
        if (operations.get(name) != null) {
            activeOperations.add(operations.get(name));
            c.getOperations().add(name);
            enabled = true;
        } else {
            c.getOperations().remove(name);
        }
        configDao.save(c);
        return enabled;
    }

    public int getAuthWait() {
        return authWait;
    }

    public String getHost() {
        return host;
    }

    public Iterator<BotOperation> getOperations() {
        final List<BotOperation> ops = new ArrayList<BotOperation>(activeOperations);
        ops.addAll(standard);
        return ops.iterator();
    }

    public String[] getStartStrings() {
        return startStrings;
    }

    public void setStartStrings(final String... startStrings) {
        final List<String> start = new ArrayList<String>();
        start.add(getNick());
        start.addAll(Arrays.asList(startStrings));
        this.startStrings = start.toArray(new String[start.size()]);
    }

    public void processMessage(final String channel, final IrcUser sender, final String message) {
        try {
            logsDao.logMessage(Logs.Type.MESSAGE, sender.getNick(), channel, message);
            if (isValidSender(sender.getNick())) {
                final List<Message> responses = new ArrayList<Message>();
                for (final String startString : startStrings) {
                    if (responses != null && message.startsWith(startString)) {
                        String content = message.substring(startString.length()).trim();
                        while (content.charAt(0) == ':' || content.charAt(0) == ',') {
                            content = content.substring(1).trim();
                        }
                        responses.addAll(getResponses(channel, sender, content));
                    }
                }
                if (responses.isEmpty()) {
                    responses.addAll(getChannelResponses(channel, sender, message));
                }
                for (final Message response : responses) {
                    response.send(this);
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("ignoring " + sender);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void addIgnore(final String sender) {
        ignores.add(sender);
    }

    public void postMessage(final Message message) {
        logMessage(message);
        sendMessage(message.getDestination(), message.getMessage());
    }

    public void postAction(final Message message) {
        logMessage(message);
        sendAction(message.getDestination(), message.getMessage());
    }

    protected final void logMessage(final Message message) {
        final IrcEvent event = message.getEvent();
        final String sender = getNick();
        final String channel = event.getChannel();
        if (!channel.equals(sender)) {
            if (channelDao.isLogged(channel)) {
                logsDao.logMessage(Logs.Type.MESSAGE, sender, message.getDestination(), message.getMessage());
            }
        }
    }

    public List<Message> getResponses(final String channel, final IrcUser sender, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        final IrcEvent event = new IrcEvent(channel, sender, message);
        while (responses.isEmpty() && iterator.hasNext()) {
            responses.addAll(iterator.next().handleMessage(event));
        }
        return responses;
    }

    public String getNickPassword() {
        return password;
    }

    public void setNickPassword(final String password) {
        this.password = password;
    }

    public List<Message> getChannelResponses(final String channel, final IrcUser sender, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        while (responses.isEmpty() && iterator.hasNext()) {
            responses.addAll(iterator.next()
                .handleChannelMessage(new IrcEvent(channel, sender, message)));
        }
        return responses;
    }

    public boolean isOnSameChannelAs(final String user) {
        for (final String channel : getChannels()) {
            if (userIsOnChannel(user, channel)) {
                return true;
            }
        }
        return false;
    }

    public static void main(final String[] args) {
        if (log.isInfoEnabled()) {
            log.info("Starting Javabot");
        }
        validateProperties();
        new Javabot(new ClassPathXmlApplicationContext("classpath:applicationContext.xml"));
    }

    public IrcUser getUser(final String name) {
        return channels.getUser(name);
    }

    private IrcUser getUser(final String sender, final String login, final String hostname) {
        return new IrcUser(sender, login, hostname);
    }

    public boolean userIsOnChannel(final String nick, final String channel) {
        for (final User user : getUsers(channel)) {
            if (user.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isValidSender(final String sender) {
        return !ignores.contains(sender) && !isShunnedSender(sender);
    }

    private boolean isShunnedSender(final String sender) {
        return shunDao.isShunned(sender);
    }

    ChannelList getChannel(final String channel) {
        return channels.get(channel);
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    protected void sleep(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
        }
    }

    public void log(final String string) {
        if (log.isInfoEnabled()) {
            log.info(string);
        }
    }

    public String getNick() {
        return nick;
    }

    @Override
    public void onMessage(final String channel, final String sender, final String login, final String hostname,
        final String message) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                processMessage(channel, getUser(sender, login, hostname), message);
            }
        });
    }

    @Override
    public void onJoin(final String channel, final String sender, final String login, final String hostname) {
        ChannelList list = getChannel(channel);
        if (list == null) {
            list = channels.add(channel);
            final User[] users = getUsers(channel);
            for (final User user : users) {
                list.addUser(user.getNick());
            }
        }
        list.addUser(sender);
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
        if (channelDao.get(channel) != null) {
            joinChannel(channel);
        }
    }

    @Override
    public void onDisconnect() {
        if (!executors.isShutdown()) {
            try {
                connect(getHost(), getPort());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void onPrivateMessage(final String sender, final String login, final String hostname,
        final String message) {
        if (adminDao.isAdmin(sender, hostname) || isOnSameChannelAs(sender)) {
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    logsDao.logMessage(Logs.Type.MESSAGE, sender, sender, message);
                    for (final Message response : getResponses(sender, new IrcUser(sender, login, hostname), message)) {
                        response.send(Javabot.this);
                    }
                }
            });
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
        logsDao.logMessage(Logs.Type.ACTION, sender, target, action);
    }

    @Override
    public void onKick(final String channel, final String kickerNick, final String kickerLogin,
        final String kickerHostname, final String recipientNick, final String reason) {
        logsDao
            .logMessage(Logs.Type.KICK, kickerNick, channel, " kicked " + recipientNick + " (" + reason + ")");
    }
}
