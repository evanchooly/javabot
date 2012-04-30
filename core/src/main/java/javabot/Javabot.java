package javabot;

import ca.grimoire.maven.ArtifactDescription;
import ca.grimoire.maven.NoArtifactException;
import ca.grimoire.maven.ResourceProvider;
import javabot.commands.AdminCommand;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.EventDao;
import javabot.dao.LogsDao;
import javabot.dao.ShunDao;
import javabot.database.UpgradeScript;
import javabot.model.AdminEvent;
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

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Javabot implements ApplicationContextAware {
    public static final Logger log = LoggerFactory.getLogger(Javabot.class);
    public static final int THROTTLE_TIME = 5 * 1000;
    private ApplicationContext context;
    Config config;
    private Map<String, BotOperation> operations;
    private String host;
    String nick;
    private String password;
    private String[] startStrings;
    ExecutorService executors;
    private final ScheduledExecutorService eventHandler = Executors.newScheduledThreadPool(1,
            new JavabotThreadFactory(true, "javabot-event-handler"));
    private final List<BotOperation> standard = new ArrayList<BotOperation>();
    private final List<String> ignores = new ArrayList<String>();
    private final Set<BotOperation> activeOperations = new TreeSet<BotOperation>(new OperationComparator());
    private int port;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    LogsDao logsDao;
    @Autowired
    private ShunDao shunDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    AdminDao adminDao;
    private BlockingQueue<Runnable> queue;
    protected MyPircBot pircBot;

    public Javabot(final ApplicationContext applicationContext) {
        context = applicationContext;
        setUpThreads();
        inject(this);
        try {
            config = configDao.get();
        } catch (NoResultException e) {
            config = configDao.create();
        }
        loadOperations(config);
        loadConfig();
        applyUpgradeScripts();
        createIrcBot();
        startStrings = new String[]{pircBot.getNick(), "~"};
        connect();
    }

    private void setUpThreads() {
        queue = new ArrayBlockingQueue<Runnable>(50);
        executors = new ThreadPoolExecutor(5, 10, 10L, TimeUnit.SECONDS, queue,
            new JavabotThreadFactory(true, "javabot-handler-thread-"));
        final Thread hook = new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });
        hook.setDaemon(false);
        Runtime.getRuntime().addShutdownHook(hook);

        eventHandler.schedule(new Runnable() {
            @Override
            public void run() {
                processAdminEvents();
            }
        }, 5, TimeUnit.SECONDS);
    }

    protected void processAdminEvents() {
        List<AdminEvent> list = eventDao.findUnprocessed();
        for (AdminEvent event : list) {
            event.handle(this);
            event.setProcessed(true);
            eventDao.save(event);
        }
    }

    protected void createIrcBot() {
        pircBot = new MyPircBot(this);
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

    public ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    public void connect() {
        while (!pircBot.isConnected()) {
            try {
                pircBot.connect(host, port);
                pircBot.sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
                sleep(3000);
                final List<Channel> channelList = channelDao.getChannels();
                for (final Channel channel : channelList) {
                    channel.join(this);
                    sleep(1000);
                }
            } catch (Exception exception) {
                queue.clear();
                pircBot.disconnect();
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
                stream = Javabot.class.getResourceAsStream("/javabot.properties");
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
        ArtifactDescription description;
        try {
            description = ArtifactDescription.locate("javabot", "core");
            return description.getVersion();
        } catch (NoArtifactException nae) {
            try {
                final File file = new File("target/maven-archiver/pom.properties");
                if(file.exists()) {
                description = ArtifactDescription.locate("javabot", "core", new ResourceProvider() {
                    @Override
                    public InputStream getResourceAsStream(final String resource) {
                        try {
                            return new FileInputStream(file);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    }
                });
                return description.getVersion();
                } else {
                    return "UNKNOWN";
                }
            } catch (NoArtifactException e) {
                return "UNKNOWN";
            }
        }
    }

    public void loadConfig() {
        try {
            host = config.getServer();
            port = config.getPort();
            nick = config.getNick();
            setNickPassword(config.getPassword());
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
                activeOperations.add(operations.get(name));
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
        Collections.sort(standard, new BotOperationComparator());
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

    public void processMessage(final IrcEvent event) {
        try {
            final IrcUser sender = event.getSender();
            final String message = event.getMessage();
            final String channel = event.getChannel();

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
        pircBot.sendMessage(message.getDestination(), message.getMessage());
    }

    public void postAction(final Message message) {
        logMessage(message);
        pircBot.sendAction(message.getDestination(), message.getMessage());
    }

    protected final void logMessage(final Message message) {
        final IrcEvent event = message.getEvent();
        final String sender = pircBot.getNick();
        final String channel = event.getChannel();
        if (!channel.equals(sender)) {
            logsDao.logMessage(Logs.Type.MESSAGE, sender, message.getDestination(), message.getMessage());
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
        for (final String channel : pircBot.getChannels()) {
            if (userIsOnChannel(user, channel)) {
                return true;
            }
        }
        return false;
    }

    IrcUser getUser(final String sender, final String login, final String hostname) {
        return new IrcUser(sender, login, hostname);
    }

    public boolean userIsOnChannel(final String nick, final String channel) {
        for (final User user : pircBot.getUsers(channel)) {
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

    @SuppressWarnings({"EmptyCatchBlock"})
    protected void sleep(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
        }
    }

    public PircBot getPircBot() {
        return pircBot;
    }

    public void join(String name, String key) {
        if (name.startsWith("#")) {
            log.debug("Joining " + name);
            if (key == null) {
                pircBot.joinChannel(name);
            } else {
                pircBot.joinChannel(name, key);
            }
        }
    }

    public void leave(String name, String reason) {
        pircBot.partChannel(name, reason);
    }
    public static void main(final String[] args) {
        if (log.isInfoEnabled()) {
            log.info("Starting Javabot");
        }
        validateProperties();
        new Javabot(new ClassPathXmlApplicationContext("classpath:applicationContext.xml"));
    }

}
