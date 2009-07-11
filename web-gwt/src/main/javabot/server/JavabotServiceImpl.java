package javabot.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import javabot.client.JavabotService;

public class JavabotServiceImpl extends RemoteServiceServlet implements JavabotService {
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}