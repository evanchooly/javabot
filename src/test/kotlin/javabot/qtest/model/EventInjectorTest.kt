package javabot.qtest.model

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import javabot.model.AdminEvent
import javabot.model.ApiEvent
import javabot.model.EventInjector
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@QuarkusTest
class EventInjectorTest {

    @Inject lateinit var eventInjector: EventInjector

    @Test
    fun `inject populates AdminEvent bot field`() {
        val event = AdminEvent()
        eventInjector.inject(event)
        assertNotNull(event.bot)
    }

    @Test
    fun `inject populates ApiEvent own and inherited fields`() {
        val event = ApiEvent()
        eventInjector.inject(event)
        assertNotNull(event.config, "config")
        assertNotNull(event.asmParser, "asmParser")
        assertNotNull(event.apiDao, "apiDao")
        assertNotNull(event.adminDao, "adminDao")
        assertNotNull(event.bot, "bot (inherited from AdminEvent)")
    }
}
