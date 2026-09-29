package javabot.dao

import java.util.concurrent.TimeUnit
import javabot.dao.util.CallLimiter
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TestCallLimiter {
    @Test
    fun testCallLimiter() {
        val limiter = CallLimiter(1, 2, TimeUnit.SECONDS)
        // first call should be fine!
        assertTrue(limiter.tryAcquire())
        // second call, not enough time has passed
        assertFalse(limiter.tryAcquire())
        // sleep for two seconds, after which
        // the limiter should think everything's okay
        Thread.sleep(2001)
        assertTrue(limiter.tryAcquire())
    }
}
