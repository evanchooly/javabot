package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Message
import javabot.dao.ApiDao
import javabot.javadoc.JavadocApi
import javax.inject.Inject

public class DropApi : AdminCommand() {
    @Inject
    lateinit var apiDao: ApiDao
    @Parameter
    lateinit var apiName: String

    override fun execute(event: Message) {
        val api = apiDao.find(apiName)
        if (api != null) {
            drop(event, api)
        } else {
            bot.postMessageToChannel(event, Sofia.unknownApi(apiName, event.user.nick))
        }
    }

    private fun drop(event: Message, api: JavadocApi) {
        bot.postMessageToChannel(event, Sofia.adminRemovingOldJavadoc(api.name))
        apiDao.delete(api)
        bot.postMessageToChannel(event, Sofia.adminDoneRemovingOldJavadoc(api.name))
    }
}