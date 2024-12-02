package javabot.web.views

import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.dao.KarmaDao
import javabot.dao.util.QueryParam
import javabot.model.Karma

class KarmaView
@Inject
constructor(
    adminDao: AdminDao,
    channelDao: ChannelDao,
    factoidDao: FactoidDao,
    apiDao: ApiDao,
    var karmaDao: KarmaDao,
    request: HttpServletRequest,
    page: Int
) : PagedView<Karma>(adminDao, channelDao, factoidDao, apiDao, request, page) {

    override fun countItems(): Long {
        return karmaDao.count()
    }

    override fun getPageUrl(): String {
        return "/karma"
    }

    override fun getPageItems(): List<Karma> {
        return karmaDao.list(QueryParam(getIndex(), ITEMS_PER_PAGE, "value", false))
    }

    override fun getPagedView(): String {
        return "karma.ftl"
    }
}
