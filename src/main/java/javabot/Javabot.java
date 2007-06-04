package javabot;

import javabot.dao.*;
import javabot.dao.model.Logs;
import javabot.operations.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Javabot extends PircBot implements ChannelControl, Responder {
    private static final Log log = LogFactory.getLog(Javabot.class);
    private final Map<String, String> channelPreviousMessages = new HashMap<String, String>();
    private List<BotOperation> operations;
    private String host;
    private String dictHost;
    private String nickName;
    private int port;
    private String javadocSources;
    private String javadocBaseUrl;
    private String[] startStrings;

    private int authWait;
    private String password;
    private List<String> channels = new ArrayList<String>();
    private List<String> ignores = new ArrayList<String>();
    private Database database;
    public static final int PORT_NUMBER = 2346;
    public static final String JAVABOT_PROPERTIES = "javabot.properties";

    // Spring wiring
    ApplicationContext context = new ClassPathXmlApplicationContext("/javabot/applicationContext.xml");

    public FactoidDao factoid_dao = (FactoidDao) context.getBean("factoidDao");

    public ChangesDao change_dao = (ChangesDao) context.getBean("changesDao");

    public SeenDao seen_dao = (SeenDao) context.getBean("seenDao");

    public LogDao log_dao = (LogDao) context.getBean("logDao");

    public ChannelConfigDao channel_dao = (ChannelConfigDao) context.getBean("channelDao");

    private String htmlFileName;

    public Javabot() throws JDOMException, IOException {
        setName("javabot");
        setLogin("javabot");
        setVersion("Javabot 1.9");
        loadConfig();
    }

    private void loadConfig() throws JDOMException, IOException {
        SAXBuilder reader = new SAXBuilder(true);
        File configFile = getConfigFile();
        Document document = null;
        try {
            document = reader.build(configFile.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e.getMessage());
        }
        Element root = document.getRootElement();

        htmlFileName = root.getChild("factoids").getAttributeValue("htmlfilename");

        String verbosity = root.getAttributeValue("verbose");
        setVerbose("true".equals(verbosity));
        database = new JDBCDatabase(root);
        loadServerInfo(root);
        loadJavadocInfo(root);
        loadDictInfo(root);
        loadNickName(root);
        loadChannelInfo(root);
        loadAuthenticationInfo(root);
        loadStartStringInfo(root);
        loadOperationInfo(root);
        loadIgnoreInfo(root);

    }

    protected File getConfigFile() {
        //return new File("config.xml");
        return new File(new File(System.getProperty("user.home")), ".javabot/config.xml").getAbsoluteFile();
    }

    private void loadIgnoreInfo(Element root) {
        List<Element> ignoreNodes = new ArrayList<Element>();
        for (Object element : root.getChildren("ignore")) {
            ignoreNodes.add((Element) element);
        }
        for (Element node : ignoreNodes) {
            ignores.add(node.getAttributeValue("name"));
        }
    }

    private void loadOperationInfo(Element root) {

        List operationNodes = root.getChildren("operation");
        Iterator iterator = operationNodes.iterator();
        operations = new ArrayList<BotOperation>();

        while (iterator.hasNext()) {
            Element node = (Element) iterator.next();
            try {
                Class operationClass = Class.forName(node.getAttributeValue("class"));
                if (AddFactoidOperation.class.equals(operationClass)) {
                    operations.add(new AddFactoidOperation(database));
                } else if (DictOperation.class.equals(operationClass)) {
                    operations.add(new DictOperation(dictHost));
                } else
                if (ForgetFactoidOperation.class.equals(operationClass)) {
                    operations.add(new ForgetFactoidOperation(database));
                } else if (GetFactoidOperation.class.equals(operationClass)) {
                    operations.add(new GetFactoidOperation(database));
                } else if (GuessOperation.class.equals(operationClass)) {
                    operations.add(new GuessOperation(database));
                } else if (JavadocOperation.class.equals(operationClass)) {
                    operations.add(new JavadocOperation(javadocSources, javadocBaseUrl));
                } else
                if (KarmaChangeOperation.class.equals(operationClass)) {
                    operations.add(new KarmaChangeOperation(database, this));
                } else if (KarmaReadOperation.class.equals(operationClass)) {
                    operations.add(new KarmaReadOperation(database));
                } else if (LeaveOperation.class.equals(operationClass)) {
                    operations.add(new LeaveOperation(this));
                } else if (LiteralOperation.class.equals(operationClass)) {
                    operations.add(new LiteralOperation(database));
                } else if (QuitOperation.class.equals(operationClass)) {
                    operations.add(new QuitOperation(getNickPassword()));
                } else
                if (SpecialCasesOperation.class.equals(operationClass)) {
                    operations.add(new SpecialCasesOperation(this));
                } else if (StatsOperation.class.equals(operationClass)) {
                    operations.add(new StatsOperation(database));
                } else if (TellOperation.class.equals(operationClass)) {
                    operations.add(new TellOperation(getNick(), this));
                } else
                if (AddFactoidOperation2.class.equals(operationClass)) {
                    operations.add(new AddFactoidOperation2(factoid_dao, change_dao, htmlFileName));
                } else
                if (ForgetFactoidOperation2.class.equals(operationClass)) {
                    operations.add(new ForgetFactoidOperation2(factoid_dao, change_dao, htmlFileName));
                } else
                if (GetFactoidOperation2.class.equals(operationClass)) {
                    operations.add(new GetFactoidOperation2(factoid_dao));
                } else
                if (KarmaChangeOperation2.class.equals(operationClass)) {
                    operations.add(new KarmaChangeOperation2(factoid_dao, change_dao, htmlFileName, this));
                } else if (KarmaReadOperation2.class.equals(operationClass)) {
                    operations.add(new KarmaReadOperation2(factoid_dao, change_dao, htmlFileName));
                } else if (StatsOperation2.class.equals(operationClass)) {
                    operations.add(new StatsOperation2(factoid_dao));
                } else if (SeenOperation2.class.equals(operationClass)) {
                    operations.add(new SeenOperation2(seen_dao));
                } else {
                    operations.add((BotOperation) operationClass.newInstance());
                }
                log.debug(operations.get(operations.size() - 1));
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private void loadStartStringInfo(Element root) {
        List startNodes = root.getChildren("message");
        Iterator iterator = startNodes.iterator();
        startStrings = new String[startNodes.size()];
        int index = 0;
        while (iterator.hasNext()) {
            Element node = (Element) iterator.next();
            startStrings[index] = node.getAttributeValue("tag");
            index++;
        }
    }

    private void loadAuthenticationInfo(Element root) {
        Element authNode = root.getChild("auth");
        authWait = Integer.parseInt(authNode.getAttributeValue("wait"));
        setNickPassword(authNode.getAttributeValue("password"));
        Element nickNode = root.getChild("nick");
        setName(nickNode.getAttributeValue("name"));
    }

    private void loadChannelInfo(Element root) {
        List channelNodes = root.getChildren("channel");
        for (Object channelNode : channelNodes) {
            Element node = (Element) channelNode;
            channels.add(node.getAttributeValue("name"));
        }
    }

    private void loadDictInfo(Element root) {
        Element dictNode = root.getChild("dict");
        dictHost = dictNode.getAttributeValue("host");
    }

    private void loadNickName(Element root) {
        Element nickNode = root.getChild("nick");
        nickName = nickNode.getAttributeValue("name");
    }

    private void loadJavadocInfo(Element root) {
        Element javadocNode = root.getChild("javadoc");
        javadocSources = javadocNode.getAttributeValue("reference-xml");
        if (javadocSources == null) {
            throw new IllegalStateException("The config file must supply a reference-xml attribute, as per the config.xml.sample file.");
        }
        javadocBaseUrl = javadocNode.getAttributeValue("base-url");
    }

    private void loadServerInfo(Element root) {
        Element serverNode = root.getChild("server");
        host = serverNode.getAttributeValue("name");
        port = Integer.parseInt(serverNode.getAttributeValue("port"));
    }

    public void main(String[] args) throws IOException, JDOMException {
        log.info("Starting Javabot");
        Javabot bot = new Javabot();

        new PortListener(PORT_NUMBER, bot.getNickPassword()).start();
        bot.setMessageDelay(2000);
        bot.connect();
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
        }
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    public void connect() {
        boolean connected = false;
        while (!connected) {
            try {
                connect(host, port);
                sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
                sleep(authWait);
                for (String channel : channels) {
                    joinChannel(channel);
                }
                connected = true;
            } catch (Exception exception) {
                log.error(exception.getMessage(), exception);
            }
            sleep(1000);
        }
    }


    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        seen_dao.logSeen(sender, channel, "said: " + message);

        if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {
            log_dao.logMessage(Logs.Type.MESSAGE, sender, channel, message);
        }
        log.info("Message " + message);
        if (isValidSender(sender)) {

            for (String startString : startStrings) {
                int length = startString.length();
                if (message.startsWith(startString)) {
                    handleAnyMessage(channel, sender, login, hostname, message.substring(length).trim());
                    return;
                }
            }
            handleAnyChannelMessage(channel, sender, login, hostname, message);
        } else {
            log.info("ignoring " + sender);
        }
    }

    public List<Message> getResponses(String channel, String sender, String login, String hostname, String message) {
        log.info("getResponses " + message);
        for (BotOperation operation : operations) {
            List<Message> messages = operation.handleMessage(new BotEvent(channel, sender, login, hostname, message));
            if (!messages.isEmpty()) {
                return messages;
            }
        }
        return null;
    }

    public List getChannelResponses(String channel, String sender, String login, String hostname, String message) {
        log.info("getChannelResponses " + message);
        for (BotOperation operation : operations) {
            List messages = operation.handleChannelMessage(new BotEvent(channel, sender, login, hostname, message));
            if (!messages.isEmpty()) {
                return messages;
            }
        }
        return null;
    }

    private void handleAnyMessage(String channel, String sender, String login, String hostname, String message) {
        List messages = getResponses(channel, sender, login, hostname, message);
        if (messages != null) {
            for (Object message1 : messages) {
                Message nextMessage = (Message) message1;
                if (nextMessage.isAction()) {
                    sendAction(nextMessage.getDestination(), nextMessage.getMessage());
                    seen_dao.logSeen(nickName, nextMessage.getDestination(), "did a /me " + nextMessage.getMessage());
                    if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {

                        log_dao.logMessage(Logs.Type.ACTION, nickName, nextMessage.getDestination(), "ACTION:" + nextMessage.getMessage());
                    }
                } else {
                    sendMessage(nextMessage.getDestination(), nextMessage.getMessage());
                    seen_dao.logSeen(nickName, nextMessage.getDestination(), "said: " + nextMessage.getMessage());
                    if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {

                        log_dao.logMessage(Logs.Type.MESSAGE, nickName, nextMessage.getDestination(), nextMessage.getMessage());
                    }
                }
            }
        }
        channelPreviousMessages.put(channel, message);
    }

    private void handleAnyChannelMessage(String channel, String sender, String login, String hostname, String message) {
        List messages = getChannelResponses(channel, sender, login, hostname, message);
        if (messages != null) {
            for (Object message1 : messages) {
                Message nextMessage = (Message) message1;
                if (nextMessage.isAction()) {
                    sendAction(nextMessage.getDestination(), nextMessage.getMessage());
                    seen_dao.logSeen(nickName, nextMessage.getDestination(), "did a /me " + nextMessage.getMessage());
                    if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {

                        log_dao.logMessage(Logs.Type.ACTION, nickName, nextMessage.getDestination(), "ACTION:" + nextMessage.getMessage());
                    }
                } else {
                    sendMessage(nextMessage.getDestination(), nextMessage.getMessage());
                    seen_dao.logSeen(nickName, nextMessage.getDestination(), "said: " + nextMessage.getMessage());
                    if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {

                        log_dao.logMessage(Logs.Type.MESSAGE, nickName, nextMessage.getDestination(), nextMessage.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
        if (channels.contains(channel)) {
            joinChannel(channel);
        }
    }

    @Override
    public void onDisconnect() {
        connect();
    }

    public String getPreviousMessage(String channel) {
        if (channelPreviousMessages.containsKey(channel)) {
            return channelPreviousMessages.get(channel);
        }
        return "";
    }

    public boolean isOnSameChannelAs(String nick) {
        for (String channel : getChannels()) {
            if (userIsOnChannel(nick, channel)) {
                return true;
            }
        }
        return false;
    }

    public boolean userIsOnChannel(String nick, String channel) {
        for (User user : getUsers(channel)) {
            if (user.getNick().toLowerCase().equals(nick.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        if (isOnSameChannelAs(sender)) {
            handleAnyMessage(sender, sender, login, hostname, message);
        }
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
        seen_dao.logSeen(sender, channel, "joined the channel");
        if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {

            log_dao.logMessage(Logs.Type.JOIN, sender, channel, "joined the channel");
        }
    }

    @Override
    public void onQuit(String channel, String sender, String login, String hostname) {
        seen_dao.logSeen(sender, channel, "quit");
        if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {

            log_dao.logMessage(Logs.Type.QUIT, sender, channel, "quit");
        }
    }

    @Override
    public void onPart(String channel, String sender, String login, String hostname) {
        seen_dao.logSeen(sender, channel, "parted the channel");
        if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {

            log_dao.logMessage(Logs.Type.PART, sender, channel, "parted the channel");
        }

    }

    @Override
    public void onAction(String sender, String login, String hostname, String target, String action) {
        seen_dao.logSeen(sender, target, "did a /me " + action);
        if (channel_dao.getChannel(target).getChannel() != null && channel_dao.getChannel(target).getLogged()) {

            log_dao.logMessage(Logs.Type.ACTION, sender, target, action);
        }
    }

    @Override
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        seen_dao.logSeen(recipientNick, channel, kickerNick + " kicked " + recipientNick + " with this reasoning: " + reason);
        if (channel_dao.getChannel(channel).getChannel() != null && channel_dao.getChannel(channel).getLogged()) {
            log_dao.logMessage(Logs.Type.KICK, kickerNick, channel, " kicked " + recipientNick + " (" + reason + ")");
        }
    }

    public void onOp() {

    }

    public String getDictHost() {
        return dictHost;
    }

    public String getJavadocSources() {
        return javadocSources;
    }

    public String getJavadocBaseUrl() {
        return javadocBaseUrl;
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
        disconnect();

    }

    @Override
    public void log(String string) {
        log.info(string);
    }
}
