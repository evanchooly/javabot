package javabot.dao

import javabot.IrcAdapter
import javabot.MockIrcUser
import javabot.model.JavabotUser
import javabot.model.NickServInfo
import org.pircbotx.PircBotX
import org.pircbotx.hooks.events.NoticeEvent
import org.testng.Assert
import org.testng.annotations.Test
import java.time.LocalDateTime
import java.time.Month
import java.util.Arrays.asList
import javax.inject.Inject
import javax.inject.Provider

@Test
class NickServDaoTest @Inject constructor(val nickServDao: NickServDao, val ircAdapter: IrcAdapter,
                                          private var ircBot: Provider<PircBotX>) : BaseServiceTest() {
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
        info.toNickServFormat().forEach { o ->
            ircAdapter.onNotice(NoticeEvent(ircBot.get(), MockIrcUser(JavabotUser("nickserv")), null, o))
        }
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