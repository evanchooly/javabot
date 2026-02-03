package javabot.web.views

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import jakarta.servlet.http.HttpServletRequest
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao

class IndexView
@Inject
constructor(
    adminDao: AdminDao,
    channelDao: ChannelDao,
    factoidDao: FactoidDao,
    apiDao: ApiDao,
    @Assisted request: HttpServletRequest,
) : MainView(adminDao, channelDao, factoidDao, apiDao, request) {

    override fun getChildView(): String {
        return "/index.ftl"
    }
}
