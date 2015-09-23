package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ApiDao
import javabot.javadoc.JavadocApi

import javax.inject.Inject

public class InfoApi : AdminCommand() {
    Inject
    private val dao: ApiDao? = null

    Param
    var name: String

    override fun execute(event: Message) {
        val api = dao!!.find(name)
        if (api != null) {
            bot.postMessageToChannel(event, Sofia.apiLocation(api.name, api.baseUrl))
        } else {
            bot.postMessageToChannel(event, Sofia.unknownApi(name, event.user.nick))
        }
    }
}