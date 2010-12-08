package javabot;

import java.util.List;

import org.schwering.irc.lib.IRCUser;

public class TellMessage extends Message {
    private final IRCUser target;

    public TellMessage(final IRCUser tell, final String dest, final IrcEvent evt, final String value) {
        super(dest, evt, value.contains(tell.getNick()) ? value : String.format("%s, %s", tell, value));
        target = tell;
    }

    @Override
    public void send(final Javabot bot) {
/*
        final IrcEvent event = getEvent();
        List<Message> messages = bot.getListener().getResponses(event.getChannel(), target, event.getMessage());
        final Message first = messages.remove(0);
        System.out.println("first = " + first);
        System.out.println("target = " + target);
        if (!first.getMessage().contains(target)) {
            messages.add(0, new Message(first.getDestination(), first.getEvent(),
                String.format("%s, %s", target, first.getMessage())));
        }
        for (Message message : messages) {
            message.send(bot);
        }
*/
    }
}
