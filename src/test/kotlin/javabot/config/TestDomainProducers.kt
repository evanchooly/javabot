package javabot.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import io.quarkus.arc.profile.IfBuildProfile
import jakarta.annotation.Priority
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Alternative
import jakarta.enterprise.inject.Produces
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import javabot.JavabotConfig
import org.aeonbits.owner.ConfigFactory
import org.testcontainers.containers.MongoDBContainer

/**
 * Test-only override of [DomainProducers.mongoClient]/[DomainProducers.javabotConfig]. Owns its own
 * Testcontainers Mongo container and its own test-specific config loading (ported from the
 * now-deleted Guice `JavabotTestModule.loadConfigProperties()`: `javabot.properties` then
 * `test-javabot.properties`, both optional) now that there's no Guice `Injector` left to delegate
 * through.
 */
@Alternative
@Priority(1)
@ApplicationScoped
class TestDomainProducers {
    private val container = MongoDBContainer("mongo:6").withReuse(true)

    @Produces
    @ApplicationScoped
    @IfBuildProfile("test")
    fun mongoClient(): MongoClient {
        container.start()
        return MongoClients.create(container.replicaSetUrl)
    }

    @Produces
    @ApplicationScoped
    @IfBuildProfile("test")
    fun javabotConfig(): JavabotConfig {
        val properties = Properties()
        for (name in listOf("javabot.properties", "test-javabot.properties")) {
            val file = File(name)
            if (file.exists()) {
                FileInputStream(file).use { stream -> properties.load(stream) }
            }
        }
        val config =
            ConfigFactory.create(
                JavabotConfig::class.java,
                HashMap(properties.toMap()),
                System.getProperties(),
                System.getenv(),
            )
        return validateJavabotConfig(config)
    }
}
