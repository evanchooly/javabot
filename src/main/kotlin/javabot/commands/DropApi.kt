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