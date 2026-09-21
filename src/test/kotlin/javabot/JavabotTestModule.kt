package javabot

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import org.testcontainers.containers.MongoDBContainer

// NickServDao/IrcAdapter's test-specific implementations (TestNickServDao/MockIrcAdapter) are no
// longer bound here -- they're CDI @Alternative beans (see those classes) now that Javabot/
// IrcAdapter/ConfigDao are all CDI-only-constructible (Task 15/18). getJavabot()/botProvider were
// deleted the same way: TestJavabot is now fully CDI-constructed, so this Guice-side path to it is
// permanently dead code, not just temporarily unreachable -- see the combined Task 15/18 report.
class JavabotTestModule : JavabotModule() {
    private val container = MongoDBContainer("mongo:6").withReuse(true)

    override fun configure() {
        super.configure()
        container.start()
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
}
