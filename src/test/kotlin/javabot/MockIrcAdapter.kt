package javabot

import jakarta.inject.Inject
import jakarta.inject.Provider
import jakarta.inject.Singleton
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
import javabot.model.Channel
import javabot.model.JavabotUser
import org.pircbotx.PircBotX

@Singleton
class MockIrcAdapter
@Inject
constructor(
    var messages: Messages,
    nickServDao: NickServDao,
    logsDao: LogsDao,
    channelDao: ChannelDao,
    adminDao: AdminDao,
    javabot: Provider<Javabot>,
    configDao: ConfigDao,
    ircBot: Provider<PircBotX>
) : OfflineAdapter(nickServDao, logsDao, channelDao, adminDao, javabot, configDao, ircBot) {
    val channels: MutableMap<String, MutableSet<String>> =
        mutableMapOf("#jbunittest" to mutableSetOf("botuser"))

    override fun startBot() {}

    override fun isConnected(): Boolean {
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

    override fun send(channel: Channel, value: String) {
        messages.add(value)
    }

    override fun action(channel: Channel, message: String) {
        throw UnsupportedOperationException("action")
    }

    override fun joinChannel(channel: Channel) {}

    override fun leave(channel: Channel, user: JavabotUser) {}

    override fun isOnCommonChannel(user: JavabotUser): Boolean {
        throw UnsupportedOperationException("isOnCommonChannel")
    }

    override fun message(target: String, message: String) {
        messages.add(message)
    }

    private val disabledOperations: MutableMap<String, Boolean> = mutableMapOf()

    fun disableOperation(op: String) {
        disabledOperations[op] = true
    }

    fun resetDisabledOperations() {
        disabledOperations.clear()
    }

    override fun isOp(nick: String, channelName: String): Boolean {
        if (disabledOperations["isOp"] == true) {
            disabledOperations.remove("isOp")
            return false
        }
        return (channels[channelName] ?: mutableSetOf()).contains(nick)
    }

    override fun isChannel(channelName: String): Boolean {
        if (disabledOperations["isChannel"] == true) {
            disabledOperations.remove("isChannel")
            return false
        }
        return channels.containsKey(channelName)
    }

    override fun isOnChannel(channel: String, nick: String): Boolean {
        if (disabledOperations["isOnChannel"] == true) {
            disabledOperations.remove("isOnChannel")
            return false
        }

        return (channels[channel] ?: mutableSetOf()).contains(nick)
    }
}
