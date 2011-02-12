package javabot;

public class TellMessage extends Message {
    public TellMessage(final IrcUser tell, final String dest, final IrcEvent evt, final String value) {
        super(dest, evt, value.contains(tell.getNick()) ? value : String.format("%s, %s", tell, value));
    }
}
