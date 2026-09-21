package javabot.qtest.model

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import javabot.model.AdminEvent
import javabot.model.EventInjector
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@QuarkusTest
class EventInjectorTest {

    @Inject lateinit var eventInjector: EventInjector

    @Disabled("Javabot isn't a CDI bean until Task 15")
    @Test
    fun `inject populates AdminEvent bot field`() {
        val event = AdminEvent()
        eventInjector.inject(event)
        assertNotNull(event.bot)
    }
}
