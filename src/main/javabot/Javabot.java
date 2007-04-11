package javabot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.rickyclarkson.java.lang.Debug;
import javabot.operations.AddFactoidOperation;
import javabot.operations.BotOperation;
import javabot.operations.DictOperation;
import javabot.operations.ForgetFactoidOperation;
import javabot.operations.GetFactoidOperation;
import javabot.operations.GuessOperation;
import javabot.operations.JavadocOperation;
import javabot.operations.KarmaChangeOperation;
import javabot.operations.KarmaReadOperation;
import javabot.operations.LeaveOperation;
import javabot.operations.LiteralOperation;
import javabot.operations.QuitOperation;
import javabot.operations.SpecialCasesOperation;
import javabot.operations.StatsOperation;
import javabot.operations.TellOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

public class Javabot extends PircBot implements ChannelControl, Responder {
    private static final Log log = LogFactory.getLog(Javabot.class);
    private final Map<String, String> channelPreviousMessages = new HashMap<String, String>();
    private List<BotOperation> operations;
    private String host;
    private String dictHost;
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

    public Javabot() throws JDOMException, IOException {
        setName("javabot");
        setLogin("javabot");
        setVersion("Javabot 1.7");
        loadConfig();
    }

    private void loadConfig() throws JDOMException, IOException {
        SAXBuilder reader = new SAXBuilder(true);
        File configFile = getConfigFile();
        Document document = null;
        try {
            document = reader.build(configFile.toURI().toURL());
        } catch(Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e.getMessage());
        }
        Element root = document.getRootElement();
        String verbosity = root.getAttributeValue("verbose");
        setVerbose("true".equals(verbosity));
        database = new JDBCDatabase(root);
        loadServerInfo(root);
        loadJavadocInfo(root);
        loadDictInfo(root);
        loadChannelInfo(root);
        loadAuthenticationInfo(root);
        loadStartStringInfo(root);
        loadOperationInfo(root);
        loadIgnoreInfo(root);
    }

    protected File getConfigFile() {
        return new File("config.xml");
    }

    private void loadIgnoreInfo(Element root) {
        List<Element> ignoreNodes = new ArrayList<Element>();
        for(Object element : root.getChildren("ignore")) {
            ignoreNodes.add((Element)element);
        }
        for(Element node : ignoreNodes) {
            ignores.add(node.getAttributeValue("name"));
        }
    }

    private void loadOperationInfo(Element root) {
        List operationNodes = root.getChildren("operation");
        Iterator iterator = operationNodes.iterator();
        operations = new ArrayList<BotOperation>();
        while(iterator.hasNext()) {
            Element node = (Element)iterator.next();
            try {
                Class operationClass = Class.forName
                    (node.getAttributeValue("class"));
                if(AddFactoidOperation.class.equals(operationClass)) {
                    operations.add(new AddFactoidOperation(database));
                } else if(DictOperation.class.equals(operationClass)) {
                    operations.add(new DictOperation(dictHost));
                } else if(ForgetFactoidOperation.class.equals(operationClass)) {
                    operations.add(new ForgetFactoidOperation(database));
                    Debug.printDebug();
                } else if(GetFactoidOperation.class.equals(operationClass)) {
                    operations.add(new GetFactoidOperation(database));
                } else if(GuessOperation.class.equals(operationClass)) {
                    operations.add(new GuessOperation(database));
                } else if(JavadocOperation.class.equals(operationClass)) {
                    operations.add(new JavadocOperation(javadocSources, javadocBaseUrl));
                } else if(KarmaChangeOperation.class.equals(operationClass)) {
                    operations.add(new KarmaChangeOperation(database, this));
                } else if(KarmaReadOperation.class.equals(operationClass)) {
                    operations.add(new KarmaReadOperation(database));
                } else if(LeaveOperation.class.equals(operationClass)) {
                    operations.add(new LeaveOperation(this));
                } else if(LiteralOperation.class.equals(operationClass)) {
                    operations.add(new LiteralOperation(database));
                } else if(QuitOperation.class.equals(operationClass)) {
                    operations.add(new QuitOperation(getNickPassword()));
                } else if(SpecialCasesOperation.class.equals(operationClass)) {
                    operations.add(new SpecialCasesOperation(this));
                } else if(StatsOperation.class.equals(operationClass)) {
                    operations.add(new StatsOperation(database));
                } else if(TellOperation.class.equals(operationClass)) {
                    operations.add(new TellOperation(getNick(), this));
                } else {
                    operations.add((BotOperation)operationClass.newInstance());
                }
                log.debug(operations.get(operations.size() - 1));
            } catch(Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private void loadStartStringInfo(Element root) {
        List startNodes = root.getChildren("message");
        Iterator iterator = startNodes.iterator();
        startStrings = new String[startNodes.size()];
        int index = 0;
        while(iterator.hasNext()) {
            Element node = (Element)iterator.next();
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
        for(Object channelNode : channelNodes) {
            Element node = (Element)channelNode;
            channels.add(node.getAttributeValue("name"));
        }
    }

    private void loadDictInfo(Element root) {
        Element dictNode = root.getChild("dict");
        dictHost = dictNode.getAttributeValue("host");
    }

    private void loadJavadocInfo(Element root) {
        Element javadocNode = root.getChild("javadoc");
        javadocSources = javadocNode.getAttributeValue("reference-xml");
        if(javadocSources == null) {
            throw new IllegalStateException
                ("The config file must supply a reference-xml attribute, as per the config.xml.sample file.");
        }
        javadocBaseUrl = javadocNode.getAttributeValue("base-url");
    }

    private void loadServerInfo(Element root) {
        Element serverNode = root.getChild("server");
        host = serverNode.getAttributeValue("name");
        port = Integer.parseInt(serverNode.getAttributeValue("port"));
    }

    public static void main(String[] args)
        throws IOException, JDOMException {
        log.info("Starting Javabot");
        Javabot bot = new Javabot();
        new PortListener(PORT_NUMBER, bot.getNickPassword()).start();
        bot.setMessageDelay(2000);
        bot.connect();
    }

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
                for(String channel : channels) {
                    joinChannel(channel);
                }
                connected = true;
            } catch(Exception exception) {
                log.error(exception.getMessage(), exception);
            }
            sleep(1000);
        }
    }

    @Override
    public void onMessage(String channel, String sender, String login,
        String hostname, String message) {
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
            log.info("ignoring " + sender);
        }
    }

    public List<Message> getResponses(String channel, String sender, String login, String hostname,
        String message) {
        for(BotOperation operation : operations) {
            List<Message> messages = operation.handleMessage(new BotEvent(channel, sender, login,
                hostname, message));
            if(!messages.isEmpty()) {
                return messages;
            }
        }
        return null;
    }

    public List getChannelResponses(String channel, String sender, String login, String hostname,
        String message) {
        for(BotOperation operation : operations) {
            List messages = operation.handleChannelMessage(new BotEvent(channel, sender,
                login, hostname, message));
            if(!messages.isEmpty()) {
                return messages;
            }
        }
        return null;
    }

    private void handleAnyMessage(String channel, String sender, String login, String hostname, String message) {
        List messages = getResponses(channel, sender, login, hostname, message);
        if(messages != null) {
            for(Object message1 : messages) {
                Message nextMessage = (Message)message1;
                if(nextMessage.isAction()) {
                    sendAction(nextMessage.getDestination(), nextMessage.getMessage());
                } else {
                    sendMessage(nextMessage.getDestination(), nextMessage.getMessage());
                }
            }
        }
        channelPreviousMessages.put(channel, message);
    }

    private void handleAnyChannelMessage(String channel, String sender, String login,
        String hostname, String message) {
        List messages = getChannelResponses(channel, sender, login, hostname, message);
        if(messages != null) {
            for(Object message1 : messages) {
                Message nextMessage = (Message)message1;
                if(nextMessage.isAction()) {
                    sendAction(nextMessage.getDestination(), nextMessage.getMessage());
                } else {
                    sendMessage(nextMessage.getDestination(), nextMessage.getMessage());
                }
            }
        }
    }

    @Override
    public void onInvite(String targetNick, String sourceNick, String sourceLogin,
        String sourceHostname, String channel) {
        if(channels.contains(channel)) {
            joinChannel(channel);
        }
    }

    @Override
    public void onDisconnect() {
        connect();
    }

    public String getPreviousMessage(String channel) {
        if(channelPreviousMessages.containsKey(channel)) {
            return channelPreviousMessages.get(channel);
        }
        return "";
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
            handleAnyMessage(sender, sender, login, hostname, message);
        }
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
