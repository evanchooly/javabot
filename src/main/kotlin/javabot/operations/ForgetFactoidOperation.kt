package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import javax.inject.Inject

public class ForgetFactoidOperation : BotOperation(), StandardOperation {
    @Inject
    lateinit var factoidDao: FactoidDao

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val channel = event.channel
        var message = event.value
        if (message.startsWith("forget ")) {
            if ((channel == null || !channel.name.startsWith("#")) && !isAdminUser(event.user)) {
                responses.add(Message(event, Sofia.privmsgChange()))
            } else {
                message = message.substring("forget ".length)
                if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
                    message = message.substring(0, message.length - 1)
                }
                forget(responses, event, message.toLowerCase())
            }
        }
        return responses
    }

    protected fun forget(responses: MutableList<Message>, event: Message, key: String) {
        val factoid = factoidDao.getFactoid(key)
        if (factoid != null) {
            if ((!factoid.locked) || isAdminUser(event.user)) {
                responses.add(Message(event, Sofia.factoidForgotten(key, event.user.nick)))
                factoidDao.delete(event.user.nick, key)
            } else {
                responses.add(Message(event, Sofia.factoidDeleteLocked(event.user.nick)))
            }
        } else {
            responses.add(Message(event, Sofia.factoidDeleteUnknown(key, event.user.nick)))
        }
    }
}
