package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ConfigDao
import javabot.operations.BotOperation
import org.apache.commons.lang.StringUtils
import javax.inject.Inject

class ListOperations @Inject constructor(bot: Javabot, adminDao: AdminDao, var configDao: ConfigDao) : OperationsCommand(bot, adminDao) {

    private val operation by lazy {
        configDao.list(BotOperation::class.java)
    }

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val list = operation
        responses.add(Message(event, Sofia.adminKnownOperations(event.user.nick,
                StringUtils.join(list.iterator(), ","))))

        listCurrent(event, responses)
        responses.add(Message(event, Sofia.adminOperationInstructions()))
        return responses
    }
}