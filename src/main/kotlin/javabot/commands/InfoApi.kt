package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ApiDao
import javax.inject.Inject

public class InfoApi : AdminCommand() {
    @Inject
    lateinit val apiDao: ApiDao

    @Param
    lateinit var apiName: String

    override fun execute(event: Message) {
        val api = apiDao.find(apiName)
        if (api != null) {
            bot.postMessageToChannel(event, Sofia.apiLocation(api.name, api.baseUrl!!))
        } else {
            bot.postMessageToChannel(event, Sofia.unknownApi(apiName, event.user.nick))
        }
    }
}