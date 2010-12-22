package javabot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.persistence.NoResultException;

import ca.grimoire.maven.ArtifactDescription;
import ca.grimoire.maven.NoArtifactException;
import javabot.commands.AdminCommand;
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
import org.schwering.irc.lib.IRCEventAdapter;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCUser;
import org.schwering.irc.manager.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Javabot implements ApplicationContextAware {
    public static final int THROTTLE_TIME = 5 * 1000;
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
    Config config;
    private Map<String, BotOperation> operations;
    private Connection connection;
    private String nick;
    private final OperationsDispatchListener listener = new OperationsDispatchListener(this);
    private final Map<String, Set<IRCUser>> channels = new HashMap<String, Set<IRCUser>>();
    private final Map<String, IRCUser> users = new HashMap<String, IRCUser>();
    private final Set<BotOperation> activeOperations = new TreeSet<BotOperation>(new OperationComparator());
    private final List<BotOperation> standard = new ArrayList<BotOperation>();

    @SuppressWarnings({"OverriddenMethodCallDuringObjectConstruction", "OverridableMethodCallDuringObjectConstruction"})
    public Javabot(final ApplicationContext applicationContext) {
        context = applicationContext;
        inject(this);
        try {
            config = configDao.get();
        } catch (NoResultException e) {
            config = configDao.create();
        }
        final Thread hook = new Thread(new Runnable() {
            @Override
            public void run() {
                listener.shutdown();
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

    public String loadVersion() {
        try {
            final ArtifactDescription javabot = ArtifactDescription.locate("javabot", "core");
            return javabot.getVersion();
        } catch (NoArtifactException nae) {
            return "UNKNOWN";
        }
    }

    public void loadConfig() {
        try {
            log.debug("Running with configuration: " + config);
            host = config.getServer();
            port = config.getPort();
            setNick(config.getNick());
            setNickPassword(config.getPassword());
            authWait = 3000;
            startStrings = new String[]{getNick(), "~"};
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

    @Deprecated
    public BotOperation getOperation(final String name) {
        return operations.get(name);
    }

    public Iterator<BotOperation> getOperations() {
        final List<BotOperation> ops = new ArrayList<BotOperation>(activeOperations);
        ops.addAll(standard);
        return ops.iterator();
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
        if (connection == null) {
            connection = new Connection(host, new int[]{port}, false, getNickPassword(),
                getNick(), getNick(), getNick());
            addIrcEventListener(new LoggerListener());
            addIrcEventListener(new Tracker());
            addIrcEventListener(listener);
        }
        try {
            connection.connect();
            connection.send("PRIVMSG NickServ :identify " + getNickPassword());
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
            connection.quit(exception.getMessage());
            log.error(exception.getMessage(), exception);
        }
    }

    public void addIrcEventListener(final IRCEventListener listener) {
        inject(listener);
        connection.addIRCEventListener(listener);
    }

    public boolean isOnSameChannelAs(final IRCUser user) {
        for (final String channel : getChannels()) {
            if (userIsOnChannel(user, channel)) {
                return true;
            }
        }
        return false;
    }

    public boolean userIsOnChannel(final IRCUser sender, final String channel) {
        for (final IRCUser user : getUsers(channel)) {
            if (user.getNick().equalsIgnoreCase(sender.getNick())) {
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

    public boolean isValidSender(final IRCUser sender) {
        return !ignores.contains(sender.getNick()) && !isShunnedSender(sender);
    }

    private boolean isShunnedSender(final IRCUser sender) {
        return shunDao.isShunned(sender.getNick());
    }

    public void addIgnore(final String sender) {
        ignores.add(sender);
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
        connection.sendPrivmsg(message.getDestination(), message.getMessage());
    }

    void postAction(final Message message) {
        logMessage(message);
        connection.sendCtcpAction(message.getDestination(), message.getMessage());
    }

    protected final void logMessage(final Message message) {
        final IrcEvent event = message.getEvent();
        final String sender = getNick();
        final String channel = event.getChannel();
        if (!channel.equals(sender) && channelDao.isLogged(channel)) {
            logsDao.logMessage(Logs.Type.MESSAGE, sender, message.getDestination(), message.getMessage());
        }
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }

    public Set<String> getChannels() {
        return channels.keySet();
    }

    public Set<IRCUser> getUsers(final String channel) {
        return channels.get(channel);
    }

    public void joinChannel(final String channel) {
        connection.joinChannel(channel);
    }

    public void joinChannel(final String channel, final String key) {
        connection.joinChannel(channel, key);
    }

    public void partChannel(final String channel) {
        connection.partChannel(channel);
    }

    public OperationsDispatchListener getListener() {
        return listener;
    }

    public void addUser(final IRCUser user) {
        users.put(user.getUsername(), user);
    }

    public IRCUser getUser(final String name) {
        return users.get(name);
    }

    private class Tracker extends IRCEventAdapter {
        @Override
        public void onPart(final String chan, final IRCUser user, final String msg) {
            if (user.getNick().equals(getNick())) {
                channels.remove(chan);
            } else {
                channels.get(chan).remove(user);
            }
        }

        @Override
        public void onJoin(final String chan, final IRCUser user) {
            Set<IRCUser> members;
            synchronized (channels) {
                members = channels.get(chan);
                if (members == null) {
                    members = new TreeSet<IRCUser>();
                    channels.put(chan, members);
                }
            }
            members.add(user);
            addUser(user);
        }

        @Override
        public void onInvite(final String chan, final IRCUser user, final String passiveNick) {
            super.onInvite(chan, user, passiveNick);
            joinChannel(chan);
            connection.sendPrivmsg(chan, "I was asked to join by " + user);
        }
    }

    private static void validateProperties() throws IOException {
        Properties props = new Properties();
        InputStream stream = null;
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

    private static void check(final Properties props, final String key) {
        if (props.get(key) == null) {
            throw new RuntimeException(String.format("Please specify the property %s in javabot.properties", key));
        }
    }

    public static void main(final String[] args) throws IOException {
        if (log.isInfoEnabled()) {
            log.info("Starting Javabot");
        }
        validateProperties();
        new Javabot(new ClassPathXmlApplicationContext("classpath:applicationContext.xml"));
    }
}
