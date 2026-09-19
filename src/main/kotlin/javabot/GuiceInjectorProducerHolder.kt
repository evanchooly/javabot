package javabot

/**
 * Tracks the [Javabot] instance that was actually constructed during a run, purely so the
 * test-suite shutdown listener (`javabot.JavabotShutdownListener`, `src/test`) can shut the bot
 * down once at the end of the run without a CDI lookup.
 *
 * Lives in `src/main` (rather than alongside the listener in `src/test`) because [Javabot] --
 * itself in `src/main` -- needs to register with it; `src/main` cannot reference a `src/test`
 * class.
 *
 * It holds the bot rather than the Guice `Injector` deliberately: looking the bot up through the
 * injector at shutdown time (`injector.getInstance(Javabot::class.java)`) would *construct* a bot
 * -- along with its thread pools and a `PircBotX` -- in any run that never created one, just to
 * immediately shut it down. Registering from [Javabot]'s constructor means shutdown is a no-op
 * exactly when no bot was ever built.
 */
object GuiceInjectorProducerHolder {
    @Volatile private var bot: Javabot? = null

    fun register(bot: Javabot) {
        this.bot = bot
    }

    /**
     * Shuts down the bot if one was built during this run. Mirrors what the old TestNG `@AfterSuite
     * bot.get().shutdown()` did: [Javabot.shutdown] stops the operation executor pool, which
     * `PircBotX.stopBotReconnect()` never touched. [Javabot.shutdown] is itself idempotent (it
     * no-ops once the pool is shut down), so overlapping with Javabot's own JVM shutdown hook is
     * harmless.
     */
    fun shutdownIfStarted() {
        bot?.shutdown()
    }
}
