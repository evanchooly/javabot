package javabot

import io.quarkus.arc.Arc
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestPlan

/**
 * Replaces TestNG's `@AfterSuite bot.shutdown()`: JUnit5 has no per-class equivalent that fires
 * once for the whole run (`@AfterAll` runs once per class), so this hooks the platform's "whole
 * test plan finished" event instead. Registered via
 * META-INF/services/org.junit.platform.launcher.TestExecutionListener.
 *
 * Uses Arc's static container accessor (rather than the deleted GuiceInjectorProducerHolder)
 * because this listener isn't itself a CDI bean -- JUnit Platform constructs it via ServiceLoader,
 * not Arc. `Arc.container().instance(Javabot::class.java)` only *resolves* an already-constructed
 * singleton if the CDI context is still active; it does not eagerly build a fresh Javabot for a
 * test run that never started one.
 */
class JavabotShutdownListener : TestExecutionListener {
    override fun testPlanExecutionFinished(testPlan: TestPlan) {
        val container = Arc.container()
        if (container != null && container.isRunning) {
            val handle = container.instance(Javabot::class.java)
            if (handle.isAvailable) {
                handle.get().shutdown()
            }
        }
    }
}
