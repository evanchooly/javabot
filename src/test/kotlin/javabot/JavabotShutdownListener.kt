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
 * Uses Arc's static container accessor (rather than a hand-rolled holder) because this listener
 * isn't itself a CDI bean -- JUnit Platform constructs it via ServiceLoader, not Arc. Probes for an
 * ALREADY-CONSTRUCTED Javabot instance via the bean's active context directly (Context.get(bean),
 * single-arg form) rather than InstanceHandle.get(), which would eagerly construct a fresh instance
 * (Javabot/TestJavabot are Singleton-scoped, so InstanceHandle.get() always succeeds in creating
 * one if none exists yet) -- exactly the eager-construction behavior this listener must avoid for a
 * test run that never started a bot.
 */
class JavabotShutdownListener : TestExecutionListener {
    override fun testPlanExecutionFinished(testPlan: TestPlan) {
        val container = Arc.container() ?: return
        if (!container.isRunning) return
        val handle = container.instance(Javabot::class.java)
        if (!handle.isAvailable) return
        val bean = handle.bean ?: return
        val context = container.getActiveContext(bean.scope) ?: return
        val existing = context.get(bean)
        existing?.shutdown()
    }
}
