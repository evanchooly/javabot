package javabot.operations.time

public class Tri<T> {
    var root: Node<T>

    init {
        root = Node()
    }

    public fun get(key: String): T? {

        var cleaned = clean(key)

        var current = root
        for (i in 0..cleaned.length - 1) {
            val index = hash(cleaned[i])
            val node = current.getChild(index) ?: return null

            current = node
        }
        return current.value as T
    }

    public fun insert(key: String, value: T) {
        var key = key
        key = clean(key)

        var current = root
        for (i in 0..key.length - 1) {
            val index = hash(key[i])
            var node = current.getChild(index)

            if (node == null) {
                node = Node()
                current.setChild(index, node)
            }
            current = node
        }
        current.value = value
    }

    private fun clean(key: String): String {
        return key.toLowerCase().replace("[^a-z]".toRegex(), "")
    }

    private fun hash(c: Char): Int {
        return c.toInt() - OFFSET
    }

    private fun toChar(index: Int): Char {
        return (index + OFFSET).toChar()
    }

    private inner class Node<T> {
        var child = arrayOfNulls<Node<T>>(26)
        public var value: T? = null

        public fun getChild(index: Int): Node<T>? {
            return child[index]
        }

        public fun setChild(index: Int, node: Node<T>) {
            child[index] = node
        }
    }

    companion object {
        private val OFFSET = 'a'.toInt()
    }
}
