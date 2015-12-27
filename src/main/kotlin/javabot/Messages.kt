package javabot

import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.Duration
import java.util.ArrayList
import javax.inject.Singleton

@Singleton
class Messages(var messages: MutableList<String> = ArrayList()) : Iterable<String>, List<String> by messages {
    fun add(message: String) {
        messages.add(message)
    }

    fun clear() {
        messages.clear()
    }

    fun get(): List<String> {
        Awaitility.await()
                .pollInterval(Duration.FIVE_HUNDRED_MILLISECONDS)
                .atMost(Duration.TEN_SECONDS)
                .until<Boolean>({ !messages.isEmpty() })
        val list = ArrayList(messages)
        clear()
        return list
    }
}
