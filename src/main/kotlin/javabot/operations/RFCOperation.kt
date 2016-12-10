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

/**
 * Displays RFC url and title
 */
@Singleton class RFCOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.toLowerCase()
        if (message.startsWith(prefix)) {
            val rfcText = message.substring(prefix.length).trim()
            if (rfcText.isEmpty()) {
                responses.add(Message(event, Sofia.rfcMissing()))
            } else {
                try {
                    val data = load(Integer.parseInt(rfcText))
                    responses.add(Message(event, Sofia.rfcSucceed(data.first, data.second)))
                } catch (e: NumberFormatException) {
                    responses.add(Message(event, Sofia.rfcInvalid(rfcText)))
                } catch (e: HttpStatusException) {
                    responses.add(Message(event, Sofia.rfcFail(rfcText)))
                } catch (e: IOException) {
                    responses.add(Message(event, Sofia.rfcFail(rfcText)))
                }
            }
        }
        return responses
    }

    fun load(rfc: Int): Pair<String, String> {
        val url = "https://tools.ietf.org/html/rfc$rfc"
        val doc = Jsoup.connect(url).get()
        return Pair(url, doc.title())
    }

    companion object {
        val prefix: String = "rfc"
    }
}