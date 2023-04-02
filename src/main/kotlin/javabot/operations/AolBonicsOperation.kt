package javabot.operations

import com.antwerkz.sofia.Sofia
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import java.util.TreeSet
import javax.inject.Inject

class AolBonicsOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    private val phrases = TreeSet<String>()

    init {
        phrases.add("u")
        phrases.add("omg")
        phrases.add("plz")
        phrases.add("r")
        phrases.add("lolz")
        phrases.add("l33t")
        phrases.add("1337")
        phrases.add("kewl")
        phrases.add("ppl")
        phrases.add("ru")
        phrases.add("ur")
        phrases.add("j00")
        phrases.add("cuz")
        phrases.add("coz")
        // Slightly questionable....
        // phrases.add("lol");
        phrases.add("ftw")
    }

    override fun handleChannelMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        for (bad in event.value.split(" ")) {
            if (phrases.contains(bad.lowercase(Locale.getDefault()).replace("!|\\.|\\?|,".toRegex(), ""))) {
                responses.add(Message(event, Sofia.botAolbonics(event.user.nick)))
            }
        }
        return responses
    }
}
