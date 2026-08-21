package javabot.operations

import com.antwerkz.sofia.Sofia
import jakarta.inject.Inject
import java.io.InputStream
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class VersionOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    BotOperation(bot, adminDao), StandardOperation {
    var lastInvocationTime = LocalDateTime.of(1992, 10, 17, 9, 0)

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if ("version".equals(message, ignoreCase = true)) {
            if (LocalDateTime.now() > lastInvocationTime.plus(5, ChronoUnit.MINUTES)) {
                lastInvocationTime = LocalDateTime.now()

                responses.add(Message(event, Sofia.botVersion(loadVersion())))
            }
        }
        return responses
    }

    fun loadVersion(): String {
        val properties = Properties()
        val versionProperties: InputStream? =
            this.javaClass.getResourceAsStream("/version.properties")
        if (versionProperties != null) {
            properties.load(versionProperties)
            val version = properties.getProperty("build.version", "UNKNOWN")
            if (version.isEmpty()) {
                return "UNKNOWN"
            } else {
                return version
            }
        } else {
            return "UNKNOWN"
        }
    }
}
