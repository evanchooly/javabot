package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import org.pircbotx.Channel
import org.pircbotx.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class LeaveOperation : BotOperation() {

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        val channel = event.channel
        val sender = event.user
        if ("leave" == message.toLowerCase()) {
            if (channel.name.equalsIgnoreCase(event.user.nick)) {
                bot.postMessageToChannel(event, Sofia.leavePrivmsg(sender.nick))
            } else {
                bot.postMessageToChannel(event, Sofia.leaveChannel(event.user.nick))
                Thread { //                        ircBot.getUserChannelDao().getChannel(channel.getName());
                    try {
                        Thread.sleep((60000 * 15).toLong())
                    } catch (exception: InterruptedException) {
                        log.error(exception.getMessage(), exception)
                    }
                    //                        getBot().getPircBot().joinChannel(channel);
                }.start()
            }
        }
        return false
    }

    companion object {
        private val log = LoggerFactory.getLogger(LeaveOperation::class.java)
    }
}
