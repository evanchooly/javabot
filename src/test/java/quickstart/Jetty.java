package quickstart;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.nio.SelectChannelConnector;

/**
 * StartJavabot
 * <p/>
 * Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */

public class Jetty {

    /**
     * Main function, stkarts the jetty server.
     *
     * @param args
     * @throws Exception
     */
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

        // JMX
        // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        // server.getContainer().addEventListener(mBeanContainer);
        // mBeanContainer.start();

        //Javabot bot;
        try {
            /*   bot = new Javabot() {
                  @Override
                  protected File getConfigFile() {
                      return new File(new File(System.getProperty("user.home")), ".javabot/config.xml").getAbsoluteFile();
                  }
              };

              bot.setMessageDelay(2000);
              bot.connect();
            */
            server.start();
            server.join();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }


    }
}
