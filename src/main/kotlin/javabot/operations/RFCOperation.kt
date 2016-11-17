package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.Singleton
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import org.jsoup.Jsoup
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * Displays RFC url and title
 */
@Singleton class RFCOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    val rfcServerList = listOf("http://www.rfc-base.org/rfc-%d.html", "https://tools.ietf.org/html/rfc%d")
    val rfcBadServers = mutableListOf<String>()

    val rfcTitleCache: LoadingCache<Int, Pair<String, String>> =
            CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.HOURS).recordStats().build(
                    object : CacheLoader<Int, Pair<String, String>>() {
                        @SuppressWarnings("NullableProblems")
                        override fun load(rfc: Int): Pair<String, String> {
                            rfcServerList
                                    .filterNot { rfcBadServers.contains(it) }
                                    .forEach {
                                        // how I wish return@label worked in the lambda...
                                        val url = it.format(rfc)
                                        try {
                                            val doc = Jsoup.connect(url).get()
                                            return Pair(url, doc.title())
                                        } catch(e: UnknownHostException) {
                                            rfcBadServers.add(it)
                                        }
                                    }
                            throw IOException("RFC $rfc not retrievable")
                        }
                    })

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.toLowerCase()
        if (message.startsWith(prefix)) {
            val rfcText = message.substring(prefix.length).trim()
            if (rfcText.isEmpty()) {
                responses.add(Message(event, Sofia.rfcMissing()))
            } else {
                try {
                    val rfc = Integer.parseInt(rfcText)
                    val data = rfcTitleCache.get(rfc)
                    responses.add(Message(event, Sofia.rfcSucceed(data.first, data.second)))
                } catch (e: NumberFormatException) {
                    responses.add(Message(event, Sofia.rfcInvalid(rfcText)))
                } catch (e: ExecutionException) {
                    responses.add(Message(event, Sofia.rfcFail(rfcText)))
                }
            }
        }
        return responses
    }

    companion object {
        val prefix: String = "rfc"
    }
}