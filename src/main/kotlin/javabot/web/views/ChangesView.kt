package javabot.web.views

import com.google.inject.assistedinject.Assisted
import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.dao.util.QueryParam
import javabot.model.Change
import javax.annotation.Nullable

class ChangesView
@Inject
constructor(
    adminDao: AdminDao,
    channelDao: ChannelDao,
    factoidDao: FactoidDao,
    apiDao: ApiDao,
    var changeDao: ChangeDao,
    @Assisted request: HttpServletRequest,
    @Assisted page: Int,
    @param:Nullable @param:Assisted private val message: String?,
    @param:Nullable @param:Assisted private val date: LocalDateTime?,
) : PagedView<Change>(adminDao, channelDao, factoidDao, apiDao, request, page) {

    override fun getPagedView(): String {
        return "changes.ftl"
    }

    override fun countItems(): Long {
        return changeDao.count(message, date)
    }

    override fun getPageUrl(): String {
        return "/changes"
    }

    override fun getPageItems(): List<Change> {
        return changeDao.getChanges(
            QueryParam(getIndex(), ITEMS_PER_PAGE, "updated"),
            message,
            date,
        )
    }
}
