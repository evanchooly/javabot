package javabot.pircbot;

import javabot.ChannelList;
import javabot.Message;
import javabot.model.Channel;
import javabot.model.Logs;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

public class EmbeddedPircBot extends PircBot {
    private final PircBotJavabot javabot;

    public EmbeddedPircBot(final PircBotJavabot javabot) {
        this.javabot = javabot;
        setVersion(javabot.loadVersion());
        setName(javabot.config.getNick());
        setLogin(javabot.config.getNick());
    }

    @Override
    public void onMessage(final String channel, final String sender, final String login, final String hostname,
        final String message) {
        javabot.executors.execute(new Runnable() {
            @Override
            public void run() {
                javabot.processMessage(channel, javabot.getUser(sender), message);
            }
        });
    }

    @Override
    public void onPrivateMessage(final String sender, final String login, final String hostname,
        final String message) {
        if (javabot.isOnSameChannelAs(javabot.getUser(sender))) {
            //The bot always replies with a private message ...
            if (PircBotJavabot.log.isDebugEnabled()) {
                PircBotJavabot.log.debug("PRIVMSG Sender:" + sender + " Login" + login);
            }
            javabot.executors.execute(new Runnable() {
                @Override
                public void run() {
                    javabot.logsDao.logMessage(Logs.Type.MESSAGE, sender, sender, message);
                    for (final Message response : javabot.getResponses(sender, javabot.getUser(sender), message)) {
                        response.send(javabot);
                    }
                }
            });
        }
    }

    @Override
    public void onJoin(final String channel, final String sender, final String login, final String hostname) {
        ChannelList list = javabot.getChannel(channel);
        if(list == null) {
            list = javabot.channels.add(channel);
            final User[] users = getUsers(channel);
            for (final User user : users) {
                list.addUser(user.getNick());
            }
        }
        list.addUser(sender);
        if (javabot.channelDao.get(channel).getLogged()) {
            javabot.logsDao.logMessage(Logs.Type.JOIN, sender, channel, ":" + hostname + " joined the channel");
        }
    }

    @Override
    public void onQuit(final String channel, final String sender, final String login, final String hostname) {
        final Channel chan = javabot.channelDao.get(channel);
        if (chan != null && chan.getLogged()) {
            javabot.logsDao.logMessage(Logs.Type.QUIT, sender, channel, "quit");
        } else if (chan == null) {
            PircBotJavabot.log.debug("not logging " + channel);
        }
    }

    @Override
    public void onInvite(final String targetNick, final String sourceNick, final String sourceLogin,
        final String sourceHostname, final String channel) {
        if (PircBotJavabot.log.isDebugEnabled()) {
            PircBotJavabot.log.debug("Invited to " + channel + " by " + sourceNick);
        }
        if (channel.equals(javabot.channelDao.get(channel).getName())) {
            joinChannel(channel);
        }
    }

    @Override
    public void onDisconnect() {
        if (!javabot.executors.isShutdown()) {
            try {
                connect(javabot.getHost(), getPort());
            } catch (Exception e) {
                PircBotJavabot.log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void onPart(final String channel, final String sender, final String login, final String hostname) {
        final Channel chan = javabot.channelDao.get(channel);
        if (chan != null && chan.getLogged()) {
            javabot.logsDao.logMessage(Logs.Type.PART, sender, channel, "parted the channel");
        }
    }

    @Override
    public void onAction(final String sender, final String login, final String hostname, final String target,
        final String action) {
        if (PircBotJavabot.log.isDebugEnabled()) {
            PircBotJavabot.log.debug("Sender " + sender + " Message " + action);
        }
        if (javabot.channelDao.get(target).getLogged()) {
            javabot.logsDao.logMessage(Logs.Type.ACTION, sender, target, action);
        }
    }

    @Override
    public void onKick(final String channel, final String kickerNick, final String kickerLogin,
        final String kickerHostname, final String recipientNick, final String reason) {
        if (javabot.channelDao.get(channel).getLogged()) {
            javabot.logsDao
                .logMessage(Logs.Type.KICK, kickerNick, channel, " kicked " + recipientNick + " (" + reason + ")");
        }
    }

}
