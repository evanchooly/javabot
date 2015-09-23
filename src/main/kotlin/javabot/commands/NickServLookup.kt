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
    Inject
    private val nickServDao: NickServDao? = null

    Param
    var name: String

    override fun execute(event: Message) {
        try {
            val info = validateNickServAccount()
            if (info == null) {
                javabot.postMessageToUser(event.user, Sofia.noNickservEntry(name))
            } else {
                info.toNickServFormat().stream().forEach({ line -> javabot.postMessageToUser(event.user, line) })
            }
        } catch (e: ConditionTimeoutException) {
            javabot.postMessageToUser(event.user, Sofia.nickservNotResponding())
        }

    }

    private fun validateNickServAccount(): NickServInfo? {
        val info = AtomicReference(nickServDao!!.find(name))
        if (info.get() == null) {
            ircBot.sendIRC().message("NickServ", "info " + name)
            Awaitility.await().atMost(10, TimeUnit.SECONDS).until<Any> {
                info.set(nickServDao.find(name))
                info.get() != null
            }
        }
        return info.get()
    }

}