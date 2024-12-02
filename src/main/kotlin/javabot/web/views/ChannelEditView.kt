package javabot.web.views

import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.model.Channel

class ChannelEditView
@Inject
constructor(
    adminDao: AdminDao,
    channelDao: ChannelDao,
    factoidDao: FactoidDao,
    apiDao: ApiDao,
    request: HttpServletRequest,
    val channel: Channel
) : MainView(adminDao, channelDao, factoidDao, apiDao, request) {

    override fun getChildView(): String {
        return "admin/editChannel.ftl"
    }
}
