package javabot.dao

import com.google.inject.Inject
import com.google.inject.Provider
import javabot.model.NickServInfo
import org.pircbotx.PircBotX

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

public class TestNickServDao : NickServDao() {
    @Inject
    protected lateinit var ircBot: Provider<PircBotX>

    override fun find(name: String): NickServInfo {
        val nickServInfo = NickServInfo(ircBot.get().userChannelDao.getUser(name))
        nickServInfo.registered = LocalDateTime.now().minus(30, ChronoUnit.DAYS)
        return nickServInfo

    }
}
