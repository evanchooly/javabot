package javabot.dao

import javabot.BotListener
import javabot.model.NickServInfo
import org.pircbotx.hooks.events.NoticeEvent
import org.testng.Assert
import org.testng.annotations.Test
import java.time.LocalDateTime
import java.time.Month
import java.util.Arrays.asList
import javax.inject.Inject

@Test class NickServDaoTest : BaseServiceTest() {
    @Inject
    protected lateinit var botListener: BotListener

    fun parseNickServResponse() {
        nickServDao.clear()
        val list = asList("Information on cheeser (account cheeser):",
              "Registered : Feb 20 21:31:56 2002 (12 years, 10 weeks, 2 days, 04:48:12 ago)",
              "Last seen  : now",
              "Flags      : HideMail, Private",
              "cheeser has enabled nick protection",
              "*** End of Info ***")
        nickServDao.process(list)
    }

    fun privMsg() {
        nickServDao.clear()
        for (i in 0..4) {
            send(getNickServInfo("account" + i, "nick" + i, LocalDateTime.of(2014, Month.MARCH, 1, 16, 30),
                  LocalDateTime.of(2014, Month.MARCH, 1, 16, 30), LocalDateTime.now()))
        }
        for (i in 0..4) {
            Assert.assertNotNull(nickServDao.find("account" + i), "Should find account" + i)
        }
    }

    private fun send(info: NickServInfo) {
        val user = ircBot.get().userChannelDao.getUser("nickserv")
        info.toNickServFormat().forEach({ o -> botListener.onNotice(NoticeEvent(ircBot.get(), user, null, o)) })
    }

    private fun getNickServInfo(account: String, nick: String, registered: LocalDateTime,
                                userRegistered: LocalDateTime, lastSeen: LocalDateTime): NickServInfo {
        val info = NickServInfo()
        info.account = account
        info.nick = nick
        info.registered = registered
        info.userRegistered = userRegistered
        info.lastSeen = lastSeen
        info.lastAddress = "last address 1"

        return info
    }
}