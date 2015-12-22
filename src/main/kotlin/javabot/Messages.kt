package javabot

import java.util.ArrayList
import javax.inject.Singleton

@Singleton
public class Messages(var messages: MutableList<String> = ArrayList()) : Iterable<String>, List<String> by messages {
    public fun add(message: String) {
        messages.add(message)
    }

    public fun get(): List<String> {
        val list = ArrayList(messages)
        messages.clear()
        return list
    }
}
