package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import javabot.Message
import javabot.dao.FactoidDao
import javax.inject.Inject

public class UnlockFactoid : AdminCommand() {

    @Parameter
    lateinit var args: MutableList<String>

    @Inject
    lateinit var factoidDao: FactoidDao

/*
    override
    protected fun parse(params: MutableList<String>) {
        val cli = message.substring(params[0].length()).trim()
        JCommander(this).parse(cli)
    }
*/

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val factoidName = args.joinToString(" ")
        val factoid = factoidDao.getFactoid(factoidName)
        if (factoid == null) {
            responses.add(Message(event, Sofia.factoidUnknown(factoidName)))
        } else {
            factoid.locked = false
            factoidDao.save(factoid)
            responses.add(Message(event, "${factoidName} unlocked."))
        }

        return responses
    }
}
