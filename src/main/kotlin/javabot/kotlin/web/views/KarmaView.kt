package javabot.kotlin.web.views

import com.google.inject.Injector
import javabot.dao.KarmaDao
import javabot.dao.util.QueryParam
import javabot.model.Karma

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

public class KarmaView(injector: Injector, request: HttpServletRequest, page: Int) : PagedView<Karma>(injector, request, page) {
    @Inject
    lateinit var karmaDao: KarmaDao

    override fun countItems(): Long {
        return karmaDao.count()
    }

    override fun getPageUrl(): String {
        return "/karma"
    }

    override fun getPageItems(): List<Karma> {
        return karmaDao.getKarmas(QueryParam(getIndex(), PagedView.ITEMS_PER_PAGE, "value", false))
    }

    override fun getPagedView(): String {
        return "karma.ftl"
    }
}
