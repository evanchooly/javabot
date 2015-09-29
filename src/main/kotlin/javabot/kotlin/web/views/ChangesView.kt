package javabot.kotlin.web.views

import com.google.inject.Injector
import javabot.dao.ChangeDao
import javabot.dao.util.QueryParam
import javabot.model.Change

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

public class ChangesView(injector: Injector, request: HttpServletRequest, page: Int, private val filter: Change) :
      PagedView<Change>(injector, request, page) {
    @Inject
    lateinit var changeDao: ChangeDao

    override fun getPagedView(): String {
        return "changes.ftl"
    }

    override fun getFilter(): Change? {
        return filter
    }

    override fun countItems(): Long {
        return changeDao.count(filter)
    }

    override fun getPageUrl(): String {
        return "/changes"
    }

    override fun getPageItems(): List<Change> {
        return changeDao.getChanges(QueryParam(getIndex(), PagedView.ITEMS_PER_PAGE, "updated", true), filter)
    }
}
