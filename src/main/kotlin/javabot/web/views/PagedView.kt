package javabot.web.views

import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javax.servlet.http.HttpServletRequest

abstract class PagedView<V>(
        adminDao: AdminDao,
        channelDao: ChannelDao,
        factoidDao: FactoidDao,
        apiDao: ApiDao,
        request: HttpServletRequest, private var page: Int) :
        MainView(adminDao, channelDao, factoidDao, apiDao, request) {
    private val itemsPerPage = ITEMS_PER_PAGE
    var itemCount: Long = -1
        get() {
            if (field == -1L) {
                field = countItems()
            }
            return field
        }


    override fun getChildView(): String {
        return "paged.ftl"
    }

    abstract fun getPagedView(): String

    abstract fun countItems(): Long

    fun getPage(): Int {
        if (page < 1) {
            page = 1
        } else if (page > getPageCount()) {
            page = getPageCount()
        }
        return page
    }

    fun getIndex(): Int {
        val index = (getPage() - 1) * getItemsPerPage()
        if (itemCount == 0L) {
            return -1
        } else if (index > itemCount) {
            return (getPageCount() - 1) * getItemsPerPage()
        } else {
            return index
        }
    }

    fun getPageCount(): Int {
        return Math.ceil(1.0 * itemCount / getItemsPerPage()).toInt()
    }

    fun getItemsPerPage(): Int {
        return itemsPerPage
    }

    open fun getNextPage(): String? {
        val nextPage = getPage() + 1
        return if (nextPage <= getPageCount()) getPageUrl() + "?page=" + nextPage else null
    }

    protected abstract fun getPageUrl(): String

    open fun getPreviousPage(): String? {
        val previousPage = if (getPage() == 1) -1 else getPage() - 1
        return if (previousPage > 0) (getPageUrl() + "?page=" + previousPage) else null
    }

    fun getEndRange(): Long {
        return Math.min(itemCount, getStartRange() + getItemsPerPage() - 1)
    }

    fun getStartRange(): Long {
        return getIndex() + 1L
    }

    abstract fun getPageItems(): List<V>

    companion object {
        val ITEMS_PER_PAGE: Int = 50
    }
}
