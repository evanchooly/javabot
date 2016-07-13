package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.javadoc.JavadocApi
import javax.inject.Inject

class DropApi @Inject constructor(bot: Javabot, adminDao: AdminDao, var apiDao: ApiDao) : AdminCommand(bot, adminDao) {
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