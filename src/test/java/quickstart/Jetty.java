package quickstart;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.nio.SelectChannelConnector;

public class Jetty {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        WebAppContext web = new WebAppContext();
        web.setContextPath("/");
        web.setWar("build/war");
        web.setDistributable(true);

        server.addHandler(web);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}