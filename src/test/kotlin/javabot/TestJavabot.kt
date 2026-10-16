package javabot

import jakarta.annotation.Priority
import jakarta.enterprise.inject.Alternative
import jakarta.inject.Inject
import jakarta.inject.Singleton
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.LogsDao
import javabot.dao.ShunDao
import javabot.model.EventInjector
import javabot.model.JavabotUser
import javabot.operations.throttle.Throttler

// @Alternative + @Priority, same reasoning as MockIrcAdapter/TestNickServDao: without it, every
// BotOperation/AdminCommand constructor parameter typed `bot: Javabot` becomes an
// AmbiguousResolutionException the moment TestJavabot is itself CDI-discoverable (it IS-A
// Javabot, so both beans match). The enabled alternative wins that resolution, giving every
// Javabot-typed injection point the same TestJavabot instance under %test -- the CDI-native
// equivalent of JavabotTestModule's old `@Provides fun getJavabot(): Javabot` override.
@Alternative
@Priority(1)
@Singleton
class TestJavabot
@Inject
constructor(
    eventInjector: EventInjector,
    configDao: ConfigDao,
    channelDao: ChannelDao,
    logsDao: LogsDao,
    shunDao: ShunDao,
    adminDao: AdminDao,
    throttler: Throttler,
    adapter: IrcAdapter,
    javabotConfig: JavabotConfig,
) :
    Javabot(
        eventInjector,
        configDao,
        channelDao,
        logsDao,
        shunDao,
        throttler,
        adapter,
        adminDao,
        javabotConfig,
    ) {

    override val nick: String = BaseTest.TEST_BOT_NICK

    override fun isOnCommonChannel(user: JavabotUser): Boolean {
        return true
    }

    override fun getUser(nick: String): JavabotUser {
        return JavabotUser(nick)
    }
}
