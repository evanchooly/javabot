package javabot.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.Set;

import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class UnixCommandOperation implements BotOperation {
    private Set<String> _commands = new TreeSet<String>();
    private List<String> _insults = new ArrayList<String>();
    private Random _random;

    public UnixCommandOperation() {
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

    private void addFiles(String path) {
        File bin = new File(path);
        if(bin.exists()) {
            File[] files = bin.listFiles();
            for(int index = 0; index < files.length; index++) {
                File file = files[index];
                if(file.isFile()) {
                    _commands.add(file.getName());
                }
            }
        }
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        return new ArrayList< Message>();
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        List<Message> messages = new ArrayList< Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String[] split = message.split(" ");
        if(_commands.contains(split[0]) && (split.length < 3)) {
            messages.add(new Message(channel, event.getSender()
                + ": wrong window, " + getInsult(), false));
        }
        return messages;
    }

    private String getInsult() {
        return _insults.get(_random.nextInt(_insults.size()));
    }
}
