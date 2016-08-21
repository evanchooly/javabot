package javabot.web.views

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javax.servlet.http.HttpServletRequest

class IndexView @Inject constructor(
        adminDao: AdminDao,
        channelDao: ChannelDao,
        factoidDao: FactoidDao,
        apiDao: ApiDao,
        @Assisted request: HttpServletRequest) : MainView(adminDao, channelDao, factoidDao, apiDao, request) {

    override fun getChildView(): String {
        return "/index.ftl"
    }
}
