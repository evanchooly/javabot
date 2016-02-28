package javabot

import com.google.inject.Injector
import com.google.inject.Singleton
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.EventDao
import javabot.dao.LogsDao
import javabot.dao.ShunDao
import javabot.kotlin.web.JavabotApplication
import javabot.operations.throttle.Throttler
import org.pircbotx.PircBotX
import org.pircbotx.User
import javax.inject.Inject
import javax.inject.Provider

@Singleton class TestJavabot @Inject
constructor(injector: Injector, configDao: ConfigDao, channelDao: ChannelDao, logsDao: LogsDao, shunDao: ShunDao,
            eventDao: EventDao, throttler: Throttler, ircBot: Provider<PircBotX>, javabotConfig: JavabotConfig,
            application: Provider<JavabotApplication>) :
        Javabot(injector, configDao, channelDao, logsDao, shunDao, eventDao, throttler, ircBot, javabotConfig, application) {

    override fun getNick(): String {
        return BaseTest.TEST_BOT_NICK
    }

    override fun isOnCommonChannel(user: User): Boolean {
        return true
    }
}
