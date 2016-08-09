package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.inject.Inject
import com.google.inject.Singleton
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import org.jsoup.Jsoup
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * Displays RFC url and title
 */
@Singleton class RFCOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    var rfcTitleCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.HOURS).recordStats().build(
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
            val rfcText = message.substring(prefix.length).trim()
            if(rfcText.isEmpty()) {
                responses.add(Message(event, Sofia.rfcMissing()))
            } else {
                try {
                    val rfc = Integer.parseInt(rfcText)
                    try {
                        val url = "http://www.rfc-base.org/rfc-%d.html".format(rfc)
                        responses.add(Message(event, Sofia.rfcSucceed(url, rfcTitleCache.get(url))))
                    } catch (e: ExecutionException) {
                        // from rfc.fail
                        responses.add(Message(event, Sofia.rfcFail(rfcText)))
                    }

                } catch (e: NumberFormatException) {
                    responses.add(Message(event, Sofia.rfcInvalid(rfcText)))
                }
            }
        }
        return responses
    }

    companion object {
        val prefix: String = "rfc"
    }
}