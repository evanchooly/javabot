package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Simple operation to pull who added the factoid and when it was added
 */
public class InfoOperation : BotOperation() {

    @Inject
    lateinit val factoidDao: FactoidDao

    override fun handleMessage(event: Message): Boolean {
        val message = event.value.toLowerCase()
        if (message.startsWith("info ")) {
            val key = message.substring("info ".length())
            val factoid = factoidDao.getFactoid(key)
            if (factoid != null) {
                val formatter = DateTimeFormatter.ofPattern(INFO_DATE_FORMAT)
                val updated = factoid.updated
                val formatted = formatter.format(updated)
                bot.postMessageToChannel(event, Sofia.factoidInfo(key, if (factoid.locked) "*" else "", factoid.userName,
                      formatted, factoid.value))
            } else {
                bot.postMessageToChannel(event, Sofia.factoidUnknown(key))
            }
            return true
        }
        return false
    }

    companion object {
        public val INFO_DATE_FORMAT: String = "dd MMM yyyy' at 'KK:mm"
    }

}