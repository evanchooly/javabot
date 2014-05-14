package javabot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionTimeoutException;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.MyPircBot;
import javabot.dao.NickServDao;
import javabot.model.NickServInfo;

@SPI({AdminCommand.class})
public class NickServLookup extends AdminCommand {
  @Inject
  private NickServDao nickServDao;

  @Param
  String name;

  @Override
  @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
  public List<Message> execute(final Javabot bot, final IrcEvent event) {
    final List<Message> responses = new ArrayList<>();
    try {
      NickServInfo info = validateNickServAccount(bot.getPircBot());
      if(info == null) {
        responses.add(new Message(event.getSender(), event, Sofia.noNickservEntry(name)));
      } else {
        responses.addAll(info.toNickServFormat().stream().map(line -> new Message(event.getSender(), event, line))
            .collect(Collectors.toList()));
      }
    } catch (ConditionTimeoutException e) {
      responses.add(new Message(event.getSender(), event, Sofia.noNickservEntry(name)));
    }

    return responses;
  }

  private NickServInfo validateNickServAccount(final MyPircBot myPircBot) {
    AtomicReference<NickServInfo> info = new AtomicReference<>(nickServDao.find(name));
    if(info.get() == null) {
      myPircBot.sendMessage("NickServ", "info " + name);
        Awaitility.await()
            .atMost(10, TimeUnit.SECONDS)
            .until(() -> {
              info.set(nickServDao.find(name));
              return info.get() != null;
            });
    }
    return info.get();
  }

}