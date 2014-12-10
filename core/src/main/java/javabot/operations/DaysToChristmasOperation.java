package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static java.time.LocalDateTime.now;

public class DaysToChristmasOperation extends BotOperation {
    @Override
    public boolean handleMessage(final Message event) {
        if ("countdown to christmas".equals(event.getValue().toLowerCase())) {
            LocalDateTime christmas = LocalDateTime.of(now().getYear(), Month.DECEMBER, 25, 0, 0, 0);
            LocalDateTime now = now();
            Duration duration = Duration.between(now, christmas);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, 12);
            cal.set(Calendar.DAY_OF_MONTH, 25);
            Instant instant = christmas.atZone(ZoneId.systemDefault()).toInstant();

            getBot().postMessage(event.getChannel(), event.getUser(),
                                 Sofia.daysUntil(event.getUser().getNick(), duration.toDays(), Date.from(instant)),
                                 event.isTell());
            return true;
        }
        return false;
    }
}
