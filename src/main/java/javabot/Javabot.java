package javabot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javabot.dao.ApiDao;
import javabot.dao.ChangeDao;
import javabot.dao.ChannelDao;
import javabot.dao.ClazzDao;
import javabot.dao.ConfigDao;
import javabot.dao.FactoidDao;
import javabot.dao.KarmaDao;
import javabot.dao.LogsDao;
import javabot.dao.SeenDao;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.operations.AddFactoidOperation;
import javabot.operations.BotOperation;
import javabot.operations.GetFactoidOperation;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class Javabot extends PircBot implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(Javabot.class);
    private final Map<String, String> channelPreviousMessages = new HashMap<String, String>();
    private final Map<String, BotOperation> operations = new LinkedHashMap<String, BotOperation>();
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
    private SeenDao seenDao;
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
    private boolean disconnecting = false;
    private ApplicationContext context;

    public Javabot(final ApplicationContext applicationContext) {
        context = applicationContext;
        context.getAutowireCapableBeanFactory().autowireBean(this);
        setVersion("Javabot 2.5");
        loadConfig();
        connect();
    }

    @Transactional
    private void loadConfig() {
        try {
            final Config config = configDao.get();
            host = config.getServer();
            port = config.getPort();
            setName(config.getNick());
            setLogin(config.getNick());
            setNickPassword(config.getPassword());
            authWait = 3000;
            startStrings = config.getPrefixes().split(" ");
            loadOperationInfo(config);
//            setMessageDelay(2000);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private void loadOperationInfo(final Config config) {
        final List<String> operationNodes = config.getOperations();
        for (final String operationNode : operationNodes) {
            try {
                final Class<BotOperation> operationClass = (Class<BotOperation>) Class.forName(operationNode);
                if (!GetFactoidOperation.class.equals(operationClass) && !AddFactoidOperation.class
                    .equals(operationClass)) {
                    add(operationClass.getConstructor(getClass()).newInstance(this));
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        add(new AddFactoidOperation(this));
        add(new GetFactoidOperation(this));
    }

    private void add(final BotOperation operation) {
        context.getAutowireCapableBeanFactory().autowireBean(operation);
        operations.put(operation.getClass().getName(), operation);
        if (log.isDebugEnabled()) {
            log.debug(operation.getClass().getCanonicalName());
        }
    }

    public void main(final String[] args) throws IOException {
        if (log.isInfoEnabled()) {
            log.info("Starting Javabot");
        }
        new Javabot(context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml"));
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
        boolean connected = false;
        while (!connected) {
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
                connected = true;
            } catch (Exception exception) {
                disconnect();
                log.error(exception.getMessage(), exception);
            }
            sleep(1000);
        }
    }

    @Override
    public void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
        try {
            seenDao.logSeen(sender, channel, "said: " + message);
            final Channel chan = channelDao.get(channel);
            if (chan != null && chan.getLogged()) {
                logsDao.logMessage(Logs.Type.MESSAGE, sender, channel, message);
            }
            if (log.isDebugEnabled()) {
                log.debug("onMessage " + message + " Sender " + sender);
            }
            if (isValidSender(sender)) {
                for (final String startString : startStrings) {
                    if (message.startsWith(startString)) {
                        handleAnyMessage(channel, sender, login, hostname, message.substring(startString.length()).trim());
                        return;
                    }
                }
                handleAnyChannelMessage(channel, sender, login, hostname, message);
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

    public List<Message> getResponses(final String channel, final String sender, final String login, final String hostname, final String message) {
        if (log.isDebugEnabled()) {
            log.debug("getResponses " + message);
        }
        for (final BotOperation operation : operations.values()) {
            final List<Message> messages = operation.handleMessage(new BotEvent(channel, sender, login, hostname, message));
            if (!messages.isEmpty()) {
                return messages;
            }
        }
        return null;
    }

    public List<Message> getChannelResponses(final String channel, final String sender, final String login, final String hostname,
        final String message) {
        if (log.isDebugEnabled()) {
            log.debug("getChannelResponses " + message);
        }
        for (final BotOperation operation : operations.values()) {
            final List<Message> messages = operation
                .handleChannelMessage(new BotEvent(channel, sender, login, hostname, message));
            if (!messages.isEmpty()) {
                return messages;
            }
        }
        return null;
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    private void handleAnyMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
        final List<Message> messages = getResponses(channel, sender, login, hostname, message);
        if (messages != null) {
            for (final Message nextMessage : messages) {
                nextMessage.send(this);
            }
        }
        channelPreviousMessages.put(channel, message);
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    private void handleAnyChannelMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
        final List<Message> messages = getChannelResponses(channel, sender, login, hostname, message);
        if (messages != null) {
            for (final Message msg : messages) {
                msg.send(this);
            }
        }
    }

    @Override
    public void onInvite(final String targetNick, final String sourceNick, final String sourceLogin, final String sourceHostname,
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
        if (!disconnecting) {
            connect();
        }
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

    @Override
    public void onPrivateMessage(final String sender, final String login, final String hostname, final String message) {
        if (isOnSameChannelAs(sender)) {
            //The bot always replies with a privmessage...
            if (log.isDebugEnabled()) {
                log.debug("PRIVMSG Sender:" + sender + " Login" + login);
            }
            logsDao.logMessage(Logs.Type.MESSAGE, sender, sender, message);
            handleAnyMessage(sender, sender, login, hostname, message);
        }
    }

    @Override
    public void onJoin(final String channel, final String sender, final String login, final String hostname) {
        seenDao.logSeen(sender, channel, ":" + hostname + " joined the channel");
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.JOIN, sender, channel, ":" + hostname + " joined the channel");
        }
    }

    @Override
    public void onQuit(final String channel, final String sender, final String login, final String hostname) {
        //Not logged to a true channel
        //seenDao.logSeen(sender, channel, "quit");
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.QUIT, sender, channel, "quit");
        }
    }

    @Override
    public void onPart(final String channel, final String sender, final String login, final String hostname) {
        seenDao.logSeen(sender, channel, "parted the channel");
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.PART, sender, channel, "parted the channel");
        }

    }

    @Override
    public void onAction(final String sender, final String login, final String hostname, final String target, final String action) {
        if (log.isDebugEnabled()) {
            log.debug("Sender " + sender + " Message " + action);
        }
        seenDao.logSeen(sender, target, "did a /me " + action);
        if (channelDao.get(target).getLogged()) {
            logsDao.logMessage(Logs.Type.ACTION, sender, target, action);
        }
    }

    @Override
    public void onKick(final String channel, final String kickerNick, final String kickerLogin, final String kickerHostname,
        final String recipientNick, final String reason) {
        seenDao.logSeen(recipientNick, channel, kickerNick + " kicked " + recipientNick
            + " with this reasoning: " + reason);
        if (channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.KICK, kickerNick, channel, " kicked " + recipientNick + " (" + reason + ")");
        }
    }

    public void onOp() {
    }

    public void setNickPassword(final String value) {
        password = value;
    }

    public String getNickPassword() {
        return password;
    }

    private boolean isValidSender(final String sender) {
        return !ignores.contains(sender);
    }

    public void addIgnore(final String sender) {
        ignores.add(sender);
    }

    public void shutdown() {
        disconnecting = true;
        disconnect();
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
        seenDao.logSeen(sender, message.getDestination(), message.logEntry());
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
}
