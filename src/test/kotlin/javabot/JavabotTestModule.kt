package javabot

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import jakarta.enterprise.inject.Produces
import jakarta.inject.Inject
import jakarta.inject.Provider
import jakarta.inject.Singleton
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import javabot.dao.ConfigDao
import org.testcontainers.containers.MongoDBContainer

class JavabotTestModule @Inject constructor(configDao: ConfigDao, ircAdapter: IrcAdapter) :
    JavabotModule(configDao, ircAdapter) {
    private lateinit var botProvider: Provider<TestJavabot>
    private val container = MongoDBContainer("mongo:6").withReuse(true)

    override fun configure() {
        super.configure()
        container.start()
        //        botProvider = binder().getProvider(TestJavabot::class.java)
        //        bind(NickServDao::class.java).to(TestNickServDao::class.java)
        //        bind(IrcAdapter::class.java).to(MockIrcAdapter::class.java)
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

    @Produces
    @Singleton
    fun getJavabot(): Javabot {
        return botProvider.get()
    }

    override fun getBotNick(): String = BaseTest.TEST_BOT_NICK
}
