package javabot.kotlin.web.views

import com.google.inject.Injector
import javabot.dao.AdminDao
import javabot.model.Admin

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

public class AdminIndexView(injector: Injector, request: HttpServletRequest) : MainView(injector, request) {

    @Inject
    private lateinit val adminDao: AdminDao

    public fun getAdmins(): List<Admin> {
        return adminDao.findAll()
    }

    override fun getChildView(): String {
        return "admin/index.ftl"
    }
}
