package javabot

import com.google.inject.Inject
import com.google.inject.Provides
import com.google.inject.Singleton
import javabot.dao.NickServDao
import javabot.dao.TestNickServDao
import javabot.model.TestUser
import org.pircbotx.Channel
import org.pircbotx.Configuration.BotFactory
import org.pircbotx.Configuration.Builder
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.pircbotx.UserChannelDao
import org.pircbotx.exception.IrcException
import org.pircbotx.output.OutputChannel
import org.pircbotx.output.OutputRaw
import org.pircbotx.output.OutputUser
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.HashMap
import java.util.Properties
import javax.inject.Provider

public class JavabotTestModule : JavabotModule() {
    lateinit private var botProvider: Provider<TestJavabot>
    lateinit private var botFactoryProvider: Provider<TestBotFactory>
    private val testJavabot: TestJavabot by lazy { botProvider.get() }

    override fun configure() {
        botProvider = binder().getProvider(TestJavabot::class.java)
        botFactoryProvider = binder().getProvider(TestBotFactory::class.java)
        bind(NickServDao::class.java).to(TestNickServDao::class.java)

        super.configure()
    }

    override protected fun loadConfigProperties(): HashMap<Any, Any> {
        return HashMap(load(load(Properties(), "javabot.properties"), "test-javabot.properties"))
    }

    private fun load(properties: Properties, name: String): Properties {
        val file = File(name)
        if (file.exists()) {
            try {
                FileInputStream(file).use { stream -> properties.load(stream) }
            } catch (e: IOException) {
                throw RuntimeException(e.getMessage(), e)
            }

        }
        return properties
    }

    @Provides
    @Singleton
    public fun getJavabot(): Javabot {
        return testJavabot
    }

    override fun buildBot(builder: Builder<PircBotX>): PircBotX {
        builder.setBotFactory(botFactory())
        return TestPircBotX(builder)
    }

    protected fun botFactory(): BotFactory {
        return botFactoryProvider.get()
    }

    private class TestPircBotX(builder: Builder<PircBotX>) : PircBotX(builder.buildConfiguration()) {

        override fun getNick(): String {
            return BaseTest.TEST_BOT_NICK
        }

        @Throws(IOException::class, IrcException::class)
        override fun connect() {
            reconnectStopped = true
        }
    }

    override protected fun getBotNick(): String = BaseTest.TEST_BOT_NICK

    @Singleton
    private class TestBotFactory : BotFactory() {

        @Inject
        lateinit var messages: Messages

        override fun createUserChannelDao(bot: PircBotX): UserChannelDao<User, Channel> {
            return object : UserChannelDao<User, Channel>(bot, this) {
                override fun getUser(nick: String): User {
                    return createUser(bot, nick)
                }
            }
        }

        override fun createOutputRaw(bot: PircBotX): OutputRaw {
            return object : OutputRaw(bot, 0) {
                override fun rawLine(line: String) {
                    LOG.debug(line)
                }

                override fun rawLineNow(line: String) {
                    LOG.debug(line)
                }

                override fun rawLineNow(line: String, resetDelay: Boolean) {
                    LOG.debug("line = [$line], resetDelay = [$resetDelay]")
                }

                override fun rawLineSplit(prefix: String,
                                          message: String,
                                          suffix: String) {
                    messages.add(if (message.startsWith("ACTION")) message.substring(7) else message)
                }
            }
        }

        override fun createOutputChannel(bot: PircBotX, channel: Channel): OutputChannel {
            return object : OutputChannel(bot, channel) {
                override fun message(user: User, message: String) {
                    messages.add(message)
                }

                override fun message(message: String) {
                    messages.add(message)
                }
            }
        }

        override fun createOutputUser(bot: PircBotX, user: User): OutputUser {
            return object : OutputUser(bot, user) {
                override fun message(message: String) {
                    messages.add(message)
                }

                override fun action(action: String) {
                    super.action(action)
                }

                override fun ctcpCommand(command: String) {
                    super.ctcpCommand(command)
                }

                override fun ctcpResponse(message: String) {
                    super.ctcpResponse(message)
                }

                override fun notice(notice: String) {
                    super.notice(notice)
                }
            }
        }

        override fun createUser(bot: PircBotX, nick: String): User {
            return TestUser(bot, bot.userChannelDao, messages, nick, null, null)
        }

        companion object {
            private val LOG = LoggerFactory.getLogger(TestBotFactory::class.java)
        }

    }
}
