package javabot

import javax.inject.Singleton
import java.util.ArrayList
import java.util.Spliterator
import java.util.function.Consumer

@Singleton
public class Messages : Iterable<String> {
    var messages: MutableList<String> = ArrayList()

    public fun isEmpty(): Boolean {
        return messages.isEmpty()
    }

    public fun add(message: String) {
        messages.add(message)
    }

    public fun size(): Int {
        return messages.size()
    }

    public fun remove(index: Int): String {
        return messages.remove(index)
    }

    public fun get(): List<String> {
        val list = ArrayList(messages)
        messages.clear()
        return list
    }

    public fun get(index: Int): String {
        return messages.get(index)
    }

    override fun iterator(): Iterator<String> {
        return messages.iterator()
    }

    override fun toString(): String {
        return messages.toString()
    }
}
