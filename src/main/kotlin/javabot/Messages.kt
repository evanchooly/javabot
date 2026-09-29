package javabot

import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.Duration
import com.jayway.awaitility.core.ConditionTimeoutException
import jakarta.inject.Singleton
import java.util.ArrayList
import java.util.concurrent.TimeUnit

// @JvmOverloads generates a public no-arg constructor overload alongside the defaulted one --
// required for this to be CDI-discoverable now that it's no longer in quarkus.arc.exclude-types
// (Task 15/18's combined report): a bean class needs either an @Inject constructor or a
// no-args one, and Kotlin's single-constructor-with-a-default-value doesn't expose a bare
// no-args overload to Java reflection without this.
@Singleton
class Messages @JvmOverloads constructor(var messages: MutableList<String> = ArrayList()) :
    Iterable<String>, List<String> by messages {
    fun add(message: String) {
        messages.add(message)
    }

    fun clear() {
        messages = ArrayList()
    }

    fun get(
        duration: Duration = Duration(30, TimeUnit.SECONDS),
        failOnTimeout: Boolean = true,
    ): List<String> {
        try {
            Awaitility.await()
                .pollInterval(Duration.FIVE_HUNDRED_MILLISECONDS)
                .atMost(duration)
                .until<Boolean>({ !messages.isEmpty() })
        } catch (e: ConditionTimeoutException) {
            if (failOnTimeout) {
                throw e
            }
        }
        val list = messages
        clear()
        return list
    }
}
