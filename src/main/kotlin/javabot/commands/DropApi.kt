package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.ApiDao
import javabot.javadoc.JavadocApi
import org.pircbotx.PircBotX
import javax.inject.Inject
import javax.inject.Provider

class DropApi @Inject constructor(javabot: Provider<Javabot>, ircBot: Provider<PircBotX>, var apiDao: ApiDao) :
        AdminCommand(javabot, ircBot) {
    @Parameter(required = true)
    lateinit var apiName: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val api = apiDao.find(apiName)
        if (api != null) {
            drop(responses, event, api)
        } else {
            responses.add(Message(event, Sofia.unknownApi(apiName, event.user.nick)))
        }
        return responses
    }

    private fun drop(responses: MutableList<Message>, event: Message, api: JavadocApi) {
        responses.add(Message(event, Sofia.adminRemovingOldJavadoc(api.name)))
        apiDao.delete(api)
        responses.add(Message(event, Sofia.adminDoneRemovingOldJavadoc(api.name)))
    }
}