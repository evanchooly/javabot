package javabot.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javabot.model.NickServInfo;
import static javabot.model.NickServInfo.NSERV_DATE_FORMAT;
import javabot.model.criteria.NickServInfoCriteria;

public class NickServDao extends BaseDao<NickServInfo> {
  protected NickServDao() {
    super(NickServInfo.class);
  }

  public void clear() {
    ds.delete(getQuery());
  }

  public void process(final List<String> list) {
    NickServInfo info = new NickServInfo();
    String[] split = list.get(0).split(" ");
    info.setNick(split[2]);
    info.setAccount(split[4].substring(0, split[4].indexOf(')')));
    list.subList(1, list.size()).stream().filter(line -> line.contains(":")).forEach(line -> {
      int i = line.indexOf(':');
      String key = line.substring(0, i).trim();
      String value = line.substring(i + 1).trim();
      switch (key) {
        case "Registered":
          info.setRegistered(extractDate(value));
          break;
        case "User Reg.":
          info.setUserRegistered(extractDate(value));
          break;
        case "Last seen":
          info.setLastSeen(extractDate(value));
          break;
        case "Last addr":
          info.setLastAddress(value);
          break;
        default:
          info.extra(key, value);
          break;
      }
    });
    NickServInfo nickServInfo = findByAccount(info.getAccount());
    if (nickServInfo != null) {
      nickServInfo.setNick(info.getNick());
      nickServInfo.setLastSeen(info.getLastSeen());
      nickServInfo.setLastAddress(info.getLastAddress());
      save(nickServInfo);
    } else {
      save(info);
    }
  }

  private LocalDateTime extractDate(final String line) {
    if (line.endsWith("now")) {
      return LocalDateTime.now();
    } else {
      String dateString = line.contains("(") ? line.substring(0, line.indexOf(" (")) : line;
      return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(NSERV_DATE_FORMAT));
    }
  }

  public NickServInfo findByAccount(final String account) {
    NickServInfoCriteria criteria = new NickServInfoCriteria(ds);
    criteria.account(account);
    return criteria.query().get();
  }

  public NickServInfo findByNick(final String nick) {
    NickServInfoCriteria criteria = new NickServInfoCriteria(ds);
    criteria.nick(nick);
    return criteria.query().get();
  }
}
