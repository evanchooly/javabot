package javabot.operations;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import org.pircbotx.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SPI(BotOperation.class)
public class DaysUntilOperation extends BotOperation {
    @Override
    public boolean handleMessage(final Message event) {
        String message = event.getValue().toLowerCase();
        boolean handled = false;
        if (message.startsWith("days until ")) {
            final User sender = event.getUser();
            message = message.substring("days until ".length());
            LocalDateTime d = null;
            final DateTimeFormatter[] formats = {
                                                    DateTimeFormatter.ofPattern("yyyy/MM/dd"), DateTimeFormatter.ofPattern("MMM d, ''yy"),
                                                    DateTimeFormatter.ofPattern("d MMM yyyy"), DateTimeFormatter.ofPattern("MMM d, yyyy"),
                                                    DateTimeFormatter.ofPattern("MMM d, ''yy")
            };
            int i = 0;
            while (i < formats.length && d == null) {
                try {
                    d = LocalDateTime.parse(message, formats[i]);
                    calcTime(event, d);
                } catch (IllegalArgumentException e) {
                    // I think we just want to ignore this...
                }
                i++;
            }
            if (d == null) {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.invalidDateFormat(sender), event.isTell());
                handled = true;
            }
        }
        return handled;
    }

    private void calcTime(final Message event, final LocalDateTime d) {
        final Long days = Duration.between(d, LocalDateTime.now().withHour(0)).toDays();
        long l = d.toLocalDate().toEpochDay();
        getBot().postMessage(event.getChannel(), event.getUser(), Sofia.daysUntil(event.getUser().getNick(), days, new Date(l)),
                             event.isTell());
    }
}
