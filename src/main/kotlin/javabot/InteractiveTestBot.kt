package javabot

import com.google.inject.Guice
import com.jayway.awaitility.Awaitility
import jakarta.inject.Inject
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.LogsDao
import javabot.dao.ShunDao
import javabot.model.Channel
import javabot.model.EventInjector
import javabot.operations.throttle.Throttler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InteractiveTestBot
@Inject
constructor(
    eventInjector: EventInjector,
    configDao: ConfigDao,
    channelDao: ChannelDao,
    logsDao: LogsDao,
    shunDao: ShunDao,
    throttler: Throttler,
    adapter: IrcAdapter,
    adminDao: AdminDao,
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

// NOTE: broken by the CDI/Arc migration (2026-09-20-remove-guice.md) -- Javabot's constructor now
// takes EventInjector (a CDI-only type Guice cannot construct), and JavabotModule's
// createIrcBot()/getBotListener()/getBotNick()/ircAdapterProvider (which this class used to
// override) were deleted as permanently-dead code once ConfigDao/IrcAdapter also went CDI-only
// (see Task 15/18's combined report). InteractiveTestBot.main() is a local developer convenience
// tool with no test coverage; left broken rather than converted, per that migration's explicit
// Non-goals. Fix forward if this tool is needed again.
class InteractiveJavabotModule : JavabotModule()
