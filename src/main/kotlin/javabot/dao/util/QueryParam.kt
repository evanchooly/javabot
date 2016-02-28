package javabot.dao.util

/**
 * Set to return count sorted elements, starting at the first element.
 * @param first   First element to return.
 * @param count   Number of elements to return.
 * @param sort    Column to sort on.
 * @param sortAsc Sort ascending or descending.
 */

class QueryParam(val first: Int, val count: Int, val sort: String? = null, val sortAsc: Boolean = true) {
    fun hasSort(): Boolean {
        return sort != null
    }
}