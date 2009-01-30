package javabot;

import java.text.MessageFormat;

public class Message {
    private final String destination;
    private final String message;
    private BotEvent event;

    public Message(String dest, BotEvent evt, String value) {
        destination = dest;
        message = value;
        event = evt;
    }

    public BotEvent getEvent() {
        return event;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    public String formatResponse(Javabot bot, String nick) {
        return getMessage().startsWith(nick) ? getMessage() : MessageFormat.format("{0}, {1}", nick, getMessage());
    }

    public String logEntry() {
        return "said: " + getMessage();
    }

    public void send(Javabot bot) {
        bot.postMessage(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
            "destination='" + destination + "'" +
            ", message='" + message + "'" +
            ", event='" + event + "'" +
            "}";
    }
}
