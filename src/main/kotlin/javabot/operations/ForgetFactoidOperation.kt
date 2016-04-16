package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import org.pircbotx.Channel
import javax.inject.Inject

class ForgetFactoidOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var factoidDao: FactoidDao, var channelDao: ChannelDao) :
        BotOperation(bot, adminDao), StandardOperation {

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
                forget(channel, responses, event, message.toLowerCase())
            }
        }
        return responses
    }

    protected fun forget(channel: Channel?, responses: MutableList<Message>, event: Message, key: String) {
        val factoid = factoidDao.getFactoid(key)
        if (factoid != null) {
            if ((!factoid.locked) || isAdminUser(event.user)) {
                responses.add(Message(event, Sofia.factoidForgotten(key, event.user.nick)))
                factoidDao.delete(event.user.nick, key, location(channelDao, channel))
            } else {
                responses.add(Message(event, Sofia.factoidDeleteLocked(event.user.nick)))
            }
        } else {
            responses.add(Message(event, Sofia.factoidDeleteUnknown(key, event.user.nick)))
        }
    }
}
