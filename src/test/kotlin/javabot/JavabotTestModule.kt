package javabot

import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import javabot.dao.NickServDao
import javabot.dao.TestNickServDao
import javax.inject.Provider
import org.testcontainers.containers.MongoDBContainer

class JavabotTestModule : JavabotModule() {
    private lateinit var botProvider: Provider<TestJavabot>
    private val container = MongoDBContainer("mongo:6")
        .withReuse(true)

    override fun configure() {
        super.configure()
        container.start()
        botProvider = binder().getProvider(TestJavabot::class.java)
        bind(NickServDao::class.java).to(TestNickServDao::class.java)
        bind(IrcAdapter::class.java).to(MockIrcAdapter::class.java)
    }

    override fun client(): MongoClient {
        return MongoClients.create(container.replicaSetUrl)
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
