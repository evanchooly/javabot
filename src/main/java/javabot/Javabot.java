package javabot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import javabot.operations.ForgetFactoidOperation;
import javabot.operations.GetFactoidOperation;
import javabot.operations.GuessOperation;
import javabot.operations.InfoOperation;
import javabot.operations.JavadocOperation;
import javabot.operations.KarmaChangeOperation;
import javabot.operations.KarmaReadOperation;
import javabot.operations.LeaveOperation;
import javabot.operations.LiteralOperation;
import javabot.operations.QuitOperation;
import javabot.operations.SeenOperation;
import javabot.operations.SpecialCasesOperation;
import javabot.operations.StatsOperation;
import javabot.operations.TellOperation;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Javabot extends PircBot implements ChannelControl, Responder {
    private static final Logger log = LoggerFactory.getLogger(Javabot.class);
    private final Map<String, String> channelPreviousMessages = new HashMap<String, String>();
    private List<BotOperation> operations = new LinkedList<BotOperation>();
    private String host;
    private int port;
    private String javabotNick;
    private String[] startStrings;
    private int authWait;
    private String password;
    private List<Channel> channels = new ArrayList<Channel>();
    private List<String> ignores = new ArrayList<String>();
    public static final String JAVABOT_PROPERTIES = "javabot.properties";
    @SpringBean
    private FactoidDao factoidDao;
    @SpringBean
    private ChangeDao changeDao;
    @SpringBean
    private SeenDao seenDao;
    @SpringBean
    private LogsDao logsDao;
    @SpringBean
    private ChannelDao channelDao;
    @SpringBean
    private ApiDao apiDao;
    @SpringBean
    private ClazzDao clazzDao;
    @SpringBean
    private KarmaDao karmaDao;
    @SpringBean
    private ConfigDao configDao;
    private boolean disconnecting = false;

    public Javabot() {
        setVersion("Javabot 2.0");
        InjectorHolder.getInjector().inject(this);
        loadConfig();
        connect();
    }

    private void loadConfig() {
        try {
            Config config = configDao.get();
            host = config.getServer();
            port = config.getPort();
            setName(config.getNick());
            javabotNick = config.getNick();
            setLogin(config.getNick());
            setNickPassword(config.getPassword());
            channels = config.getChannels();
            authWait = 3000;
            startStrings = config.getPrefixes().split(" ");
            loadOperationInfo(config);
            setMessageDelay(2000);
        } catch(Exception e) {
            log.debug(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void loadOperationInfo(Config root) {
        List<String> operationNodes = root.getOperations();
        for(String operationNode : operationNodes) {
            try {
                Class operationClass = Class.forName(operationNode);
                if(JavadocOperation.class.equals(operationClass)) {
                    operations.add(new JavadocOperation(apiDao, clazzDao));
                } else if(LeaveOperation.class.equals(operationClass)) {
                    operations.add(new LeaveOperation(this));
                } else if(LiteralOperation.class.equals(operationClass)) {
                    operations.add(new LiteralOperation(factoidDao));
                } else if(QuitOperation.class.equals(operationClass)) {
                    operations.add(new QuitOperation(getNickPassword()));
                } else if(SpecialCasesOperation.class.equals(operationClass)) {
                    operations.add(new SpecialCasesOperation(this));
                } else if(TellOperation.class.equals(operationClass)) {
                    operations.add(new TellOperation(getNick(), this));
                } else if(ForgetFactoidOperation.class.equals(operationClass)) {
                    operations.add(new ForgetFactoidOperation(factoidDao, changeDao));
                } else if(GuessOperation.class.equals(operationClass)) {
                    operations.add(new GuessOperation(factoidDao));
                } else if (KarmaChangeOperation.class.equals(operationClass)) {
                    operations.add(new KarmaChangeOperation(karmaDao));
                } else if (KarmaReadOperation.class.equals(operationClass)) {
                    operations.add(new KarmaReadOperation(karmaDao));
                } else if(StatsOperation.class.equals(operationClass)) {
                    operations.add(new StatsOperation(factoidDao));
                } else if(SeenOperation.class.equals(operationClass)) {
                    operations.add(new SeenOperation(seenDao));
                } else if(InfoOperation.class.equals(operationClass)) {
                    operations.add(new InfoOperation(factoidDao));
                } else {
                    if(!GetFactoidOperation.class.equals(operationClass) && !AddFactoidOperation.class
                        .equals(operationClass)) {
                        operations.add((BotOperation)operationClass.newInstance());
                    }
                }
            } catch(Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        operations.add(operations.size(), new AddFactoidOperation(factoidDao, changeDao));
        operations.add(operations.size(), new GetFactoidOperation(factoidDao));
        for(BotOperation operation : operations) {
            if(log.isDebugEnabled()) {
                log.debug(operation.getClass().getCanonicalName());
            }
        }

    }

    public void main(String[] args) throws IOException {
        if(log.isInfoEnabled()) {
            log.info("Starting Javabot");
        }
        Javabot bot = new Javabot();
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch(InterruptedException exception) {
        }
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    public void connect() {
        boolean connected = false;
        while(!connected) {
            try {
                connect(host, port);
                sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
                sleep(authWait);
                for(Channel channel : channels) {
                    joinChannel(channel.getName());
                }
                connected = true;
            } catch(Exception exception) {
                log.error(exception.getMessage(), exception);
            }
            sleep(1000);
        }
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        seenDao.logSeen(sender, channel, "said: " + message);
        if(channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.MESSAGE, sender, channel, message);
        }
        if(log.isDebugEnabled()) {
            log.debug("onMessage " + message + " Sender " + sender);
        }
        if(isValidSender(sender)) {
            for(String startString : startStrings) {
                int length = startString.length();
                if(message.startsWith(startString)) {
                    handleAnyMessage(channel, sender, login, hostname, message.substring(length).trim());
                    return;
                }
            }
            handleAnyChannelMessage(channel, sender, login, hostname, message);
        } else {
            if(log.isInfoEnabled()) {
                log.info("ignoring " + sender);
            }
        }
    }

    public List<Message> getResponses(String channel, String sender, String login, String hostname, String message) {
        if(log.isDebugEnabled()) {
            log.debug("getResponses " + message);
        }
        for(BotOperation operation : operations) {
            List<Message> messages = operation.handleMessage(new BotEvent(channel, sender, login, hostname, message));
            if(!messages.isEmpty()) {
                return messages;
            }
        }
        return null;
    }

    public List<Message> getChannelResponses(String channel, String sender, String login, String hostname,
        String message) {
        if(log.isDebugEnabled()) {
            log.debug("getChannelResponses " + message);
        }
        for(BotOperation operation : operations) {
            List<Message> messages = operation.handleChannelMessage(new BotEvent(channel, sender, login, hostname, message));
            if(!messages.isEmpty()) {
                return messages;
            }
        }
        return null;
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    private void handleAnyMessage(String channel, String sender, String login, String hostname, String message) {
        List<Message> messages = getResponses(channel, sender, login, hostname, message);
        if(messages != null) {
            String nick = sender;
            sender = javabotNick;
            for(Message nextMessage : messages) {
                if(nextMessage.isAction()) {
                    sendAction(nextMessage.getDestination(), nextMessage.getMessage());
                    if(log.isDebugEnabled()) {
                        log.debug("handleAnyMessage: Sender " + nick + " Message " + message + " Channel " + channel);
                    }
                    seenDao.logSeen(sender, nextMessage.getDestination(), "did a /me " + nextMessage.getMessage());
                    if(!channel.equals(nick)) {
                        if(channelDao.get(channel).getLogged()) {
                            logsDao.logMessage(Logs.Type.ACTION, sender, nextMessage.getDestination(),
                                nextMessage.getMessage());
                        }
                    }
                } else {
                    sendMessage(nextMessage.getDestination(), nextMessage.getMessage());
                    if(log.isDebugEnabled()) {
                        log.debug("handleAnyMessage: Type Message - Sender " + nick + " Message " + message + " Channel "
                        + channel);
                    }
                    seenDao.logSeen(sender, nextMessage.getDestination(), "said: " + nextMessage.getMessage());
                    if(!channel.equals(nick)) {
                        if(channelDao.get(channel).getLogged()) {
                            logsDao.logMessage(Logs.Type.MESSAGE, sender, nextMessage.getDestination(),
                                nextMessage.getMessage());
                        }
                    }
                }
            }
        }
        channelPreviousMessages.put(channel, message);
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    private void handleAnyChannelMessage(String channel, String sender, String login, String hostname, String message) {
        List<Message> messages = getChannelResponses(channel, sender, login, hostname, message);
        if(messages != null) {
            sender = javabotNick;
            for(Message nextMessage : messages) {
                if(nextMessage.isAction()) {
                    sendAction(nextMessage.getDestination(), nextMessage.getMessage());
                    if(log.isDebugEnabled()) {
                        log.debug("handleAnyChannelMessage: Sender " + sender + " Message " + message);
                    }
                    seenDao.logSeen(sender, nextMessage.getDestination(), "did a /me " + nextMessage.getMessage());
                    if(channelDao.get(channel).getLogged()) {
                        logsDao.logMessage(Logs.Type.ACTION, sender, nextMessage.getDestination(),
                            nextMessage.getMessage());
                    }
                } else {
                    sendMessage(nextMessage.getDestination(), nextMessage.getMessage());
                    if(log.isDebugEnabled()) {
                        log.debug("handleAnyChannelMessage: Sender " + sender + " Message " + message);
                    }
                    seenDao.logSeen(sender, nextMessage.getDestination(), "said: " + nextMessage.getMessage());
                    if(channelDao.get(channel).getLogged()) {
                        logsDao.logMessage(Logs.Type.MESSAGE, sender, nextMessage.getDestination(),
                            nextMessage.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname,
        String channel) {
        if(log.isDebugEnabled()) {
            log.debug("Invited to " + channel + " by " + sourceNick);
        }
        if(!channel.equals(channelDao.get(channel).getName())) {
            return;
        }
        joinChannel(channel);
    }

    @Override
    public void onDisconnect() {
        if(!disconnecting) {
            connect();
        }
    }

    public boolean isOnSameChannelAs(String nick) {
        for(String channel : getChannels()) {
            if(userIsOnChannel(nick, channel)) {
                return true;
            }
        }
        return false;
    }

    public boolean userIsOnChannel(String nick, String channel) {
        for(User user : getUsers(channel)) {
            if(user.getNick().toLowerCase().equals(nick.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        if(isOnSameChannelAs(sender)) {
            //The bot always replies with a privmessage...
            if(log.isDebugEnabled()) {
                log.debug("PRIVMSG Sender:" + sender + " Login" + login);
            }
            logsDao.logMessage(Logs.Type.MESSAGE, sender, sender, message);
            handleAnyMessage(sender, sender, login, hostname, message);
        }
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
        seenDao.logSeen(sender, channel, ":" + hostname + " joined the channel");
        if(channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.JOIN, sender, channel, ":" + hostname + " joined the channel");
        }
    }

    @Override
    public void onQuit(String channel, String sender, String login, String hostname) {
        //Not logged to a true channel
        //seenDao.logSeen(sender, channel, "quit");
        if(channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.QUIT, sender, channel, "quit");
        }
    }

    @Override
    public void onPart(String channel, String sender, String login, String hostname) {
        seenDao.logSeen(sender, channel, "parted the channel");
        if(channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.PART, sender, channel, "parted the channel");
        }

    }

    @Override
    public void onAction(String sender, String login, String hostname, String target, String action) {
        if(log.isDebugEnabled()) {
            log.debug("Sender " + sender + " Message " + action);
        }
        seenDao.logSeen(sender, target, "did a /me " + action);
        if(channelDao.get(target).getLogged()) {
            logsDao.logMessage(Logs.Type.ACTION, sender, target, action);
        }
    }

    @Override
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname,
        String recipientNick, String reason) {
        seenDao.logSeen(recipientNick, channel, kickerNick + " kicked " + recipientNick
            + " with this reasoning: " + reason);
        if(channelDao.get(channel).getLogged()) {
            logsDao.logMessage(Logs.Type.KICK, kickerNick, channel, " kicked " + recipientNick + " (" + reason + ")");
        }
    }

    public void onOp() {
    }

    public void setNickPassword(String value) {
        password = value;
    }

    public String getNickPassword() {
        return password;
    }

    private boolean isValidSender(String sender) {
        return !ignores.contains(sender);
    }

    public void addIgnore(String sender) {
        ignores.add(sender);
    }

    public void shutdown() {
        disconnecting = true;
        disconnect();
    }

    @Override
    public void log(String string) {
        if(log.isInfoEnabled()) {
            log.info(string);
        }
    }

    public String[] getStartStrings() {
        return startStrings;
    }
}
