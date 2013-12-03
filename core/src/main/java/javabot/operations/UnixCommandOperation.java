package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
@SPI(BotOperation.class)
public class UnixCommandOperation extends BotOperation {
    private final Set<String> commands = new TreeSet<String>();
    private final List<String> insults = new ArrayList<String>();
    private final Random random;

    public UnixCommandOperation() {
//        addFiles("/usr/bin");
//        addFiles("/bin");
//        addFiles("/usr/sbin");
//        addFiles("/sbin");
        commands.add("rm");
        commands.add("ls");
        commands.add("clear");
        
        insults.add("dumbass");
        insults.add("genius");
        insults.add("Einstein");
        insults.add("pal");
        
        random = new Random();
    }

    @Override
    public List<Message> handleChannelMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String[] split = message.split(" ");
        final List<Message> responses = new ArrayList<Message>();
        if (split.length != 0 && split.length < 3 && commands.contains(split[0])) {
            responses.add(new Message(channel, event,

                String.format("%s: wrong window, %s", event.getSender(), getInsult())));
        }
        return responses;
    }

    private String getInsult() {
        return insults.get(random.nextInt(insults.size()));
    }
}