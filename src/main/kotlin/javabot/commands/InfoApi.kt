package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import org.pircbotx.PircBotX
import javax.inject.Inject
import javax.inject.Provider

class InfoApi @Inject constructor(bot: Javabot, adminDao: AdminDao, ircBot: Provider<PircBotX>, var apiDao: ApiDao) :
        AdminCommand(bot, adminDao, ircBot) {

    @Parameter(required = true)
    lateinit var apiName: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val api = apiDao.find(apiName)
        if (api != null) {
            responses.add(Message(event, Sofia.apiLocation(api.name, api.baseUrl)))
        } else {
            responses.add(Message(event, Sofia.unknownApi(apiName, event.user.nick)))
        }
        return responses
    }
}