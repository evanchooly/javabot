package javabot.dao.util

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class CallLimiter(
    val allowed: Int = 1000,
    val span: Long = 1,
    val period: TimeUnit = TimeUnit.DAYS,
) {
    companion object {
        fun create(
            allowed: Int = 1000,
            span: Long = 1,
            period: TimeUnit = TimeUnit.DAYS,
        ): CallLimiter {
            return CallLimiter(allowed, span, period)
        }
    }

    var reset: Instant
    var acquisitions = AtomicInteger(allowed)

    init {
        reset = Instant.now().plus(period.toMillis(span), ChronoUnit.MILLIS)
    }

    fun tryAcquire(): Boolean {
        if (Instant.now().isAfter(reset)) {
            reset = Instant.now().plus(period.toMillis(span), ChronoUnit.MILLIS)
            acquisitions = AtomicInteger(allowed)
        }
        return if (acquisitions.get() > 0) {
            acquisitions.decrementAndGet()
            true
        } else {
            false
        }
    }
}
