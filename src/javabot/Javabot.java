package javabot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import com.rickyclarkson.java.util.TypeSafeList;
import com.rickyclarkson.xml.DOMSimple;
import com.rickyclarkson.xml.XMLUtility;
import javabot.operations.BotOperation;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Javabot extends PircBot {
    Map map = new HashMap();
    Map channelPreviousMessages = new HashMap();
    BotOperation[] operations;
    private String host, dictHost;
    private int port;
    private String factoidFilename;
    private String javadocSources, javadocBaseUrl;
    private String[] startStrings = null;
    private int authWait;
    private String password;
    private List channels = new TypeSafeList(new ArrayList(), String.class);
    private String htmlFile;
    private List ignores = new ArrayList();

    private Javabot() {
        setName("javabot");
        setLogin("javabot");
        setVersion("Javabot 1.4 by Ricky Clarkson"
            + " (ricky_clarkson@yahoo.com) and various"
            + " contributors, based on PircBot by Paul Mutton");
        Document document = XMLUtility.xmlToDom("config.xml");
        Node javabotNode =
            DOMSimple.getChildElementNode(document, "javabot");
        String verbosity = DOMSimple.getAttribute(javabotNode, "verbose");
        setVerbose(!"false".equals(verbosity));
        Node serverNode =
            DOMSimple.getChildElementNode(javabotNode, "server");
        host = DOMSimple.getAttribute(serverNode, "name");
        port = Integer.parseInt
            (DOMSimple.getAttribute(serverNode, "port"));
        Node javadocNode =
            DOMSimple.getChildElementNode(javabotNode, "javadoc");
        javadocSources =
            DOMSimple.getAttribute(javadocNode, "source-list");
        javadocBaseUrl = DOMSimple.getAttribute(javadocNode, "base-url");
        Node factoidsNode =
            DOMSimple.getChildElementNode(javabotNode, "factoids");
        factoidFilename =
            DOMSimple.getAttribute(factoidsNode, "filename");
        htmlFile = DOMSimple.getAttribute(factoidsNode, "htmlfilename");
        Node dictNode =
            DOMSimple.getChildElementNode(javabotNode, "dict");
        dictHost = DOMSimple.getAttribute(dictNode, "host");
        Node[] channelNodes =
            DOMSimple.getChildElementNodes(javabotNode, "channel");
        for(int a = 0; a < channelNodes.length; a++) {
            channels.add(DOMSimple.getAttribute(channelNodes[a], "name"));
        }
        Node[] authNodes =
            DOMSimple.getChildElementNodes(javabotNode, "auth");
        for(int a = 0; a < authNodes.length; a++) {
            authWait = Integer.parseInt
                (DOMSimple.getAttribute(authNodes[a], "wait"));
            setNickPassword
                (DOMSimple.getAttribute(authNodes[a], "password"));
        }
        Node[] nickNodes =
            DOMSimple.getChildElementNodes(javabotNode, "nick");
        if(nickNodes.length > 0) {
            setName(DOMSimple.getAttribute(nickNodes[0], "name"));
        }
        Node[] startNodes =
            DOMSimple.getChildElementNodes(javabotNode, "message");
        startStrings = new String[startNodes.length];
        for(int a = 0; a < startNodes.length; a++) {
            startStrings[a] =
                DOMSimple.getAttribute(startNodes[a], "tag");
        }
        Node[] operationNodes = DOMSimple.getChildElementNodes(javabotNode, "operation");
        operations = new BotOperation[operationNodes.length];
        for(int a = 0; a < operationNodes.length; a++) {
            try {
                Class operationClass = Class.forName
                    (DOMSimple.getAttribute
                    (operationNodes[a], "class"));
                operations[a] = (BotOperation)operationClass
                    .newInstance();
                System.out.println(operations[a]);
            } catch(Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        Node[] ignoreNodes =
            DOMSimple.getChildElementNodes(javabotNode, "ignore");
        for(int a = 0; a < ignoreNodes.length; a++) {
            ignores.add(DOMSimple.getAttribute(ignoreNodes[a], "name"));
        }
        loadFactoids();
    }

    public static void main(String[] args) {
        System.out.println("Starting Javabot");
        Javabot bot = new Javabot();
        bot.setMessageDelay(2000);
        bot.connect();
    }

    private void connect() {
        while(true) {
            boolean noException = true;
            try {
                connect(host, port);
            } catch(Exception exception) {
                exception.printStackTrace();
                noException = false;
            }
            sendRawLine
                ("PRIVMSG NickServ :identify " +
                getNickPassword());
            try {
                Thread.sleep(authWait);
            } catch(InterruptedException exception) {
                throw new RuntimeException(exception);
            }
            Iterator iterator = channels.iterator();
            while(iterator.hasNext()) {
                joinChannel((String)iterator.next());
            }
            if(noException) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch(InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public void onMessage
        (String channel,
        String sender,
        String login,
        String hostname,
        String message) {
        String[] startStrings =
            {"~", "javabot: ", "javabot, ", "javabot "};
        for(int a = 0; a < startStrings.length; a++) {
            int length = startStrings[a].length();
            if(message.startsWith(startStrings[a])) {
                handleAnyMessage
                    (channel,
                        sender,
                        login,
                        hostname,
                        message.substring(length).trim());
                return;
            }
        }
    }

    public List getResponses
        (String channel,
        String sender,
        String login,
        String hostname,
        String message) {
        for(int a = 0; a < operations.length; a++) {
            List messages = operations[a].handleMessage
                (new BotEvent
                    (this,
                        channel,
                        sender,
                        login,
                        hostname,
                        message));
            if(messages.size() != 0) {
                return messages;
            }
        }
        return null;
    }

    private void handleAnyMessage
        (String channel,
        String sender,
        String login,
        String hostname,
        String message) {
        List messages = getResponses
            (channel, sender, login, hostname, message);
        if(messages == null) {
            return;
        }
        Iterator iterator = messages.iterator();
        while(iterator.hasNext()) {
            Message nextMessage = (Message)iterator.next();
            if(nextMessage.isAction()) {
                sendAction
                    (nextMessage.getDestination(),
                        nextMessage.getMessage());
            } else {
                sendMessage
                    (nextMessage.getDestination(),
                        nextMessage.getMessage());
            }
        }
        channelPreviousMessages.put(channel, message);
    }

    public void onPrivateMessage
        (String sender, String login, String hostname, String message) {
        handleAnyMessage(sender, sender, login, hostname, message);
    }

    public void onInvite
        (String targetNick,
        String sourceNick,
        String sourceLogin,
        String sourceHostname,
        String channel) {
        if(channels.contains(channel)) {
            joinChannel(channel);
        }
    }

    public void onDisconnect() {
        connect();
    }

    public void addFactoid(String key, String value) {
        map.put(key, value);
        saveFactoids();
    }

    public boolean hasFactoid(String key) {
        return map.containsKey(key);
    }

    public String getFactoid(String key) {
        return (String)map.get(key);
    }

    public void forgetFactoid(String key) {
        map.remove(key);
        saveFactoids();
    }

    public Map getMap() {
        return map;
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
            if(users[a].getNick().toLowerCase().equals(nick.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void saveFactoids() {
        try {
            FileOutputStream fos = new FileOutputStream(factoidFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
            fos.close();
            dumpHTML();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private void dumpHTML() {
        Iterator iterator = new TreeSet(map.keySet()).iterator();
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(htmlFile));
            writer.println(new StringBuffer()
                .append("<html><body><table ")
                .append("border=\"1\"><tr><th>")
                .append("factoid</th><th>value</th></tr>")
                .toString());
            while(iterator.hasNext()) {
                String factoid = (String)iterator.next();
                String value = (String)map.get(factoid);
                value = value.replaceAll("<", "&lt;");
                value = value.replaceAll(">", "&gt;");
                writer.println("<tr><td>" + factoid
                    + "</td><td>" + value + "</td></tr>");
            }
            writer.println("</table></body></html>");
            writer.flush();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFactoids() {
        try {
            FileInputStream fis =
                new FileInputStream(factoidFilename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (Map)ois.readObject();
            ois.close();
            fis.close();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        Set keySet = new HashSet(map.keySet());
        Iterator iterator = keySet.iterator();
        while(iterator.hasNext()) {
            String next = (String)iterator.next();
            if(!next.equals(next.toLowerCase())) {
                String value = (String)map.get(next);
                map.remove(next);
                map.put(next.toLowerCase(), value);
            }
        }
    }

    /**
     * @return the number of factoids that this bot stores.
     */
    public int getNumberOfFactoids() {
        return map.size();
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

    public boolean isValidSender(String sender) {
        return ignores.contains(sender);
    }

    public void addIgnore(String sender) {
        ignores.add(sender);
    }
}
