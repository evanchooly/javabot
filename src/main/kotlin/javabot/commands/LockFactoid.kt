package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.FactoidDao
import javax.inject.Inject

class LockFactoid
@Inject
constructor(bot: Javabot, adminDao: AdminDao, var factoidDao: FactoidDao) :
    AdminCommand(bot, adminDao) {

    @Parameter lateinit var args: MutableList<String>

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val factoidName = args.joinToString(" ")
        val factoid = factoidDao.getFactoid(factoidName)
        if (factoid == null) {
            responses.add(Message(event, Sofia.factoidUnknown(factoidName)))
        } else {
            factoid.locked = true
            factoidDao.save(factoid)
            responses.add(Message(event, "${factoidName} locked."))
        }
        return responses
    }
}
