package javabot

import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.Duration
import com.jayway.awaitility.core.ConditionTimeoutException
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
class Messages(var messages: MutableList<String> = ArrayList()) : Iterable<String>, List<String> by messages {
    fun add(message: String) {
        messages.add(message)
    }

    fun clear() {
        messages = ArrayList()
    }

    fun get(duration: Duration = Duration(30, TimeUnit.SECONDS), failOnTimeout: Boolean = true): List<String> {
        try {
            Awaitility.await()
                    .pollInterval(Duration.FIVE_HUNDRED_MILLISECONDS)
                    .atMost(duration)
                    .until<Boolean>({ !messages.isEmpty() })
        } catch(e: ConditionTimeoutException) {
            if (failOnTimeout) {
                throw e
            }
        }
        val list = messages
        clear()
        return list
    }
}
