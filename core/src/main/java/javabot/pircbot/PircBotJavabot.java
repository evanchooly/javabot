package javabot.pircbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javabot.ChannelList;
import javabot.Channels;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.JavabotThreadFactory;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.LogsDao;
import javabot.dao.ShunDao;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.operations.BotOperation;
import org.jibble.pircbot.PircBot;
import org.schwering.irc.lib.IrcUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class PircBotJavabot extends Javabot {
    static final Logger log = LoggerFactory.getLogger(PircBotJavabot.class);
    private PircBot pircBot;
    private String host;
    private int port;
    private String[] startStrings;
    private int authWait;
    private final List<String> ignores = new ArrayList<String>();
    @Autowired
    LogsDao logsDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private ShunDao shunDao;
    private ApplicationContext context;
    protected final ExecutorService executors;
    public static final int THROTTLE_TIME = 5 * 1000;
    protected Config config;
    private Map<String, BotOperation> operations;
    private String nick;
    private String password;
    final Channels channels = new Channels();

    @SuppressWarnings({"OverriddenMethodCallDuringObjectConstruction", "OverridableMethodCallDuringObjectConstruction"})
    public PircBotJavabot(final ApplicationContext applicationContext) {
        super(applicationContext);
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

    @SuppressWarnings({"StringContatenationInLoop"})
    public void connect() {
        pircBot = new EmbeddedPircBot(this);
        while (!pircBot.isConnected()) {
            try {
                pircBot.connect(host, port);
                pircBot.sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
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
                                channel.join(PircBotJavabot.this);
                            }
                        }).start();
                    }
                }
            } catch (Exception exception) {
                pircBot.disconnect();
                log.error(exception.getMessage(), exception);
            }
            sleep(1000);
        }
    }

    public void processMessage(final String channel, final IrcUser sender, final String message) {
        try {
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

    public List<Message> getResponses(final String channel, final IrcUser sender, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        final IrcEvent event = new IrcEvent(channel, sender, message);
        while (responses.isEmpty() && iterator.hasNext()) {
            responses.addAll(iterator.next().handleMessage(event));
        }
        return responses;
    }

    public List<Message> getChannelResponses(final String channel, final IrcUser sender, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        while (responses.isEmpty() && iterator.hasNext()) {
            final IrcUser user = null;
            responses.addAll(iterator.next()
                .handleChannelMessage(new IrcEvent(channel, sender, message)));
        }
        return responses;
    }

    @Override
    public boolean isOnSameChannelAs(final IrcUser user) {
        for (final String channel : pircBot.getChannels()) {
            if (userIsOnChannel(user, channel)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void partChannel(final String name) {
        pircBot.partChannel(name);
    }

    @Override
    public IrcUser getUser(final String name) {
        return channels.getUser(name);
    }

    @Override
    public boolean userIsOnChannel(final IrcUser IrcUser, final String channel) {
       return channels.get(channel).getUser(IrcUser.getNick()) != null;
    }

    public void setNickPassword(final String value) {
        password = value;
    }

    @Override
    public void joinChannel(final String channel) {
        pircBot.joinChannel(channel);
    }

    @Override
    public void joinChannel(final String channel, final String key) {
        pircBot.joinChannel(channel, key);
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

    @Override
    public void postMessage(final Message message) {
        logMessage(message);
        pircBot.sendMessage(message.getDestination(), message.getMessage());
    }

    @Override
    public void postAction(final Message message) {
        logMessage(message);
        pircBot.sendAction(message.getDestination(), message.getMessage());
    }

    protected final void logMessage(final Message message) {
        final IrcEvent event = message.getEvent();
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

    ChannelList getChannel(final String channel) {
        return channels.get(channel);
    }
}