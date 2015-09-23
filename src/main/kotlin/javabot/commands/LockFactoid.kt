package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import javabot.model.Factoid

import javax.inject.Inject

public class LockFactoid : AdminCommand() {
    Param(primary = true)
    var name: String

    Inject
    private val dao: FactoidDao? = null

    override fun canHandle(message: String): Boolean {
        return "lock" == message || "unlock" == message
    }

    override fun execute(event: Message) {
        val command = args.get(0)
        if ("lock" == command || "unlock" == command) {
            val factoid = dao!!.getFactoid(name)
            if (factoid == null) {
                bot.postMessageToChannel(event, Sofia.factoidUnknown(name))
            } else if ("lock" == command) {
                factoid.locked = true
                dao.save(factoid)
                bot.postMessageToChannel(event, name + " locked.")
            } else if ("unlock" == command) {
                factoid.locked = false
                dao.save(factoid)
                bot.postMessageToChannel(event, name + " unlocked.")
            }
        }
    }
}
