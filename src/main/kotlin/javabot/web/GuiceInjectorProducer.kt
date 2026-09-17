package javabot.web

import com.google.inject.Guice
import com.google.inject.Injector
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty

/**
 * Bridges Quarkus/Arc's CDI container to the Guice-managed domain layer (the IRC bot, its DAOs, and
 * the Mongo/Morphia wiring in [javabot.JavabotModule]) by exposing a single shared [Injector] as a
 * CDI bean.
 *
 * The domain layer stays Guice-managed rather than becoming a set of CDI beans: it has its own
 * object graph (Mongo client settings, Morphia datastore, PircBotX configuration) that Arc has no
 * definitions for. Web-layer CDI beans that need a Guice-managed type should take this [Injector]
 * in their constructor and call `injector.getInstance(...)` explicitly, instead of asking CDI to
 * inject Guice types directly.
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
        Guice.createInjector(module).also { javabot.GuiceInjectorProducerHolder.register(it) }
    }

    @Produces @ApplicationScoped fun injector(): Injector = injector
}
