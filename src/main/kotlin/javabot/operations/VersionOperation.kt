package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.jar.Manifest

class VersionOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao), StandardOperation {
    var lastInvocationTime = LocalDateTime.of(1992, 10, 17, 9, 0)

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if ("version".equals(message, ignoreCase = true)) {
            if (LocalDateTime.now() > lastInvocationTime.plus(5, ChronoUnit.MINUTES)) {
                lastInvocationTime=LocalDateTime.now()

                responses.add(Message(event, Sofia.botVersion(loadVersion())))
            }
        }
        return responses
    }

    fun loadVersion(): String {
        val mf = Manifest();
        val manifestResource=Thread.currentThread().getContextClassLoader().getResourceAsStream("/META-INF/MANIFEST.MF")
        if(manifestResource!=null) {
            mf.read(manifestResource);

            val atts = mf.getMainAttributes();

            return atts.getValue("Implementation-Build");
        } else {
            return "UNKNOWN"
        }
    }

}