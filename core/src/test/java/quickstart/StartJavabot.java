package quickstart;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class StartJavabot {
    public static void main(final String[] args) throws Exception {
        final Server server = new Server();
        final SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        final WebAppContext web = new WebAppContext();
        web.setContextPath("/javabot");
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