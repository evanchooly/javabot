package javabot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;
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
import javabot.database.UpgradeScript;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.operations.BotOperation;
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
    private final Set<BotOperation> operations = new TreeSet<BotOperation>();
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
    ChannelDao channelDao;
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
    private final ExecutorService executors;
    public static final int THROTTLE_TIME = 5 * 1000;

    @SuppressWarnings({"OverriddenMethodCallDuringObjectConstruction", "OverridableMethodCallDuringObjectConstruction"})
    public Javabot(final ApplicationContext applicationContext) throws IOException {
        context = applicationContext;
        inject(this);
        setVersion("Javabot " + loadVersion());
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
        applyUpgradeScripts();
        connect();
    }

    public void inject(final Object object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
    }

    private void applyUpgradeScripts() {
        final ServiceLoader<UpgradeScript> loader = ServiceLoader.load(UpgradeScript.class);
        for (final UpgradeScript script : loader) {
            script.execute(this);
        }

    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    private String loadVersion() throws IOException {
        final Properties props = new Properties();
        InputStream inStream = null;
        try {
            inStream = getClass().getResourceAsStream("/META-INF/maven/javabot/core/pom.properties");
            if (inStream == null) {
                final File file = new File("target/maven-archiver/pom.properties");
                if(file.exists()) {
                    inStream = new FileInputStream(file);
                }
            }
            if(inStream != null) {
                props.load(inStream);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        return props.getProperty("version");
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

    public void loadConfig(final Config config) {
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
        for (final String name : config.getOperations()) {
            try {
                addOperation(name);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }
        if (operations.isEmpty()) {
            for (final BotOperation operation : BotOperation.listKnownOperations()) {
                add(operation);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    public boolean addOperation(final String name) {
        boolean added = false;
        final List<BotOperation> ops = BotOperation.listKnownOperations();
        final Iterator<BotOperation> it = ops.iterator();
        while(it.hasNext() && !added) {
            final BotOperation operation = it.next();
            if (operation.getName().equalsIgnoreCase(name)) {
                added = add(operation);
            }
        }
        if (!added) {
            log.debug("Operation not found: " + name);
        }
        return added;
    }

    private boolean add(final BotOperation operation) {
        final Config config = configDao.get();
        final boolean added = config.getOperations().add(operation.getName());
        configDao.save(config);
        operation.setBot(this);
        context.getAutowireCapableBeanFactory().autowireBean(operation);
        operations.add(operation);

        return true;
    }

    @SuppressWarnings({"unchecked"})
    public boolean addOperation(final BotOperation operation) {
        final boolean added = operations.add(operation);
        if (added) {
            operation.setBot(this);
            context.getAutowireCapableBeanFactory().autowireBean(operation);
        }
        return added;
    }

    public boolean removeOperation(final String name) {
        boolean removed = false;
        final Iterator<BotOperation> it = operations.iterator();
        while (it.hasNext() && !removed) {
            final BotOperation operation = it.next();
            if (operation.getName().equals(name) && !operation.isStandardOperation()) {
                removed = true;
                it.remove();
                final Config config = configDao.get();
                config.getOperations().remove(name);
                configDao.save(config);
            }
        }
        return removed;
    }

    public static void main(final String[] args) throws IOException {
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
    public void connect() {
        while (!isConnected()) {
            try {
                connect(host, port);
                sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
                sleep(authWait);
                final List<Channel> channelList = channelDao.getChannels();
                if (channelList.isEmpty()) {
                    final Channel chan = new Channel();
                    chan.setName("##" + getNick());
                    System.out.println("No channels found.  Initializing to " + chan.getName());
                    channelDao.save(chan);
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

    public void processMessage(final String channel, final String message, final String sender, final String login,
        final String hostname) {
        try {
            logsDao.logMessage(Logs.Type.MESSAGE, sender, channel, message);
            if (isValidSender(sender)) {
                final List<Message> responses = responses(channel,
                        message,
                        sender,
                        login,
                        hostname);
                if (responses.isEmpty()) {
                    responses.addAll(getChannelResponses(channel, sender, login, hostname, message));
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
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Message> responses(final String channel,
            final String message,
            final String sender,
            final String login,
            final String hostname) {
        final List<Message> responses = new ArrayList<Message>();
        for (final String startString : startStrings) {
            if (message.startsWith(startString)) {
                responses.addAll(handleFullLineTrigger(channel,
                        message,
                        sender,
                        login,
                        hostname,
                        startString));
            }

            if (responses.isEmpty())
                responses.addAll(handleEmbeddedTriggers(channel,
                        message,
                        sender,
                        login,
                        hostname,
                        startString));
        }
        return responses;
    }

    private List<Message> handleEmbeddedTriggers(final String channel,
            final String message,
            final String sender,
            final String login,
            final String hostname,
            final String startString) {
        // Find embedded phrases of the form '(~foo bar () baz)'
        final String embeddedStartString = "(" + startString;
        for (int startIndex = message.indexOf(embeddedStartString); startIndex != -1; startIndex = message
                .indexOf(embeddedStartString, startIndex + 1)) {
            // Number of unbalanced (s seen. End of embedded segment if it goes
            // to zero.
            int parensLevel = 1;
            int endIndex = startIndex + embeddedStartString.length();
            for (; endIndex < message.length() && parensLevel > 0; ++endIndex) {
                switch (message.charAt(endIndex)) {
                    case ')':
                        --parensLevel;
                        break;
                    case '(':
                        ++parensLevel;
                        break;
                }
            }
            if (parensLevel == 0) {
                // endIndex is the index of the character after the ) at the end
                // of the embedded hit. startIndex is exactly on the (. Remove both.
                final String embeddedMessage = message.substring(startIndex
                        + 1, endIndex - 1);
                final String content = extractContent(embeddedMessage,
                        startString);

                return getResponses(channel, sender, login, hostname, content);
            }
        }
        return Collections.emptyList();
    }

    private List<Message> handleFullLineTrigger(final String channel,
            final String message,
            final String sender,
            final String login,
            final String hostname,
            final String startString) {
        final String content = extractContent(message,
                startString);
        List<Message> lineResponses = getResponses(channel,
                sender,
                login,
                hostname,
                content);
        return lineResponses;
    }

    public List<Message> getResponses(final String channel, final String sender, final String login,
        final String hostname, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        final BotEvent event = new BotEvent(channel, sender, login, hostname, message);
        while (responses.isEmpty() && iterator.hasNext()) {
            responses.addAll(iterator.next().handleMessage(event));
        }
//        System.out.println("responses = " + responses);
        return responses;
    }

    public Iterator<BotOperation> getOperations() {
        return operations.iterator();
    }

    public List<Message> getChannelResponses(final String channel, final String sender, final String login,
        final String hostname, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        while (responses.isEmpty() && iterator.hasNext()) {
            responses
                .addAll(iterator.next().handleChannelMessage(new BotEvent(channel, sender, login, hostname, message)));
        }
        return responses;
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

    public void setStartStrings(final String... startStrings) {
        final List<String> start = new ArrayList<String>();
        start.add(getNick());
        start.addAll(Arrays.asList(startStrings));
        this.startStrings = start.toArray(new String[start.size()]);
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

    public BotOperation getOperation(final Class<? extends BotOperation> clazz) {
        final Iterator<BotOperation> iter = getOperations();
        while (iter.hasNext()) {
            final BotOperation operation = iter.next();
            if (operation.getClass().equals(clazz)) {
                return operation;
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private String extractContent(final String message, final String startString) {
        String content = message.substring(startString.length()).trim();
        while (content.charAt(0) == ':' || content.charAt(0) == ',') {
            content = content.substring(1).trim();
        }
        return content;
    }

}