package javabot.dao;

import javabot.BotListener;
import javabot.model.NickServInfo;
import org.pircbotx.User;
import org.pircbotx.hooks.events.NoticeEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Test
public class NickServDaoTest extends BaseServiceTest {
    @Inject
    private NickServDao nickServDao;
    @Inject
    private BotListener botListener;

    public void parseNickServResponse() {
        nickServDao.clear();
        List<String> list = new ArrayList<>(
                                               Arrays.asList("Information on cheeser (account cheeser):",
                                                             "Registered : Feb 20 21:31:56 2002 (12 years, 10 weeks, 2 days, 04:48:12 ago)",
                                                             "Last seen  : now",
                                                             "Flags      : HideMail, Private",
                                                             "cheeser has enabled nick protection",
                                                             "*** End of Info ***"
                                                            ));
        nickServDao.process(list);
    }

    public void privMsg() {
        nickServDao.clear();
        for (int i = 0; i < 5; i++) {
            send(getNickServInfo("account" + i, "nick" + i, LocalDateTime.of(2014, Month.MARCH, 1, 16, 30),
                                 LocalDateTime.of(2014, Month.MARCH, 1, 16, 30), LocalDateTime.now()));
        }
        for (int i = 0; i < 5; i++) {
            Assert.assertNotNull(nickServDao.find("account" + i), "Should find account" + i);
        }
    }

    private void send(final NickServInfo info) {
        User user = getIrcBot().getUserChannelDao().getUser("nickserv");
        info.toNickServFormat().stream().forEach(o -> botListener.onNotice(new NoticeEvent<>(getIrcBot(), user, null, o)));
    }

    private NickServInfo getNickServInfo(final String account, final String nick, final LocalDateTime registered,
                                         final LocalDateTime userRegistered, final LocalDateTime lastSeen) {
        NickServInfo info = new NickServInfo();
        info.setAccount(account);
        info.setNick(nick);
        info.setRegistered(registered);
        info.setUserRegistered(userRegistered);
        info.setLastSeen(lastSeen);
        info.setLastAddress("last address 1");

        return info;
    }
}