package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.AdminDao;
import javabot.model.NickRegistration;
import org.springframework.beans.factory.annotation.Autowired;

@SPI(BotOperation.class)
public class RegisterNickOperation extends BotOperation {
  @Autowired
  private AdminDao adminDao;

  @Override
  public List<Message> handleMessage(IrcEvent event) {
    final String message = event.getMessage();
    final List<Message> responses = new ArrayList<Message>();
    if (message.startsWith("register ")) {
      String[] split = message.split(" ");
      if (split.length > 1) {
        String twitterName = split[1];
        NickRegistration registration = new NickRegistration(event.getSender(), twitterName);
        adminDao.save(registration);
        String eventMessage = Sofia.registerNick(
          String.format("http://evanchooly.com/register?id=%s", registration.getUrl()), twitterName);

        responses.add(new Message(event.getSender(), new IrcEvent(event.getSender().getNick(), event.getSender(), eventMessage),
          eventMessage));
      }
    }
    return responses;
  }
}
