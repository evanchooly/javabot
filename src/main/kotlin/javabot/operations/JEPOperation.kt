package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.inject.Singleton
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import org.jsoup.Jsoup
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * Astute coders might notice a SLIGHT similarity to RFCOperation... as in, it's copied and pasted directly
 * with references to RFC stuff being renamed to JEP stuff instead.
 */
@Singleton class JEPOperation @com.google.inject.Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    var jepTitleCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.HOURS).recordStats().build(
            object : CacheLoader<String, String>() {
                @SuppressWarnings("NullableProblems")
                @Throws(IOException::class)
                override fun load(url: String): String {
                    val doc = Jsoup.connect(url).get()
                    return doc.title()
                }
            })

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.toLowerCase()
        if (message.startsWith(prefix)) {
            val jepText = message.substring(prefix.length).trim()
            if(jepText.isEmpty()) {
                responses.add(Message(event, Sofia.jepMissing()))
            } else {
                try {
                    val jep = Integer.parseInt(jepText)
                    try {
                        val url = "http://openjdk.java.net/jeps/%d".format(jep)
                        responses.add(Message(event, Sofia.jepSucceed(jepTitleCache.get(url), url)))
                    } catch (e: ExecutionException) {
                        // from jep.fail
                        responses.add(Message(event, Sofia.jepInvalid(jepText)))
                    }

                } catch (e: NumberFormatException) {
                    responses.add(Message(event, Sofia.jepInvalid(jepText)))
                }
            }
        }
        return responses
    }

    companion object {
        val prefix: String = "jep"
    }
}