package javabot;

public class Message {
    private final String destination;
    private final String message;
    private final boolean action;

    public Message(String dest, String value, boolean isAction) {
        destination = dest;
        message = value;
        action = isAction;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAction() {
        return action;
    }

    public String toString() {
        return "Message{" +
            "destination='" + destination + "'" +
            ", message='" + message + "'" +
            ", action=" + action +
            "}";
    }
}
