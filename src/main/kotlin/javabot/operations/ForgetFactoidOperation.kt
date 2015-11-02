package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import javax.inject.Inject

public class ForgetFactoidOperation : BotOperation(), StandardOperation {
    @Inject
    lateinit var factoidDao: FactoidDao

    override fun handleMessage(event: Message): Boolean {
        val channel = event.channel
        var message = event.value
        var handled = false
        if (message.startsWith("forget ")) {
            if ((channel == null || !channel.name.startsWith("#")) && !isAdminUser(event.user)) {
                bot.postMessageToChannel(event, Sofia.privmsgChange())
            } else {
                message = message.substring("forget ".length)
                if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
                    message = message.substring(0, message.length - 1)
                }
                handled = forget(event, message.toLowerCase())
            }
        }
        return handled
    }

    protected fun forget(event: Message, key: String): Boolean {
        val factoid = factoidDao.getFactoid(key)
        if (factoid != null) {
            if ((!factoid.locked) || isAdminUser(event.user)) {
                bot.postMessageToChannel(event, Sofia.factoidForgotten(key, event.user.nick))
                factoidDao.delete(event.user.nick, key)
            } else {
                bot.postMessageToChannel(event, Sofia.factoidDeleteLocked(event.user.nick))
            }
        } else {
            bot.postMessageToChannel(event, Sofia.factoidDeleteUnknown(key, event.user.nick))
        }

        return true
    }
}
