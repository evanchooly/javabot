package javabot.operations;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

import com.rickyclarkson.java.util.Arrays;
import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author ricky_clarkson
 */
public class DictOperation implements BotOperation {
    private final static int port = 2628;

    /**
     * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);

        String message = event.getMessage();
        String sender = event.getSender();
        String channel = event.getChannel();
        Javabot bot = event.getBot();

        String[] messageParts = message.split(" ");

        if (!messageParts[0].equals("dict"))
            return messages;

        Object wordParts = Arrays.subset(messageParts, 1, messageParts.length);

        message = Arrays.toString(wordParts, " ");

        try {
            Socket socket = new Socket(
                InetAddress.getByName(bot.getDictHost()), port);

            OutputStream outputStream = socket.getOutputStream();

            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            InputStream inputStream = socket.getInputStream();

            InputStreamReader reader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(reader);

            writer.write("DEFINE * \"" + message + "\"\n");

            writer.write("QUIT\n");

            writer.flush();

            String input;
            do {
                input = bufferedReader.readLine();

                if (input != null && !input.trim().equals("")
                    && (input.charAt(0) < '0' || input.charAt(0) > '9')
                    && !input.equals("."))
                    messages.add(new Message(sender, input, false));

                if (input != null && input.startsWith("151")) {
                    String message2 = input.replaceFirst("[^\"]*\"[^\"]*\" ",
                        "");

                    messages.add(new Message(sender, message2, false));
                }
            } while (input != null);

            reader.close();
            inputStream.close();

            writer.close();
            outputStream.close();
        } catch (Exception exception) {
            messages.add(new Message(sender, exception.toString(), false));
        }

        if (messages.size() == 0) {
            messages.add(new Message(channel, "I cannot find a definition for "
                + message, false));
        } else {
            messages.add(0, new Message(channel,
                "I'll tell you what that means in a " + "private message, "
                    + sender, false));
        }
        return messages;
    }

    public List handleChannelMessage(BotEvent event)
    {
	    	return new TypeSafeList(new ArrayList(),Message.class);
    }
}
