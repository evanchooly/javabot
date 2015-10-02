package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Message
import javabot.dao.FactoidDao
import javax.inject.Inject

public class LockFactoid : AdminCommand() {

    @Parameter
    lateinit var args: MutableList<String>

    @Inject
    lateinit var factoidDao: FactoidDao

    override fun execute(event: Message) {
        val factoidName = args.join(" ")
        val factoid = factoidDao.getFactoid(factoidName)
        if (factoid == null) {
            bot.postMessageToChannel(event, Sofia.factoidUnknown(factoidName))
        } else {
            factoid.locked = true
            factoidDao.save(factoid)
            bot.postMessageToChannel(event, "${factoidName} locked.")
        }
    }
}
