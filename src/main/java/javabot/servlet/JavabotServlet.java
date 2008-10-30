package javabot.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import javabot.Javabot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created Feb 5, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class JavabotServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(JavabotServlet.class);
    private Javabot bot;

    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        if(log.isInfoEnabled()) {
            log.info("Starting Javabot");
        }
        try {
            bot = new Javabot();
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            System.out.println(e);
            throw new ServletException(e.getMessage());
        }
        bot.setMessageDelay(2000);
        bot.connect();
        if(log.isInfoEnabled()) {
            log.info("Javabot loaded");
        }
    }

    @Override
    public void destroy() {
        if(bot == null) {
            bot.shutdown();
        }
    }
}
