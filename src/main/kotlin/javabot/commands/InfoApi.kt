package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javax.inject.Inject

class InfoApi @Inject constructor(bot: Javabot, adminDao: AdminDao, var apiDao: ApiDao) : AdminCommand(bot, adminDao) {

    @Parameter(required = true)
    lateinit var apiName: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val api = apiDao.find(apiName)
        if (api != null) {
            responses.add(Message(event, Sofia.apiLocation(api.name, api.version)))
        } else {
            responses.add(Message(event, Sofia.unknownApi(apiName, event.user.nick)))
        }
        return responses
    }
}