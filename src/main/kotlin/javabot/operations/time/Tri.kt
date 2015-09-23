package javabot.operations.time

public class Tri<T> {
    var root: Node<T>

    init {
        root = Node()
    }

    public fun get(key: String?): T? {
        var key: String? = key ?: return null

        key = clean(key)

        var current: Node<Any> = root
        for (i in 0..key.length() - 1) {
            val index = hash(key.charAt(i))
            val node = current.getChild(index) ?: return null

            current = node
        }
        return current.value as T
    }

    public fun insert(key: String, value: T) {
        var key = key
        key = clean(key)

        var current: Node<Any> = root
        for (i in 0..key.length() - 1) {
            val index = hash(key.charAt(i))
            var node: Node<Any>? = current.getChild(index)

            if (node == null) {
                node = Node()
                current.setChild(index, node)
            }
            current = node
        }
        current.value = value
    }

    private fun clean(key: String): String {
        return key.toLowerCase().replaceAll("[^a-z]", "")
    }

    private fun hash(c: Char): Int {
        return c - OFFSET
    }

    private fun toChar(index: Int): Char {
        return (index + OFFSET).toChar()
    }

    private inner class Node<T> {
        var child = arrayOfNulls<Node<Any>>(ALPHABET_SIZE)
        public var value: T

        public fun getChild(index: Int): Node<Any>? {
            return child[index]
        }

        public fun setChild(index: Int, node: Node<Any>) {
            child[index] = node
        }
    }

    companion object {
        private val OFFSET = 'a'
        private val ALPHABET_SIZE = 26
    }
}
