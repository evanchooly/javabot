package javabot.dao.util;

/*
* Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */

//
// User: joed
// Date: Apr 11, 2007
// Time: 2:39:43 PM

//


public class QueryParam {

    private int first;

    private int count;

    private String sort;

    private boolean sortAsc;

    /**
     * Set to return <tt>count</tt> elements, starting at the <tt>first</tt>
     * element.
     *
     * @param first First element to return.
     * @param count Number of elements to return.
     */
    public QueryParam(int first, int count) {
        this(first, count, null, true);
    }

    /**
     * Set to return <tt>count</tt> sorted elements, starting at the
     * <tt>first</tt> element.
     *
     * @param first   First element to return.
     * @param count   Number of elements to return.
     * @param sort    Column to sort on.
     * @param sortAsc Sort ascending or descending.
     */
    public QueryParam(int first, int count, String sort, boolean sortAsc) {
        this.first = first;
        this.count = count;
        this.sort = sort;
        this.sortAsc = sortAsc;
    }

    public int getCount() {
        return count;
    }

    public int getFirst() {
        return first;
    }

    public String getSort() {
        return sort;
    }

    public boolean isSortAsc() {
        return sortAsc;
    }

    public boolean hasSort() {
        return sort != null;
    }

}
