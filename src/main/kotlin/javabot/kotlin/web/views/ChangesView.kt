package javabot.kotlin.web.views

import com.google.inject.Injector
import javabot.dao.ChangeDao
import javabot.dao.util.QueryParam
import javabot.model.Change
import java.time.LocalDateTime

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

public class ChangesView(injector: Injector, request: HttpServletRequest, page: Int, private val message: String? = null,
                         private val date: LocalDateTime? = null) :
      PagedView<Change>(injector, request, page) {
    @Inject
    lateinit var changeDao: ChangeDao

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
        return changeDao.getChanges(QueryParam(getIndex(), PagedView.ITEMS_PER_PAGE, "updated", true), message, date)
    }
}
