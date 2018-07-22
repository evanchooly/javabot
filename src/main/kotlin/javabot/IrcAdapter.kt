package javabot


import com.antwerkz.sofia.Sofia
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.Logs.Type
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.pircbotx.exception.DaoException
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.ActionEvent
import org.pircbotx.hooks.events.ConnectEvent
import org.pircbotx.hooks.events.InviteEvent
import org.pircbotx.hooks.events.JoinEvent
import org.pircbotx.hooks.events.KickEvent
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.hooks.events.NickChangeEvent
import org.pircbotx.hooks.events.NoticeEvent
import org.pircbotx.hooks.events.PartEvent
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.pircbotx.hooks.events.QuitEvent
import org.slf4j.LoggerFactory
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Provider

open class IrcAdapter @Inject
constructor(private var nickServDao: NickServDao, private var logsDao: LogsDao, private var channelDao: ChannelDao,
            private var adminDao: AdminDao, private var javabotProvider: Provider<Javabot>, private var configDao: ConfigDao,
            private var ircBot: Provider<PircBotX>) : ListenerAdapter() {

    companion object {
        private val LOG = LoggerFactory.getLogger(IrcAdapter::class.java)
    }

    private val nickServ = ArrayList<String>()

    override fun onMessage(event: MessageEvent) {
        val bot = javabotProvider.get()
        val channel = event.channel.toJavabot()
        val user = event.user!!.toJavabot()
        bot.executors.execute {
            bot.processMessage(Message.extractContentFromMessage(bot, channel, user, bot.startString, bot.nick, event.message))
        }
    }

    override fun onPrivateMessage(event: PrivateMessageEvent) {
        val bot = javabotProvider.get()
        var start = ""
        for (startString in arrayOf(bot.startString, bot.nick)) {
            if (event.message.startsWith(startString)) {
                start = startString
            }
        }

        bot.executors.execute({
            bot.processMessage(Message.extractContentFromMessage(bot, null, event.user!!.toJavabot(), start, bot.nick, event.message))
        })
    }

    override fun onJoin(event: JoinEvent) {
        logsDao.logMessage(Logs.Type.JOIN, event.channel.toJavabot(), event.user?.toJavabot(),
                Sofia.userJoined(event.user?.nick!!, event.user?.hostmask!!, event.channel.name))
    }

    override fun onPart(event: PartEvent) {
        logsDao.logMessage(Logs.Type.PART, event.channel.toJavabot(), event.user.toJavabot(),
                Sofia.userParted(event.user.nick, event.reason))
        nickServDao.unregister(event.user.toJavabot())
    }

    override fun onQuit(event: QuitEvent) {
        logsDao.logMessage(Logs.Type.QUIT, null, event.user.toJavabot(), Sofia.userQuit(event.user.nick, event.reason))
        nickServDao.unregister(event.user.toJavabot())
    }

    override fun onInvite(event: InviteEvent) {
        val channel = channelDao.get(event.channel)
        if (channel != null) {
            if (channel.key == null) {
                ircBot.get().sendIRC().joinChannel(channel.name)
            } else {
                ircBot.get().sendIRC().joinChannel(channel.name, channel.key)
            }
        } else if (adminDao.count() == 0L) {
            channelDao.create(event.channel, true, null)
            ircBot.get().sendIRC().joinChannel(event.channel)
        }
    }

    override fun onConnect(event: ConnectEvent) {
        nickServDao.clear()
        event.getBot<PircBotX>().sendIRC().message("nickserv", "identify " + configDao.get().password)
    }

    override fun onNotice(event: NoticeEvent) {
        if (event.userHostmask.nick.equals("NickServ", ignoreCase = true)) {
            synchronized(nickServ) {
                val message = event.notice.replace("\u0002", "")
                if (message == "*** End of Info ***" && !nickServ.isEmpty()) {
                    nickServDao.process(nickServ)
                    nickServ.clear()
                } else {
                    LOG.debug(message)
                    if (message.startsWith("Information on ") || !nickServ.isEmpty()) {
                        nickServ.add(message)
                    }
                }
            }
        }
    }

    override fun onNickChange(event: NickChangeEvent) {
        logsDao.logMessage(Type.NICK, null, event.user?.toJavabot(), Sofia.userNickChanged(event.oldNick, event.newNick))
        nickServDao.updateNick(event.oldNick, event.newNick)
    }

    override fun onAction(event: ActionEvent) {
        logsDao.logMessage(Logs.Type.ACTION, event.channel?.toJavabot(), event.user?.toJavabot(), event.message)
    }

    override fun onKick(event: KickEvent) {
        logsDao.logMessage(Logs.Type.KICK, event.channel.toJavabot(), event.user?.toJavabot(),
                " kicked %s (%s)".format(event.recipient?.nick, event.reason))
    }

    open fun isOnChannel(channel: String, nick: String): Boolean {
        val userChannelDao = ircBot.get().userChannelDao
        return try {
            val user=userChannelDao.getUser(nick)
            user.channels.contains(userChannelDao.getChannel(channel))
        } catch(u:DaoException) {
            false
        }
    }

    open fun isOp(nick: String, channelName: String): Boolean {
        val userChannelDao = ircBot.get().userChannelDao
        return try {
            val user = userChannelDao.getUser(nick)
            val userChannels = user.channelsOpIn
            if (userChannels != null) {
                val channel = userChannelDao.getChannel(channelName)
                userChannels.contains(channel)
            } else {
                false
            }
        } catch (u: DaoException) {
            // mostly for testing, as the nick shouldn't be generated by anything other than the IRC framework
            false
        }
    }

    open fun isChannel(channelName: String): Boolean {
        val userChannelDao = ircBot.get().userChannelDao
        return try {
            userChannelDao.getChannel(channelName)
            true
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }

    open fun isOnCommonChannel(user: JavabotUser): Boolean {
        val target = ircBot.get().userChannelDao.getUser(user.nick)
        return !ircBot.get().userChannelDao.getChannels(target).isEmpty()
    }

    open fun send(channel: Channel, value: String) {
        channel.toIrcChannel()
                .send()
                .message(value)
    }

    open fun send(user: JavabotUser, value: String) {
        user.toIrcUser()
                .send()
                .message(value)

    }

    open fun action(channel: Channel, message: String) {
        channel.toIrcChannel()
                .send()
                .action(message)
    }

    open fun joinChannel(channel: Channel) {
        if (channel.key != null) {
            ircBot.get().sendIRC().joinChannel(channel.name, channel.key)
        } else {
            ircBot.get().sendIRC().joinChannel(channel.name)
        }
    }

    open fun message(target: String, message: String) {
        ircBot.get().sendIRC().message(target, message)
    }

    open fun getUser(nick: String): JavabotUser {
        return ircBot.get().userChannelDao.getUser(nick).toJavabot()
    }

    open fun isConnected(): Boolean {
        return ircBot.get().isConnected
    }

    open
    fun isBotOnChannel(name: String): Boolean {
        return ircBot.get().userChannelDao.containsChannel(name)
    }

    open fun startBot() {
        ircBot.get().startBot()
    }

    open fun leave(channel: Channel, user: JavabotUser) {
        channel.toIrcChannel()
                .send()
                .part(Sofia.channelDeleted(user.nick))
    }

    fun JavabotUser.toIrcUser(): User {
        return ircBot.get().userChannelDao.getUser(nick)
    }

    fun Channel.toIrcChannel(): org.pircbotx.Channel {
        return ircBot.get().userChannelDao.getChannel(name)
    }

    fun org.pircbotx.Channel.toJavabot(): Channel? {
        return channelDao.get(name)
    }

    fun User.toJavabot(): JavabotUser {
        return JavabotUser(nick, login, hostmask)
    }
}
