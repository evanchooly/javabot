package javabot;

public class Message {
    private final String destination;
    private String message;
    private IrcEvent event;

    public Message(final String dest, final IrcEvent evt, final String value) {
        destination = dest;
        message = value;
        event = evt;
    }

    public Message(final IrcUser sender, final IrcEvent event, final String value) {
        this(sender.getNick(), event, value);
    }

    public IrcEvent getEvent() {
        return event;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    protected void setMessage(final String message) {
        this.message = message;
    }

    public void send(final Javabot bot) {
        bot.postMessage(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
            "type='" + getClass().getSimpleName() + "'" +
            ", destination='" + destination + "'" +
            ", message='" + message + "'" +
            ", event='" + event + "'" +
            "}";
    }
}
