package javabot;

import org.schwering.irc.lib.IRCUser;

public class TellMessage extends Message {
    public TellMessage(final IRCUser tell, final String dest, final IrcEvent evt, final String value) {
        super(dest, evt, value.contains(tell.getNick()) ? value : String.format("%s, %s", tell, value));
    }
}
