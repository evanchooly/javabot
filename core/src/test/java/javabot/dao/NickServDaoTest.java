package javabot.dao;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

import javabot.model.NickServInfo;
import org.junit.Assert;
import org.testng.annotations.Test;

@Test
public class NickServDaoTest extends BaseServiceTest {
  @Inject
  private NickServDao nickServDao;

  public void parseNickServResponse() {
    nickServDao.clear();
    List<String> list = new ArrayList<>(Arrays.asList(
        "Information on cheeser (account cheeser):",
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
      Assert.assertNotNull("Should find account" + i, nickServDao.find("account" + i));
    }
  }

  private void send(final NickServInfo info) {
    info.toNickServFormat().stream().forEach(o -> {
      getJavabot().getPircBot().onNotice("nickserv", "nickserv", "", "", o);
    });
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