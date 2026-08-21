package javabot.web

import com.google.inject.Guice
import com.google.inject.Injector
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import javabot.JavabotModule

/**
 * Bridges Quarkus/Arc's CDI container to the Guice-managed domain layer (the IRC bot, its DAOs, and
 * the Mongo/Morphia wiring in [JavabotModule]) by exposing a single shared [Injector] as a CDI
 * bean.
 *
 * The domain layer stays Guice-managed rather than becoming a set of CDI beans: it has its own
 * object graph (Mongo client settings, Morphia datastore, PircBotX configuration) that Arc has no
 * definitions for. Web-layer CDI beans that need a Guice-managed type should take this [Injector]
 * in their constructor and call `injector.getInstance(...)` explicitly, instead of asking CDI to
 * inject Guice types directly.
 */
@ApplicationScoped
class GuiceInjectorProducer {
    @Produces @ApplicationScoped fun injector(): Injector = INJECTOR

    companion object {
        val INJECTOR: Injector by lazy { Guice.createInjector(JavabotModule()) }
    }
}
