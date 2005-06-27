package javabot;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Killbot {
    private static Log log = LogFactory.getLog(Killbot.class);

    private Killbot() {
    }

    public static void main(final String[] args) throws Exception {
        Javabot bot = new Javabot();
        String password = bot.getNickPassword();
        log.debug("password = " + password);
        Socket socket = new Socket("localhost", Javabot.PORT_NUMBER);
        PrintWriter writer =
            new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.println(password);
        writer.close();
        socket.close();
    }
}
