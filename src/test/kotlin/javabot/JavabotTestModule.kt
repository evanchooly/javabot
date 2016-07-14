package javabot

import com.google.inject.Provides
import com.google.inject.Singleton
import javabot.dao.NickServDao
import javabot.dao.TestNickServDao
import java.io.File
import java.io.FileInputStream
import java.util.HashMap
import java.util.Properties
import javax.inject.Provider

class JavabotTestModule : JavabotModule() {
    lateinit private var botProvider: Provider<TestJavabot>

    override fun configure() {
        super.configure()
        botProvider = binder().getProvider(TestJavabot::class.java)
        bind(NickServDao::class.java).to(TestNickServDao::class.java)
        bind(IrcAdapter::class.java).to(MockIrcAdapter::class.java)
    }

    override fun loadConfigProperties(): HashMap<Any, Any> {
        return HashMap(load(load(Properties(), "javabot.properties"), "test-javabot.properties"))
    }

    private fun load(properties: Properties, name: String): Properties {
        val file = File(name)
        if (file.exists()) {
            FileInputStream(file).use { stream -> properties.load(stream) }
        }
        return properties
    }

    @Provides
    @Singleton
    fun getJavabot(): Javabot {
        return botProvider.get()
    }

    override fun getBotNick(): String = BaseTest.TEST_BOT_NICK
}
