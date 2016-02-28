package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import java.util.Random

class UnixCommandOperation : BotOperation() {
    private val commands = sortedSetOf("rm","ls","clear")
    private val insults = listOf("genius", "Einstein","pal","buddy")

    private val random = Random()

    override fun handleChannelMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        val split = message.split(" ")
        if (split.size != 0 && split.size < 3 && commands.contains(split[0])) {
            responses.add(Message(event, Sofia.botUnixCommand(event.user.nick, getInsult())))
        }
        return responses
    }

    private fun getInsult(): String {
        return insults[random.nextInt(insults.size)]
    }
}