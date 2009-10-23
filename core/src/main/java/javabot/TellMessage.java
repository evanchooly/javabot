package javabot;

public class TellMessage extends Message {
    private String target;

    public TellMessage(String tell, String dest, BotEvent evt, String value) {
        super(dest, evt, value.contains(tell) ? value : String.format("%s, %s", tell, value));
        target = tell;
    }

/*
    @Override
    public void send(final Javabot bot) {
        final BotEvent event = getEvent();
        List<Message> messages = bot
            .getResponses(event.getChannel(), target, event.getLogin(), event.getHostname(), event.getMessage());
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
    }
*/
}
