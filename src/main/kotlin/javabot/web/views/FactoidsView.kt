package javabot.web.views

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.dao.util.QueryParam
import javabot.model.Factoid
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import javax.servlet.http.HttpServletRequest

class FactoidsView @Inject constructor(
        adminDao: AdminDao,
        channelDao: ChannelDao,
        factoidDao: FactoidDao,
        apiDao: ApiDao,
        @Assisted request: HttpServletRequest, @Assisted page: Int, @Assisted private val filter: Factoid) :
        PagedView<Factoid>(adminDao, channelDao, factoidDao, apiDao,  request, page) {

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
        return factoidDao.getFactoidsFiltered(QueryParam(getIndex(), ITEMS_PER_PAGE, "Name", true), filter)
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
