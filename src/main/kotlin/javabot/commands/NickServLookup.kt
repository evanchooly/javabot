package javabot.commands

import com.antwerkz.sofia.Sofia
import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.core.ConditionTimeoutException
import javabot.Message
import javabot.dao.NickServDao
import javabot.model.NickServInfo

import javax.inject.Inject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

public class NickServLookup : AdminCommand() {
    @Inject
    lateinit val nickServDao: NickServDao

    @Param
    lateinit var nick: String

    override fun execute(event: Message) {
        try {
            val info = validateNickServAccount()
            if (info == null) {
                javabot.get().postMessageToUser(event.user, Sofia.noNickservEntry(nick))
            } else {
                info.toNickServFormat().forEach({ line -> javabot.get().postMessageToUser(event.user, line) })
            }
        } catch (e: ConditionTimeoutException) {
            javabot.get().postMessageToUser(event.user, Sofia.nickservNotResponding())
        }

    }

    private fun validateNickServAccount(): NickServInfo? {
        val info = AtomicReference(nickServDao!!.find(nick))
        if (info.get() == null) {
            pircBot.get().sendIRC().message("NickServ", "info " + nick)
            Awaitility.await().atMost(10, TimeUnit.SECONDS).until<Any> {
                info.set(nickServDao.find(nick))
                info.get() != null
            }
        }
        return info.get()
    }

}