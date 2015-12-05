package javabot.kotlin.web.views

import com.google.inject.Injector
import javabot.dao.util.QueryParam
import javabot.model.Factoid
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import javax.servlet.http.HttpServletRequest

public class FactoidsView(injector: Injector, request: HttpServletRequest, page: Int, private val filter: Factoid) :
      PagedView<Factoid>(injector, request, page) {

    override fun getPageUrl(): String {
        return "/factoids"
    }

    override fun countItems(): Long {
        return factoidDao.countFiltered(filter)
    }

    fun getFilter(): Factoid? {
        return filter
    }

    override fun getPageItems(): List<Factoid> {
        return factoidDao.getFactoidsFiltered(QueryParam(getIndex(), PagedView.ITEMS_PER_PAGE, "Name", true), filter)
    }

    override fun getNextPage(): String? {
        return applyFilter(super.getNextPage())
    }

    override fun getPreviousPage(): String? {
        return applyFilter(super.getPreviousPage())
    }

    private fun applyFilter(url: String?): String? {
        try {
            if (url != null) {
                val builder = StringBuilder()
                builder.append("&name=").append(encode(filter.name))
                builder.append("&value=").append(encode(filter.value))
                builder.append("&userName=").append(encode(filter.userName))
                return url + builder
            }
        } catch (e: UnsupportedEncodingException) {
            LOG.error(e.message, e)
        }

        return url
    }

    override fun getPagedView(): String {
        return "/factoids.ftl"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(FactoidsView::class.java)
    }

}
