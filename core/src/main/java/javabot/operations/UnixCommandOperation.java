package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import org.pircbotx.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class UnixCommandOperation extends BotOperation {
    private final Set<String> commands = new TreeSet<>();
    private final List<String> insults = new ArrayList<>();
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
    public boolean handleChannelMessage(final Message event) {
        final String message = event.getValue();
        final Channel channel = event.getChannel();
        final String[] split = message.split(" ");
        if (split.length != 0 && split.length < 3 && commands.contains(split[0])) {
            getBot().postMessage(channel, event.getUser(), Sofia.botUnixCommand(event.getUser().getNick(), getInsult()),
                                 event.isTell());
            return true;
        }
        return false;
    }

    private String getInsult() {
        return insults.get(random.nextInt(insults.size()));
    }
}