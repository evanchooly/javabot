package javabot.config

import com.google.inject.Injector
import com.mongodb.client.MongoClient
import io.quarkus.arc.profile.IfBuildProfile
import jakarta.annotation.Priority
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Alternative
import jakarta.enterprise.inject.Produces
import jakarta.inject.Inject
import javabot.JavabotConfig

/**
 * Test-only override of [DomainProducers.mongoClient]/[DomainProducers.javabotConfig]. Delegates to
 * the instances Guice's `JavabotTestModule` already built, so CDI-managed and still-Guice-managed
 * beans share the exact same running Mongo container and the exact same test-specific config
 * (loaded from `javabot.properties`/`test-javabot.properties` by
 * `JavabotTestModule.loadConfigProperties()`, which `DomainProducers.javabotConfig()`'s own
 * `loadConfigProperties()` doesn't know about) throughout this migration, instead of diverging onto
 * two separate configs/databases. This class is deleted (and replaced by producers that own this
 * configuration directly) once `JavabotTestModule` itself is deleted -- see this plan's final task.
 */
@Alternative
@Priority(1)
@ApplicationScoped
class TestDomainProducers @Inject constructor(private val injector: Injector) {
    @Produces
    @ApplicationScoped
    @IfBuildProfile("test")
    fun mongoClient(): MongoClient = injector.getInstance(MongoClient::class.java)

    @Produces
    @ApplicationScoped
    @IfBuildProfile("test")
    fun javabotConfig(): JavabotConfig = injector.getInstance(JavabotConfig::class.java)
}
