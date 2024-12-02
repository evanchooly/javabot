package javabot.web.views

import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.model.Admin

class AdminIndexView
@Inject
constructor(
    adminDao: AdminDao,
    channelDao: ChannelDao,
    factoidDao: FactoidDao,
    apiDao: ApiDao,
    request: HttpServletRequest,
    val current: Admin,
    val editing: Admin?
) : MainView(adminDao, channelDao, factoidDao, apiDao, request) {

    fun getAdmins(): List<Admin> {
        return adminDao.findAll()
    }

    override fun getChildView(): String {
        return "admin/index.ftl"
    }
}
