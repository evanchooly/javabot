package javabot.web.views

import com.google.inject.assistedinject.Assisted
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.dao.KarmaDao
import javabot.dao.util.QueryParam
import javabot.model.Karma
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

class KarmaView @Inject constructor(
        adminDao: AdminDao,
        channelDao: ChannelDao,
        factoidDao: FactoidDao,
        apiDao: ApiDao,
        var karmaDao: KarmaDao,
        @Assisted request: HttpServletRequest, @Assisted page: Int) :
        PagedView<Karma>(adminDao, channelDao, factoidDao, apiDao, request, page) {

    override fun countItems(): Long {
        return karmaDao.count()
    }

    override fun getPageUrl(): String {
        return "/karma"
    }

    override fun getPageItems(): List<Karma> {
        return karmaDao.getKarmas(QueryParam(getIndex(), ITEMS_PER_PAGE, "value", false))
    }

    override fun getPagedView(): String {
        return "karma.ftl"
    }
}
