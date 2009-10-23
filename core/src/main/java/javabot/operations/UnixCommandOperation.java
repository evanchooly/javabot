package javabot.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class UnixCommandOperation extends BotOperation {
    private final Set<String> commands = new TreeSet<String>();
    private final List<String> insults = new ArrayList<String>();
    private final Random random;

    public UnixCommandOperation(final Javabot bot) {
        super(bot);
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

    private void addFiles(final String path) {
        final File bin = new File(path);
        if (bin.exists()) {
            final File[] files = bin.listFiles();
            for (final File file : files) {
                if (file.isFile()) {
                    commands.add(file.getName());
                }
            }
        }
    }

    @Override
    public List<Message> handleChannelMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String[] split = message.split(" ");
        List<Message> responses = new ArrayList<Message>();
        if (commands.contains(split[0]) && split.length < 3) {
            responses.add(new Message(channel, event,
                String.format("%s: wrong window, %s", event.getSender(), getInsult())));
        }
        return responses;
    }

    private String getInsult() {
        return insults.get(random.nextInt(insults.size()));
    }
}