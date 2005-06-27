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
import javabot.operations.ListFactoidsOperation;
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
    private static Log log = LogFactory.getLog(Javabot.class);
    private Map channelPreviousMessages = new HashMap();
    private List<BotOperation> operations;
    private String host;
    private String dictHost;
    private int port;
    private String javadocSources;
    private String javadocBaseUrl;
    private String[] startStrings;
    private int authWait;
    private String password;
    private List<String> _channels = new ArrayList<String>();
    private List ignores = new ArrayList();
    private Database _database;
    public static final int PORT_NUMBER = 2346;

    Javabot() throws JDOMException, IOException {
        setName("javabot");
        setLogin("javabot");
        setVersion("Javabot 1.5 by Ricky Clarkso (ricky_clarkson@yahoo.com) and "
            + "various contributors, based on PircBot by Paul Mutton");
        loadConfig();
    }

    private void loadConfig() throws JDOMException, IOException {
        SAXBuilder reader = new SAXBuilder(true);
        Document document = reader.build(new File("config.xml"));
        Element root = document.getRootElement();
        String verbosity = root.getAttributeValue("verbose");
        setVerbose("true".equals(verbosity));
        _database = new HashMapDatabase(root);
        loadServerInfo(root);
        loadJavadocInfo(root);
        loadDictInfo(root);
        loadChannelInfo(root);
        loadAuthenticationInfo(root);
        loadStartStringInfo(root);
        loadOperationInfo(root);
        loadIgnoreInfo(root);
        log.debug("finished loading");
    }

    private void loadIgnoreInfo(Element root) {
        List ignoreNodes = root.getChildren("ignore");
        Iterator iterator = ignoreNodes.iterator();
        while(iterator.hasNext()) {
            Element node = (Element)iterator.next();
            ignores.add(node.getAttributeValue("name"));
        }
    }

    private void loadOperationInfo(Element root) {
        List operationNodes = root.getChildren("operation");
        Iterator iterator = operationNodes.iterator();
        operations = new ArrayList<BotOperation>();
        int index = -1;
        while(iterator.hasNext()) {
            index++;
            Element node = (Element)iterator.next();
            final String className = node.getAttributeValue("class");
            try {
                Class operationClass = Class.forName
                    (node.getAttributeValue("class"));
                if(AddFactoidOperation.class.equals(operationClass)) {
                    operations.add(new AddFactoidOperation(_database));
                } else if(DictOperation.class.equals(operationClass)) {
                    operations.add(new DictOperation(dictHost));
                } else if(ForgetFactoidOperation.class.equals(operationClass)) {
                    operations.add(new ForgetFactoidOperation(_database));
                    Debug.printDebug();
                } else if(GetFactoidOperation.class.equals(operationClass)) {
                    operations.add(new GetFactoidOperation(_database));
                } else if(GuessOperation.class.equals(operationClass)) {
                    operations.add(new GuessOperation(_database));
                } else if(JavadocOperation.class.equals(operationClass)) {
                    operations.add(new JavadocOperation(javadocSources, javadocBaseUrl));
                } else if(KarmaChangeOperation.class.equals(operationClass)) {
                    operations.add(new KarmaChangeOperation(_database));
                } else if(KarmaReadOperation.class.equals(operationClass)) {
                    operations.add(new KarmaReadOperation(_database));
                } else if(LeaveOperation.class.equals(operationClass)) {
                    operations.add(new LeaveOperation(this));
                } else if(ListFactoidsOperation.class.equals(operationClass)) {
                    operations.add(new ListFactoidsOperation(_database));
                } else if(LiteralOperation.class.equals(operationClass)) {
                    operations.add(new LiteralOperation(_database));
                } else if(QuitOperation.class.equals(operationClass)) {
                    operations.add(new QuitOperation(getNickPassword()));
                } else if(SpecialCasesOperation.class.equals(operationClass)) {
                    operations.add(new SpecialCasesOperation(this));
                } else if(StatsOperation.class.equals(operationClass)) {
                    operations.add(new StatsOperation(_database));
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
        Iterator iterator = channelNodes.iterator();
        while(iterator.hasNext()) {
            Element node = (Element)iterator.next();
            _channels.add(node.getAttributeValue("name"));
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
                ("The config file must supply a reference-xml " +
                    "attribute, as per the config.xml.sample file.");
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

    private void connect() {
        boolean connected = false;
        while(!connected) {
            try {
                connect(host, port);
                sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
                sleep(authWait);
                Iterator iterator = _channels.iterator();
                while(iterator.hasNext()) {
                    joinChannel
                        ((String)iterator.next());
                }
                connected = true;
            } catch(Exception exception) {
                log.error(exception.getMessage(), exception);
            }
            sleep(1000);
        }
    }

    public void onMessage(String channel, String sender, String login,
        String hostname, String message) {
        if(isValidSender(sender)) {
            for(int a = 0; a < startStrings.length; a++) {
                int length = startStrings[a].length();
                if(message.startsWith(startStrings[a])) {
                    handleAnyMessage(channel, sender, login, hostname,
                        message.substring(length).trim());
                    return;
                }
            }
            handleAnyChannelMessage(channel, sender, login, hostname, message);
        } else {
            log.info("ignoring " + sender);
        }
    }

    public List getResponses(String channel, String sender, String login, String hostname,
        String message) {
        for(BotOperation operation : operations) {
            List messages = operation.handleMessage(new BotEvent(channel, sender, login,
                hostname, message));
            if(messages.size() != 0) {
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
            if(messages.size() != 0) {
                return messages;
            }
        }
        return null;
    }

    private void handleAnyMessage(String channel, String sender, String login,
        String hostname, String message) {
        List messages = getResponses(channel, sender, login, hostname, message);
        if(messages != null) {
            Iterator iterator = messages.iterator();
            while(iterator.hasNext()) {
                Message nextMessage = (Message)iterator.next();
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
            Iterator iterator = messages.iterator();
            while(iterator.hasNext()) {
                Message nextMessage = (Message)iterator.next();
                if(nextMessage.isAction()) {
                    sendAction(nextMessage.getDestination(), nextMessage.getMessage());
                } else {
                    sendMessage(nextMessage.getDestination(), nextMessage.getMessage());
                }
            }
        }
    }

    public void onInvite(String targetNick, String sourceNick, String sourceLogin,
        String sourceHostname, String channel) {
        if(_channels.contains(channel)) {
            joinChannel(channel);
        }
    }

    public void onDisconnect() {
        connect();
    }

    public String getPreviousMessage(String channel) {
        if(channelPreviousMessages.containsKey(channel)) {
            return (String)channelPreviousMessages.get(channel);
        }
        return "";
    }

    public boolean isOnSameChannelAs(String nick) {
        String[] channels = getChannels();
        for(int a = 0; a < channels.length; a++) {
            if(userIsOnChannel(nick, channels[a])) {
                return true;
            }
        }
        return false;
    }

    public boolean userIsOnChannel(String nick, String channel) {
        User[] users = getUsers(channel);
        for(int a = 0; a < users.length; a++) {
            if
                (
                users[a].getNick().toLowerCase()
                    .equals(nick.toLowerCase())
                ) {
                return true;
            }
        }
        return false;
    }

    public void onPrivateMessage
        (String sender,
            String login,
            String hostname,
            String message) {
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

    public void setNickPassword(String password) {
        this.password = password;
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
}
