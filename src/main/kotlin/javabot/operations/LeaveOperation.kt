package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import org.slf4j.LoggerFactory

public class LeaveOperation : BotOperation() {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        val channel = event.channel
        val sender = event.user
        if ("leave" == message.toLowerCase()) {
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
