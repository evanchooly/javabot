package javabot

import jakarta.inject.Inject
import jakarta.inject.Provider
import jakarta.inject.Singleton
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.EventDao
import javabot.dao.LogsDao
import javabot.dao.ShunDao
import javabot.model.JavabotUser
import javabot.operations.throttle.Throttler
import javabot.web.JavabotApplication

@Singleton
class TestJavabot
@Inject
constructor(
    configDao: ConfigDao,
    channelDao: ChannelDao,
    logsDao: LogsDao,
    shunDao: ShunDao,
    adminDao: AdminDao,
    eventDao: EventDao,
    throttler: Throttler,
    adapter: IrcAdapter,
    javabotConfig: JavabotConfig,
    application: Provider<JavabotApplication>
) :
    Javabot(
        configDao,
        channelDao,
        logsDao,
        shunDao,
        eventDao,
        throttler,
        adapter,
        adminDao,
        javabotConfig,
        application
    ) {

    override val nick: String = BaseTest.TEST_BOT_NICK

    override fun isOnCommonChannel(user: JavabotUser): Boolean {
        return true
    }

    override fun getUser(nick: String): JavabotUser {
        return JavabotUser(nick)
    }
}
