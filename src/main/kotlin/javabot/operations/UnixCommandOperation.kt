package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import org.pircbotx.Channel

import java.util.ArrayList
import java.util.Random
import java.util.TreeSet

public class UnixCommandOperation : BotOperation() {
    private val commands = TreeSet<String>()
    private val insults = ArrayList<String>()
    private val random: Random

    init {
        commands.add("rm")
        commands.add("ls")
        commands.add("clear")

        insults.add("dumbass")
        insults.add("genius")
        insults.add("Einstein")
        insults.add("pal")

        random = Random()
    }

    override fun handleChannelMessage(event: Message): Boolean {
        val message = event.value
        val split = message.split(" ")
        if (split.size != 0 && split.size < 3 && commands.contains(split[0])) {
            bot.postMessageToChannel(event, Sofia.botUnixCommand(event.user.nick, getInsult()))
            return true
        }
        return false
    }

    private fun getInsult(): String {
        return insults.get(random.nextInt(insults.size))
    }
}