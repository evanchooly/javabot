package javabot.operations

import com.google.inject.Inject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.Charset
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import org.slf4j.LoggerFactory

abstract class UrlOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        var message = event.value
        if (message.startsWith(getTrigger())) {
            message = message.substring(getTrigger().length)
            try {
                responses.add(
                    Message(
                        event,
                        getBaseUrl() +
                            URLEncoder.encode(message, Charset.defaultCharset().displayName()),
                    )
                )
            } catch (e: UnsupportedEncodingException) {
                log.error(e.message, e)
            }
        }
        return responses
    }

    protected abstract fun getBaseUrl(): String

    protected abstract fun getTrigger(): String

    companion object {
        private val log = LoggerFactory.getLogger(UrlOperation::class.java)
    }
}
