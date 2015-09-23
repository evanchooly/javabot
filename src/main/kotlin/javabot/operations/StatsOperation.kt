package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao

import javax.inject.Inject
import java.time.Duration
import java.time.Instant

import java.time.Instant.now

public class StatsOperation : BotOperation() {
    Inject
    private val factoidDao: FactoidDao? = null
    private var numberOfMessages = 0

    override fun handleMessage(event: Message): Boolean {
        numberOfMessages++
        val message = event.value
        if ("stats".equalsIgnoreCase(message)) {
            bot.postMessageToChannel(event,
                  Sofia.botStats(Duration.between(now(), startTime).toDays(), numberOfMessages, factoidDao!!.count()))
            return true
        }
        return false
    }

    companion object {
        private val startTime = now()
    }
}