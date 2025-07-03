package javabot.operations.time

class Tri<T> {
    private var root: Node<T> = Node()

    fun get(key: String): T? {

        var cleaned = clean(key)

        var current = root
        for (i in 0..cleaned.length - 1) {
            val index = hash(cleaned[i])
            val node = current.getChild(index) ?: return null

            current = node
        }
        @Suppress("UNCHECKED_CAST")
        return current.value as T
    }

    fun insert(key: String, value: T) {
        var cleaned = clean(key)

        var current = root
        for (i in 0..cleaned.length - 1) {
            val index = hash(cleaned[i])
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
        return key.lowercase().replace("[^a-z]".toRegex(), "")
    }

    private fun hash(c: Char): Int {
        return c.code - OFFSET
    }

    private inner class Node<T> {
        var child = arrayOfNulls<Node<T>>(26)
        var value: T? = null

        fun getChild(index: Int): Node<T>? {
            return child[index]
        }

        fun setChild(index: Int, node: Node<T>) {
            child[index] = node
        }
    }

    companion object {
        private val OFFSET = 'a'.code
    }
}
