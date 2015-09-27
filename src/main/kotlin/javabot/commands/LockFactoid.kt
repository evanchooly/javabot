package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import javabot.model.Factoid

import javax.inject.Inject

public class LockFactoid : AdminCommand() {
    @Param(primary = true)
    lateinit var factoidName: String

    @Inject
    lateinit val factoidDao: FactoidDao

    override fun canHandle(message: String): Boolean {
        return "lock" == message || "unlock" == message
    }

    override fun execute(event: Message) {
        val command = args.get(0)
        if ("lock" == command || "unlock" == command) {
            val factoid = factoidDao!!.getFactoid(factoidName)
            if (factoid == null) {
                bot.postMessageToChannel(event, Sofia.factoidUnknown(factoidName))
            } else if ("lock" == command) {
                factoid.locked = true
                factoidDao.save(factoid)
                bot.postMessageToChannel(event, factoidName + " locked.")
            } else if ("unlock" == command) {
                factoid.locked = false
                factoidDao.save(factoid)
                bot.postMessageToChannel(event, factoidName + " unlocked.")
            }
        }
    }
}
