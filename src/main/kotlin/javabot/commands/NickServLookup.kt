package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.core.ConditionTimeoutException
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.NickServDao
import javabot.model.NickServInfo
import org.pircbotx.PircBotX
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Provider

class NickServLookup  @Inject constructor(bot: Javabot, adminDao: AdminDao, ircBot: com.google.inject.Provider<PircBotX>,
                                          var nickServDao: NickServDao) :
        AdminCommand(bot, adminDao, ircBot) {

    @Parameter(required = true)
    lateinit var nick: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        try {
            val info = validateNickServAccount()
            if (info == null) {
                responses.add(Message(event.user, Sofia.noNickservEntry(nick)))
            } else {
                info.toNickServFormat().forEach({ line -> responses.add(Message(event.user, line)) })
            }
        } catch (e: ConditionTimeoutException) {
            responses.add(Message(event.user, Sofia.nickservNotResponding()))
        }

        return responses
    }

    private fun validateNickServAccount(): NickServInfo? {
        val info = AtomicReference(nickServDao.find(nick))
        if (info.get() == null) {
            ircBot.get().sendIRC().message("NickServ", "info " + nick)
            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .until<Boolean> {
                        info.set(nickServDao.find(nick))
                        info.get() != null
                    }
        }
        return info.get()
    }
}