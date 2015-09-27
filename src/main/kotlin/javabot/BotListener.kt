package javabot

import com.antwerkz.sofia.Sofia
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
import javabot.model.Admin
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.operations.throttle.NickServViolationException
import javabot.operations.throttle.Throttler
import org.pircbotx.PircBotX
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

public class BotListener : ListenerAdapter<PircBotX>() {

    @Inject
    private lateinit val throttler: Throttler

    @Inject
    private lateinit val nickServDao: NickServDao

    @Inject
    private lateinit val logsDao: LogsDao

    @Inject
    private lateinit val channelDao: ChannelDao

    @Inject
    private lateinit val adminDao: AdminDao

    @Inject
    private lateinit val javabotProvider: Provider<Javabot>

    @Inject
    private lateinit val configDao: ConfigDao

    private val nickServ = ArrayList<String>()

    @Inject
    private lateinit val ircBot: Provider<PircBotX>

    public fun log(string: String) {
        if (Javabot.LOG.isInfoEnabled) {
            Javabot.LOG.info(string)
        }
    }

    override fun onMessage(event: MessageEvent<PircBotX>) {
        val javabot = javabotProvider.get()
        javabot.executors.execute({ javabot.processMessage(Message(event.channel, event.user, event.message)) })
    }

    override fun onJoin(event: JoinEvent<PircBotX>) {
        logsDao.logMessage(Logs.Type.JOIN, event.channel, event.user,
              Sofia.userJoined(event.user.nick, event.user.hostmask,
                    event.channel.name))
        if (adminDao.count() == 0L) {
            val users = getIrcBot().userChannelDao.getUsers(event.channel)
            var admin: Admin? = null
            val iterator = users.iterator()
            while (admin == null && iterator.hasNext()) {
                val user = iterator.next()
                if (user.nick != javabotProvider.get().getNick()) {
                    admin = adminDao.create(user.nick, user.login, user.hostmask)
                }
            }
        }
    }

    override fun onPart(event: PartEvent<PircBotX>) {
        logsDao.logMessage(Logs.Type.PART, event.channel, event.user, Sofia.userParted(event.user.nick, event.reason))
        nickServDao.unregister(event.user)
    }

    override fun onQuit(event: QuitEvent<PircBotX>) {
        logsDao.logMessage(Logs.Type.QUIT, null, event.user, Sofia.userQuit(event.user.nick, event.reason))
        nickServDao.unregister(event.user)
    }

    override fun onInvite(event: InviteEvent<PircBotX>) {
        val channel = channelDao.get(event.channel)
        if (channel != null) {
            if (channel.key == null) {
                ircBot.get().sendIRC().joinChannel(channel.name)
            } else {
                ircBot.get().sendIRC().joinChannel(channel.name, channel.key)
            }
        } else if (adminDao.count() == 0L) {
            channelDao.create(event.channel, true, null)
            getIrcBot().sendIRC().joinChannel(event.channel)
        }
    }

    override fun onConnect(event: ConnectEvent<PircBotX>) {
        nickServDao.clear()
        event.bot.sendIRC().message("nickserv", "identify " + configDao.get().password)
    }

    override fun onNotice(event: NoticeEvent<PircBotX>) {
        if (event.user.nick.equals("NickServ", ignoreCase = true)) {
            val message = event.notice.replace("\u0002", "")
            synchronized (nickServ) {
                if (message == "*** End of Info ***" && !nickServ.isEmpty()) {
                    try {
                        nickServDao.process(nickServ)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

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

    override fun onNickChange(event: NickChangeEvent<PircBotX>) {
        logsDao.logMessage(Type.NICK, null, event.user, Sofia.userNickChanged(event.oldNick, event.newNick))
        nickServDao.updateNick(event.oldNick, event.newNick)
    }

    override fun onPrivateMessage(event: PrivateMessageEvent<PircBotX>) {
        val javabot = javabotProvider.get()
        var startStringForPm = ""
        val message = event.message
        for (startString in javabot.startStrings) {
            if (message.startsWith(startString)) {
                startStringForPm = startString
            }
        }

        val content = javabot.extractContentFromMessage(message, startStringForPm)
        if (adminDao.isAdmin(event.user) || javabot.isOnCommonChannel(event.user)) {
            javabot.executors.execute({
                javabot.logMessage(null, event.user, event.message)
                try {
                    if (!throttler.isThrottled(event.user)) {
                        javabot.getResponses(Message(event.user, content), event.user)
                    }
                } catch (e: NickServViolationException) {
                    event.user.send().message(e.getMessage())
                }
            })
        }
    }

    override fun onAction(event: ActionEvent<PircBotX>) {
        logsDao.logMessage(Logs.Type.ACTION, event.channel, event.user, event.message)
    }

    override fun onKick(event: KickEvent<PircBotX>) {
        logsDao.logMessage(Logs.Type.KICK, event.channel, event.user,
              " kicked %s (%s)".format(event.recipient.nick, event.reason))
    }

    public fun getIrcBot(): PircBotX {
        return ircBot.get()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BotListener::class.java)
    }
}
