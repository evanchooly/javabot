package javabot.dao

import com.google.common.collect.ImmutableMap.of
import java.time.LocalDateTime
import java.time.Month
import java.util.Arrays.asList
import javabot.IrcAdapter
import javabot.MockIrcUser
import javabot.MockUserHostmask
import javabot.model.JavabotUser
import javabot.model.NickServInfo
import javax.inject.Inject
import org.pircbotx.hooks.events.NoticeEvent
import org.testng.Assert
import org.testng.annotations.Test

@Test
class NickServDaoTest
@Inject
constructor(val nickServDao: NickServDao, val ircAdapter: IrcAdapter) : BaseServiceTest() {
    fun parseNickServResponse() {
        nickServDao.clear()
        val list =
            asList(
                "Information on cheeser (account cheeser):",
                "Registered : Feb 20 21:31:56 2002 (12 years, 10 weeks, 2 days, 04:48:12 ago)",
                "Last seen  : now",
                "Flags      : HideMail, Private",
                "cheeser has enabled nick protection",
                "*** End of Info ***",
            )
        nickServDao.process(list)
    }

    fun privMsg() {
        nickServDao.clear()
        for (i in 0..4) {
            send(
                getNickServInfo(
                    "account" + i,
                    "nick" + i,
                    LocalDateTime.of(2014, Month.MARCH, 1, 16, 30),
                    LocalDateTime.of(2014, Month.MARCH, 1, 16, 30),
                    LocalDateTime.now(),
                )
            )
        }
        for (i in 0..4) {
            Assert.assertNotNull(nickServDao.find("account" + i), "Should find account" + i)
        }
    }

    private fun send(info: NickServInfo) {
        info.toNickServFormat().forEach { o ->
            val user = JavabotUser("nickserv")
            ircAdapter.onNotice(
                NoticeEvent(
                    ircBot.get(),
                    MockUserHostmask(ircBot.get(), user.nick),
                    MockIrcUser(ircBot.get(), user.nick),
                    null,
                    "",
                    o,
                    of(),
                )
            )
        }
    }

    private fun getNickServInfo(
        account: String,
        nick: String,
        registered: LocalDateTime,
        userRegistered: LocalDateTime,
        lastSeen: LocalDateTime,
    ): NickServInfo {
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
