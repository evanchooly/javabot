package javabot.config

import com.google.inject.Injector
import com.mongodb.client.MongoClient
import io.quarkus.arc.profile.IfBuildProfile
import jakarta.annotation.Priority
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Alternative
import jakarta.enterprise.inject.Produces
import jakarta.inject.Inject

/**
 * Test-only override of [DomainProducers.mongoClient]. Delegates to the [MongoClient] Guice's
 * `JavabotTestModule` already built (its Testcontainers `MongoDBContainer`), so CDI-managed and
 * still-Guice-managed beans share the exact same running container throughout this migration
 * instead of diverging onto two separate empty databases. This class is deleted (and replaced by a
 * producer that owns the container directly) once `JavabotTestModule` itself is deleted -- see this
 * plan's final task.
 */
@Alternative
@Priority(1)
@ApplicationScoped
class TestDomainProducers @Inject constructor(private val injector: Injector) {
    @Produces
    @ApplicationScoped
    @IfBuildProfile("test")
    fun mongoClient(): MongoClient = injector.getInstance(MongoClient::class.java)
}
