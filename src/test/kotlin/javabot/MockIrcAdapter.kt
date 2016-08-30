package javabot

import com.google.inject.Inject
import com.google.inject.Singleton
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
import javabot.model.Channel
import javabot.model.JavabotUser
import org.pircbotx.PircBotX
import javax.inject.Provider

@Singleton
class MockIrcAdapter @Inject
constructor(var messages: Messages, nickServDao: NickServDao, logsDao: LogsDao, channelDao: ChannelDao, adminDao: AdminDao,
            javabot: Provider<Javabot>, configDao: ConfigDao, ircBot: Provider<PircBotX>) :
        OfflineAdapter(nickServDao, logsDao, channelDao, adminDao, javabot, configDao, ircBot) {

    override
    fun startBot() {
    }

    override
    fun isConnected(): Boolean {
        return true
    }

    override fun isBotOnChannel(name: String): Boolean {
        return true
    }

    override fun log(value: String) {
        messages.add(value)
    }

    override fun send(user: JavabotUser, value: String) {
        messages.add(value)
    }

    override
    fun send(channel: Channel, value: String) {
        messages.add(value)
    }

    override fun action(channel: Channel, message: String) {
        throw UnsupportedOperationException("action")
    }

    override
    fun joinChannel(channel: Channel) {
    }

    override fun leave(channel: Channel, user: JavabotUser) {
    }

    override fun isOnCommonChannel(user: JavabotUser): Boolean {
        throw UnsupportedOperationException("isOnCommonChannel")
    }

    override fun message(target: String, message: String) {
        messages.add(message)
    }
}