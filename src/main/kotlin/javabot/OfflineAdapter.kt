package javabot

import com.google.inject.Inject
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
import javabot.model.Channel
import javabot.model.JavabotUser
import org.pircbotx.PircBotX
import org.slf4j.LoggerFactory
import javax.inject.Provider

open class OfflineAdapter @Inject
constructor(nickServDao: NickServDao, logsDao: LogsDao, channelDao: ChannelDao, adminDao: AdminDao,
            javabot: Provider<Javabot>, configDao: ConfigDao, ircBot: Provider<PircBotX>) :
        IrcAdapter(nickServDao, logsDao, channelDao, adminDao, javabot, configDao, ircBot) {

    companion object {
        val LOG = LoggerFactory.getLogger("offline")
    }

    override fun action(channel: Channel, message: String) {
        throw UnsupportedOperationException("action")
    }

    override fun joinChannel(channel: Channel) {
        throw UnsupportedOperationException("joinChannel")
    }

    override fun leave(channel: Channel, user: JavabotUser) {
        throw UnsupportedOperationException("leave")
    }

    override fun isOnCommonChannel(user: JavabotUser): Boolean {
        throw UnsupportedOperationException("isOnCommonChannel")
    }

    override fun send(user: JavabotUser, value: String) {
        log(value)
    }

    override
    fun send(channel: Channel, value: String) {
        log(value)
    }

    override fun message(target: String, message: String) {
        log(message)
    }

    open protected fun log(value: String) {
        LOG.info(value)
    }
}