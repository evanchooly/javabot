package javabot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javabot.dao.ChannelDao;
import javabot.operations.BotOperation;
import org.schwering.irc.lib.IRCEventAdapter;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class OperationsDispatchListener extends IRCEventAdapter {
    private static final Logger log = LoggerFactory.getLogger(OperationsDispatchListener.class);
    private final ExecutorService executors;
    @Autowired
    private ChannelDao channelDao;
    private final Javabot bot;

    public OperationsDispatchListener(final Javabot javabot) {
        bot = javabot;
        executors = new ThreadPoolExecutor(15, 40, 10L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
            new JavabotThreadFactory(true, "javabot-handler-thread-"));
    }
//    @Override
//    public void onMessage(final String channel, final String sender, final String login, final String hostname,
//        final String message) {
//        executors.execute(new Runnable() {
//            @Override
//            public void run() {
//                processMessage(channel, message, sender, login, hostname);
//            }
//        });
//    }

    @Override
    public void onPrivmsg(final String channel, final IRCUser user, final String message) {
        if (bot.isOnSameChannelAs(user)) {
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    for (final Message response : getResponses(channel, user, message)) {
                        response.send(bot);
                    }
                }
            });
        }
    }

    public void onDisconnect() {
        if (!executors.isShutdown()) {
            bot.connect();
        }
    }

    public void processMessage(final String channel, final String message, final IRCUser sender) {
        try {
            if (bot.isValidSender(sender)) {
                final List<Message> responses = new ArrayList<Message>();
                for (final String startString : bot.getStartStrings()) {
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
                    response.send(bot);
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

    @Override
    public void onInvite(final String channel, final IRCUser user, final String passiveNick) {
        if (channelDao.get(channel) != null) {
//            bot.joinChannel(channel);
        }
    }

    public Iterator<BotOperation> getOperations() {
        return bot.getOperations();
    }

    public List<Message> getResponses(final String channel, final IRCUser sender, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        final IrcEvent event = new IrcEvent(channel, sender, message);
        while (responses.isEmpty() && iterator.hasNext()) {
            final BotOperation next = iterator.next();
            responses.addAll(next.handleMessage(event));
        }
        if(message.contains("shortcut")) {
            log.debug("message = " + message);
            final Iterator<BotOperation> it = getOperations();
            while(it.hasNext()) {
                log.debug(it.next().toString());
            }
            log.debug("responses = " + responses);
        }
        return responses;
    }

    public List<Message> getChannelResponses(final String channel, final IRCUser sender, final String message) {
        final Iterator<BotOperation> iterator = getOperations();
        final List<Message> responses = new ArrayList<Message>();
        while (responses.isEmpty() && iterator.hasNext()) {
            responses.addAll(iterator.next()
                    .handleChannelMessage(new IrcEvent(channel, sender, message)));
        }
        return responses;
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
}