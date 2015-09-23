package javabot.kotlin.web.views

import com.google.inject.Injector
import javabot.model.Channel

import javax.servlet.http.HttpServletRequest

public class ChannelEditView(injector: Injector, request: HttpServletRequest, public val channel: Channel) : MainView(injector, request) {

    override fun getChildView(): String {
        return "admin/editChannel.ftl"
    }
}
