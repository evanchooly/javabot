package javabot

import jakarta.enterprise.inject.Typed
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
import org.slf4j.LoggerFactory

// @Typed restricts this bean's exposed CDI bean types to OfflineAdapter itself -- without it,
// CDI's default behavior of exposing every supertype as a bean type would make OfflineAdapter
// ALSO match every `IrcAdapter`-typed injection point (Javabot's `adapter` field,
// IrcBotProducer's `ircAdapter` param), creating an AmbiguousResolutionException against the
// real IrcAdapter bean. Guice never had this problem: it resolves purely by the requested type,
// not by "is-a" relationships across concrete subclasses. OfflineAdapter has no direct CDI
// consumer of its own -- its only real consumer is the test-only javabot.mocks.MockIrcAdapter,
// which extends it and is itself a CDI @Alternative @Priority(1) @Singleton bean (see
// MockIrcAdapter.kt), not Guice-constructed.
@Singleton
@Typed(OfflineAdapter::class)
open class OfflineAdapter
@Inject
constructor(
    nickServDao: NickServDao,
    logsDao: LogsDao,
    channelDao: ChannelDao,
    adminDao: AdminDao,
    javabot: Provider<Javabot>,
    configDao: ConfigDao,
    ircBot: Provider<PircBotX>,
) : IrcAdapter(nickServDao, logsDao, channelDao, adminDao, javabot, configDao, ircBot) {

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

    override fun send(channel: Channel, value: String) {
        log(value)
    }

    override fun message(target: String, message: String) {
        log(message)
    }

    open protected fun log(value: String) {
        LOG.info(value)
    }
}
