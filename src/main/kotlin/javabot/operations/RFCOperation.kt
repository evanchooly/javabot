package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import com.google.inject.Singleton
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import java.io.IOException
import java.util.Locale

/**
 * Displays RFC url and title
 */
@Singleton
class RFCOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.lowercase(Locale.getDefault())
        if (message.startsWith(PREFIX)) {
            val rfcText = message.substring(PREFIX.length).trim()
            val parts = rfcText.split(Regex("\\s+"))
            val rfcNumPart = parts.getOrNull(0)
            if (rfcNumPart == null || rfcNumPart == "") {
                responses.add(Message(event, Sofia.rfcMissing()))
            } else {
                val rfcNum = rfcNumPart.toIntOrNull()
                if (rfcNum == null) {
                    responses.add(Message(event, Sofia.rfcInvalid(rfcText)))
                } else {
                    try {
                        val anchorType = parts.getOrNull(1)
                        val name = parts.getOrNull(2)
                        val data = if (name != null && ("section".equals(anchorType, true) || "page".equals(anchorType, true))) {
                            load(rfcNum, "#$anchorType-$name")
                        } else {
                            load(rfcNum)
                        }
                        responses.add(Message(event, Sofia.rfcSucceed(data.first, data.second)))
                    } catch (e: HttpStatusException) {
                        responses.add(Message(event, Sofia.rfcFail(rfcText)))
                    } catch (e: IOException) {
                        responses.add(Message(event, Sofia.rfcFail(rfcText)))
                    }
                }
            }
        }
        return responses
    }

    fun load(rfc: Int, anchor: String = ""): Pair<String, String> {
        val url = "https://tools.ietf.org/html/rfc$rfc$anchor"
        val doc = Jsoup.connect(url).get()
        val meta= doc
            .getElementsByTag("span")
            .first { it.className().equals("h1", true) }
            ?.text()
        return Pair(url, meta ?: doc.title())
    }

    companion object {
        val PREFIX: String = "rfc"
    }
}
