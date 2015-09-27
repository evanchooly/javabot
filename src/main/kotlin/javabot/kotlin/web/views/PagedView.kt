package javabot.kotlin.web.views

import com.google.inject.Injector

import javax.servlet.http.HttpServletRequest

public abstract class PagedView<V>(injector: Injector, request: HttpServletRequest, private var page: Int) :
      MainView(injector, request) {
    private val itemsPerPage = ITEMS_PER_PAGE
    protected var itemCount: Long = -1
        get() {
            if ($itemCount == -1L) {
                $itemCount = countItems()
            }
            return $itemCount
        }


    override fun getChildView(): String {
        return "paged.ftl"
    }

    public abstract fun getPagedView(): String

    public abstract fun countItems(): Long

    public open fun getFilter(): V? {
        return null
    }

    public fun getPage(): Int {
        if (page < 1) {
            page = 1
        } else if (page > getPageCount()) {
            page = getPageCount()
        }
        return page
    }

    public fun getIndex(): Int {
        val index = (getPage() - 1) * getItemsPerPage()
        if (itemCount == 0L) {
            return -1
        } else if (index > itemCount) {
            return (getPageCount() - 1) * getItemsPerPage()
        } else {
            return index
        }
    }

    public fun getPageCount(): Int {
        return Math.ceil(1.0 * itemCount / getItemsPerPage()).toInt()
    }

    public fun getItemsPerPage(): Int {
        return itemsPerPage
    }

    public open fun getNextPage(): String? {
        val nextPage = getPage() + 1
        return if (nextPage <= getPageCount()) getPageUrl() + "?page=" + nextPage else null
    }

    protected abstract fun getPageUrl(): String

    public open fun getPreviousPage(): String? {
        val previousPage = if (getPage() == 1) -1 else getPage() - 1
        return if (previousPage > 0) (getPageUrl() + "?page=" + previousPage) else null
    }

    public fun getEndRange(): Long {
        return Math.min(itemCount, getStartRange() + getItemsPerPage() - 1)
    }

    public fun getStartRange(): Long {
        return getIndex() + 1L
    }

    public abstract fun getPageItems(): List<V>

    companion object {
        public val ITEMS_PER_PAGE: Int = 50
    }
}
