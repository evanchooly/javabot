package javabot.servlet;

import java.io.IOException;
import java.io.File;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javabot.PortListener;
import javabot.Javabot;
import org.jdom.JDOMException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created Feb 5, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class JavabotServlet implements Servlet {
    private static final Log log = LogFactory.getLog(JavabotServlet.class);
    private Javabot bot;

    public void init(final ServletConfig servletConfig) throws ServletException {
        log.info("Starting Javabot");
        try {
            bot = new Javabot() {
                @Override
                protected File getConfigFile() {
                    return new File(new File(System.getProperty("user.home")), ".javabot/config.xml").getAbsoluteFile();
                }
            };
        } catch(JDOMException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } catch(IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        new PortListener(Javabot.PORT_NUMBER, bot.getNickPassword()).start();
        bot.setMessageDelay(2000);
        bot.connect();
        log.info("Javabot loaded");
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    
    public void service(ServletRequest servletRequest, ServletResponse servletResponse)
        throws ServletException, IOException {
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {
        if(bot == null) {
            bot.shutdown();
        }
    }
}
