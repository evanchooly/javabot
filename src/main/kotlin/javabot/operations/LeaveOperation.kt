package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class LeaveOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        val channel = event.channel
        val sender = event.user
        if ("leave" == message.lowercase(Locale.getDefault())) {
            if (channel!!.name.equals(event.user.nick, ignoreCase = true)) {
                responses.add(Message(event, Sofia.leavePrivmsg(sender.nick)))
            } else {
                responses.add(Message(event, Sofia.leaveChannel(event.user.nick)))
                //                        ircBot.getUserChannelDao().getChannel(channel.getName());
                //                        getBot().getPircBot().joinChannel(channel);
            }
        }
        return responses
    }
}
