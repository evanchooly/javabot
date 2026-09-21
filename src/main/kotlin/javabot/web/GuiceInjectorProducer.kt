package javabot.web

import com.google.inject.Guice
import com.google.inject.Injector
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty

/**
 * Exposes a Guice [Injector] as a CDI bean. As of the 2026-09-20-remove-guice migration, the domain
 * layer (the IRC bot, its DAOs, operations/commands, and the Mongo/Morphia wiring) is real CDI
 * beans, not Guice-managed -- this producer is no longer a general-purpose bridge for web-layer
 * code to reach into Guice. Its sole remaining purpose is [javabot.config.TestDomainProducers],
 * whose `%test`-profile `mongoClient()`/`javabotConfig()` producers delegate to the instances
 * Guice's still-live `JavabotTestModule` builds (its Testcontainers `MongoDBContainer` and
 * test-specific config loading), so CDI-managed and still-Guice-managed beans share the exact same
 * container/config instead of diverging. This class -- and `JavabotModule`/ `JavabotTestModule`
 * themselves -- are temporary scaffolding, deleted once that bridge is no longer needed by a later
 * task in this migration.
 *
 * The concrete module class is config-driven (`javabot.guice.module`, overridden to
 * `javabot.JavabotTestModule` under the `%test` profile) because `src/main` can't reference a
 * `src/test` class directly.
 */
@ApplicationScoped
class GuiceInjectorProducer(
    @ConfigProperty(name = "javabot.guice.module") moduleClassName: String
) {

    private val injector: Injector by lazy {
        val module =
            Class.forName(moduleClassName).getDeclaredConstructor().newInstance()
                as com.google.inject.Module
        Guice.createInjector(module)
    }

    @Produces @ApplicationScoped fun injector(): Injector = injector
}
