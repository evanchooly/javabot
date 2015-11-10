package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Message
import javabot.dao.ApiDao
import javax.inject.Inject

public class InfoApi : AdminCommand() {
    @Inject
    lateinit var apiDao: ApiDao

    @Parameter
    lateinit var apiName: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val api = apiDao.find(apiName)
        if (api != null) {
            responses.add(Message(event, Sofia.apiLocation(api.name, api.baseUrl!!)))
        } else {
            responses.add(Message(event, Sofia.unknownApi(apiName, event.user.nick)))
        }
        return responses
    }
}