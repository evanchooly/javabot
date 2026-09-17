package javabot

import com.google.inject.Injector
import org.pircbotx.PircBotX

/**
 * Tracks the injector `GuiceInjectorProducer` builds, purely so the test-suite shutdown listener
 * (`javabot.JavabotShutdownListener`, `src/test`) can shut the bot down once at the end of the run
 * without a CDI lookup. Lives in `src/main` (rather than alongside the listener in `src/test`)
 * because `GuiceInjectorProducer` -- itself in `src/main` -- needs to register with it; `src/main`
 * cannot reference a `src/test` class.
 */
object GuiceInjectorProducerHolder {
    @Volatile private var injector: Injector? = null

    fun register(injector: Injector) {
        this.injector = injector
    }

    fun shutdownIfStarted() {
        injector?.let {
            it.getInstance(PircBotX::class.java).let { bot ->
                if (bot.isConnected) bot.stopBotReconnect()
            }
        }
    }
}
