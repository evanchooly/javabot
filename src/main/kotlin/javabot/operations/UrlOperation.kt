package javabot.operations

import javabot.Message
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.Charset

public abstract class UrlOperation : BotOperation() {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        var message = event.value
        if (message.startsWith(getTrigger())) {
            message = message.substring(getTrigger().length)
            try {
                responses.add(Message(event, getBaseUrl() + URLEncoder.encode(message, Charset.defaultCharset().displayName())))
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