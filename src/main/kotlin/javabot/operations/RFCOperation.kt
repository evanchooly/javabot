package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Singleton
import javabot.Message
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.pircbotx.Channel

import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * Displays RFC url and title
 */
Singleton
public class RFCOperation : BotOperation() {
    var rfcTitleCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.HOURS).recordStats().build(
          object : CacheLoader<String, String>() {
              SuppressWarnings("NullableProblems")
              Throws(IOException::class)
              override fun load(url: String): String {
                  val doc = Jsoup.connect(url).get()
                  return doc.title()
              }
          })

    override fun handleMessage(event: Message): Boolean {
        val message = event.value.toLowerCase()
        if (message.startsWith(prefix)) {
            val rfcText = message.substring(prefix.length()).trim()
            try {
                val rfc = Integer.parseInt(rfcText)
                try {
                    val url = String.format("http://www.faqs.org/rfcs/rfc%d.html", rfc)
                    bot.postMessageToChannel(event, Sofia.rfcSucceed(url, rfcTitleCache.get(url)))
                } catch (e: ExecutionException) {
                    // from rfc.fail
                    bot.postMessageToChannel(event, Sofia.rfcFail(rfcText))
                }

            } catch (e: NumberFormatException) {
                bot.postMessageToChannel(event, Sofia.rfcInvalid(rfcText))
            }

            return true
        }
        return false
    }

    companion object {
        public val prefix: String = "rfc "
    }
}