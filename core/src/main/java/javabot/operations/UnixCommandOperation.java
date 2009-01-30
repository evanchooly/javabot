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
    private final Set<String> _commands = new TreeSet<String>();
    private final List<String> _insults = new ArrayList<String>();
    private final Random _random;

    public UnixCommandOperation(final Javabot bot) {
        super(bot);
//        addFiles("/usr/bin");
//        addFiles("/bin");
//        addFiles("/usr/sbin");
//        addFiles("/sbin");
        _commands.add("rm");
        _commands.add("ls");
        _commands.add("clear");
        
        _insults.add("dumbass");
        _insults.add("genius");
        _insults.add("Einstein");
        _insults.add("pal");
        
        _random = new Random();
    }

    private void addFiles(final String path) {
        final File bin = new File(path);
        if (bin.exists()) {
            final File[] files = bin.listFiles();
            for (final File file : files) {
                if (file.isFile()) {
                    _commands.add(file.getName());
                }
            }
        }
    }

    @Override
    public boolean handleChannelMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String[] split = message.split(" ");
        boolean handled = true;
        if (_commands.contains(split[0]) && split.length < 3) {
            getBot().postMessage(new Message(channel, event,
                String.format("%s: wrong window, %s", event.getSender(), getInsult())));
            handled = true;
        }
        return handled;
    }

    private String getInsult() {
        return _insults.get(_random.nextInt(_insults.size()));
    }
}