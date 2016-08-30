package javabot.web.views

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.google.inject.name.Named
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.model.Admin
import javax.inject.Qualifier
import javax.servlet.http.HttpServletRequest

class AdminIndexView @Inject constructor(
        adminDao: AdminDao,
        channelDao: ChannelDao,
        factoidDao: FactoidDao,
        apiDao: ApiDao,
        @Assisted request: HttpServletRequest,
        @Assisted("current") val current: Admin,
        @Assisted("editing") val editing: Admin?) :
        MainView(adminDao, channelDao, factoidDao, apiDao, request) {

    fun getAdmins(): List<Admin> {
        return adminDao.findAll()
    }

    override fun getChildView(): String {
        return "admin/index.ftl"
    }
}
