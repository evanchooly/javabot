package javabot.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class UnixCommandOperation implements BotOperation {
    private TreeSet _commands = new TreeSet();
    private List _insults = new ArrayList();
    private Random _random;

    public UnixCommandOperation() {
        addFiles("/usr/bin");
        addFiles("/bin");
        addFiles("/usr/sbin");
        addFiles("/sbin");

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
     * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
     */
    public List handleMessage(BotEvent event) {
        return new TypeSafeList(new ArrayList(), Message.class);
    }

    public List handleChannelMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String message = event.getMessage();
        String channel = event.getChannel();
        String[] split = message.split(" ");
        if(_commands.contains(split[0])) {
            messages.add(new Message(channel, event.getSender()
                + ": wrong window, " + getInsult(), false));
        }
        return messages;
    }

    private String getInsult() {
        return (String)_insults.get(_random.nextInt(_insults.size()));
    }
}
