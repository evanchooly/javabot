package javabot;

import ca.grimoire.maven.ArtifactDescription;
import ca.grimoire.maven.NoArtifactException;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
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

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Javabot extends PircBot implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(Javabot.class);
    private String host;
    private int port;
    private String[] startStrings;
    private int authWait;
    private String password;
    private final List<String> ignores = new ArrayList<String>();
    @Autowired
    private LogsDao logsDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private ShunDao shunDao;
    private ApplicationContext context;
    private final ExecutorService executors;
    public static final int THROTTLE_TIME = 5 * 1000;
    Config config;
    private Map<String, BotOperation> operations;

    @SuppressWarnings({"OverriddenMethodCallDuringObjectConstruction", "OverridableMethodCallDuringObjectConstruction"})
    public Javabot(final ApplicationContext applicationContext) {
        context = applicationContext;
        inject(this);
        setVersion("Javabot " + loadVersion());
        try {
            config = configDao.get();
        } catch (NoResultException e) {
            config = configDao.create();
        }
        executors = new ThreadPoolExecutor(15, 40, 10L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                new JavabotThreadFactory(true, "javabot-handler-thread-"));
        final Thread hook = new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        });
        hook.setDaemon(false);
        Runtime.getRuntime().addShutdownHook(hook);
        loadOperations(config);
        loadConfig();
        applyUpgradeScripts();
        connect();
    }

    public void inject(final Object object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
    }

    private void applyUpgradeScripts() {
        for (final UpgradeScript script : UpgradeScript.loadScripts()) {
            script.execute(this);
        }
    }


    private String loadVersion() {
        try {
            final ArtifactDescription javabot = ArtifactDescription.locate("javabot", "core");
            return javabot.getVersion();
        } catch (NoArtifactException nae) {
            return "UNKNOWN";
        }
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

    public void loadConfig() {
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
    private void loadOperations(final Config config) {
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

        if(config.getOperations().isEmpty()) {
            for (BotOperation operation : operations.values()) {
                enableOperation(operation.getName());
            }
        }
    }

    public boolean disableOperation(final String name) {
        boolean disabled = false;
        if (operations.get(name) != null) {
            operations.get(name).setEnabled(false);
            disabled = true;
        } else {
            final Config c = configDao.get();
            c.getOperations().remove(name);
            configDao.save(c);
        }
        return disabled;
    }

    public boolean enableOperation(String name) {
        boolean enabled = false;
        final Config c = configDao.get();
        if (operations.get(name) != null) {
            operations.get(name).setEnabled(true);
            c.getOperations().add(name);
            enabled = true;
        } else {
            c.getOperations().remove(name);
        }
        configDao.save(c);
        return enabled;
    }

    public BotOperation getOperation(String name) {
        return operations.get(name);
    }

    public Iterator<BotOperation> getOperations() {
        Set<BotOperation> active = new TreeSet<BotOperation>();
        for (BotOperation operation : operations.values()) {
            if (operation.isEnabled()) {
                active.add(operation);
            }
        }
        return active.iterator();
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
            //The bot always replies with a private message ...
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
                       final String kickerHostname, final String recipientNick, final String reason) {
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.KICK, kickerNick, channel, " kicked " + recipientNick + " (" + reason + ")");
        }
    }

    public void processMessage(final String channel, final String message, final String sender, final String login,
                               final String hostname) {
        try {
            logsDao.logMessage(Logs.Type.MESSAGE, sender, channel, message);
            if (isValidSender(sender)) {
                final List<Message> responses = new ArrayList<Message>();
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

    public List<Message> getResponses(final String channel, final String sender, final String login,
                                      final String hostname, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        final BotEvent event = new BotEvent(channel, sender, login, hostname, message);
        while (responses.isEmpty() && iterator.hasNext()) {
            responses.addAll(iterator.next().handleMessage(event));
        }
        return responses;
    }

    public List<Message> getChannelResponses(final String channel, final String sender, final String login,
                                             final String hostname, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        while (responses.isEmpty() && iterator.hasNext()) {
            responses.addAll(iterator.next()
                    .handleChannelMessage(new BotEvent(channel, sender, login, hostname, message)));
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
        logMessage(message);
        sendMessage(message.getDestination(), message.getMessage());
    }

    void postAction(final Message message) {
        logMessage(message);
        sendAction(message.getDestination(), message.getMessage());
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

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static void main(final String[] args) {
        if (log.isInfoEnabled()) {
            log.info("Starting Javabot");
        }
        new Javabot(new ClassPathXmlApplicationContext("classpath:applicationContext.xml"));
    }

}