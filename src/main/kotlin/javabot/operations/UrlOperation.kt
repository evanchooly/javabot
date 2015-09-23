package javabot.operations

import javabot.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.Charset

public abstract class UrlOperation : BotOperation() {

    override fun handleMessage(event: Message): Boolean {
        var message = event.value
        if (message.startsWith(getTrigger())) {
            message = message.substring(getTrigger().length())
            try {
                bot.postMessageToChannel(event, getBaseUrl() + URLEncoder.encode(message, Charset.defaultCharset().displayName()))
                return true
            } catch (e: UnsupportedEncodingException) {
                log.error(e.getMessage(), e)
            }

        }
        return false
    }

    protected abstract fun getBaseUrl(): String

    protected abstract fun getTrigger(): String

    companion object {
        private val log = LoggerFactory.getLogger(UrlOperation::class.java)
    }
}