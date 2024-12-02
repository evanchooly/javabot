package javabot

import com.jayway.awaitility.Awaitility
import jakarta.inject.Inject
import jakarta.inject.Provider
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.EventDao
import javabot.dao.LogsDao
import javabot.dao.ShunDao
import javabot.model.Channel
import javabot.operations.throttle.Throttler
import javabot.web.JavabotApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InteractiveTestBot
@Inject
constructor(
    injector: Any, // Injector,
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
            //            val injector = Guice.createInjector(InteractiveJavabotModule())
            if (LOG.isInfoEnabled) {
                LOG.info("Starting Javabot")
            }
            val bot: InteractiveTestBot? =
                null // = injector.getInstance(InteractiveTestBot::class.java)
            bot?.start()
            Awaitility.await().forever().until<Boolean> { bot?.isRunning() ?: false }
        }
    }

    override fun start() {
        channelDao.findAll().forEach { channelDao.delete(it) }
        channelDao.save(Channel("#test-jb"))
        super.start()
    }
}
