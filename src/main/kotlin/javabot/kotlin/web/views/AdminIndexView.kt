package javabot.kotlin.web.views

import com.google.inject.Injector
import javabot.model.Admin
import javax.servlet.http.HttpServletRequest

public class AdminIndexView(injector: Injector, request: HttpServletRequest, val current: Admin?,
                            val editing: Admin? = Admin()) : MainView(injector, request) {
    public fun getAdmins(): List<Admin> {
        return adminDao.findAll()
    }

    override fun getChildView(): String {
        return "admin/index.ftl"
    }
}
