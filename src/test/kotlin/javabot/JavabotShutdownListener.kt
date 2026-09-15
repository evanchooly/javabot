package javabot

import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestPlan

/**
 * Replaces TestNG's `@AfterSuite bot.shutdown()`: JUnit5 has no per-class equivalent that fires
 * once for the whole run (`@AfterAll` runs once per class), so this hooks the platform's "whole
 * test plan finished" event instead. Registered via
 * META-INF/services/org.junit.platform.launcher.TestExecutionListener.
 */
class JavabotShutdownListener : TestExecutionListener {
    override fun testPlanExecutionFinished(testPlan: TestPlan) {
        GuiceInjectorProducerHolder.shutdownIfStarted()
    }
}
