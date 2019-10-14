package javabot.operations

import com.google.inject.Inject
import com.rivescript.Config
import com.rivescript.RiveScript
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class BrowseOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    private val rivescript: RiveScript = RiveScript(Config.utf8())

    init {
        RiveScript::class.java.getResourceAsStream("/rive/browse.rive").use {
            rivescript.loadInputStream(it)
        }
        // must be the last operation in the constructor after scripts are loaded
        rivescript.sortReplies()
    }

    override fun handleMessage(event: Message): List<Message> {
        return listOfNotNull(rivescript.reply(event.user.nick, event.value))
                .filter { !it.startsWith("ERR:") }
                .map {
            Message(event, it)
        }
    }
}