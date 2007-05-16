package javabot;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;

public class Killbot {
    private static Log log = LogFactory.getLog(Killbot.class);

    public Killbot() throws IOException, JDOMException {
        kill();
    }

    private void kill() throws JDOMException, IOException {
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

    public static void main(final String[] args) throws Exception {
        new Killbot();
    }
}
