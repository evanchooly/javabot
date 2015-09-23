package javabot.dao.util

/*
* Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */

//
// User: joed
// Date: Apr 11, 2007
// Time: 2:39:43 PM

//


public class QueryParam
/**
 * Set to return count sorted elements, starting at the
 * first element.

 * @param first   First element to return.
 * *
 * @param count   Number of elements to return.
 * *
 * @param sort    Column to sort on.
 * *
 * @param sortAsc Sort ascending or descending.
 */
JvmOverloads constructor(public val first: Int, public val count: Int, public val sort: String? = null, private val sortAsc: Boolean = true) {

    public fun isSortAsc(): Boolean {
        return sortAsc
    }

    public fun hasSort(): Boolean {
        return sort != null
    }

}
/**
 * Set to return count elements, starting at the first
 * element.

 * @param first First element to return.
 * *
 * @param count Number of elements to return.
 */
