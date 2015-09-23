package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ApiDao
import javabot.javadoc.JavadocApi
import org.pircbotx.Channel

import javax.inject.Inject

public class DropApi : AdminCommand() {
    Inject
    private val dao: ApiDao? = null
    Param
    var name: String

    override fun execute(event: Message) {
        val api = dao!!.find(name)
        if (api != null) {
            drop(event, api)
        } else {
            bot.postMessageToChannel(event, Sofia.unknownApi(name, event.user.nick))
        }
    }

    private fun drop(event: Message, api: JavadocApi) {
        bot.postMessageToChannel(event, Sofia.adminRemovingOldJavadoc(api.name))
        dao!!.delete(api)
        bot.postMessageToChannel(event, Sofia.adminDoneRemovingOldJavadoc(api.name))
    }
}