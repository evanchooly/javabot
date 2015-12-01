package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import java.time.Duration
import java.time.Instant.now
import javax.inject.Inject

public class StatsOperation : BotOperation() {
    @Inject
    lateinit var factoidDao: FactoidDao
    private var numberOfMessages = 0

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        numberOfMessages++
        val message = event.value
        if ("stats".equals(message, ignoreCase = true)) {
            responses.add(Message(event, Sofia.botStats(Duration.between(now(), startTime).toDays(), numberOfMessages, factoidDao.count())))
        }
        return responses
    }

    companion object {
        private val startTime = now()
    }
}