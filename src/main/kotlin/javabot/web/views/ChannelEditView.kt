package javabot.web.views

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.model.Channel
import javax.servlet.http.HttpServletRequest

class ChannelEditView @Inject constructor(
        adminDao: AdminDao,
        channelDao: ChannelDao,
        factoidDao: FactoidDao,
        apiDao: ApiDao,
        @Assisted request: HttpServletRequest,
        @Assisted val channel: Channel) :
        MainView(adminDao, channelDao, factoidDao, apiDao, request) {

    override fun getChildView(): String {
        return "admin/editChannel.ftl"
    }
}
