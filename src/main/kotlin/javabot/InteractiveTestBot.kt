package javabot

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.jayway.awaitility.Awaitility
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.EventDao
import javabot.dao.LogsDao
import javabot.dao.ShunDao
import javabot.model.Channel
import javabot.operations.throttle.Throttler
import javabot.web.JavabotApplication
import javax.inject.Provider
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InteractiveTestBot
@Inject
constructor(
    injector: Injector,
    configDao: ConfigDao,
    channelDao: ChannelDao,
    logsDao: LogsDao,
    shunDao: ShunDao,
    eventDao: EventDao,
    throttler: Throttler,
    adapter: IrcAdapter,
    adminDao: AdminDao,
    javabotConfig: JavabotConfig,
    application: Provider<JavabotApplication>
) :
    Javabot(
        injector,
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
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Javabot::class.java)

        @JvmStatic
        fun main() {
            val injector = Guice.createInjector(InteractiveJavabotModule())
            if (LOG.isInfoEnabled) {
                LOG.info("Starting Javabot")
            }
            val bot = injector.getInstance(InteractiveTestBot::class.java)
            bot.start()
            Awaitility.await().forever().until<Boolean> { !bot.isRunning() }
        }
    }

    override fun start() {
        channelDao.findAll().forEach { channelDao.delete(it) }
        channelDao.save(Channel("#test-jb"))
        super.start()
    }
}

class InteractiveJavabotModule : JavabotModule() {
    override fun configure() {
        super.configure()
        ircAdapterProvider = binder().getProvider(IrcAdapter::class.java)
    }

    override fun getBotNick(): String {
        return "test-jb"
    }
}
