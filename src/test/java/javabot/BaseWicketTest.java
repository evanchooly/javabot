package javabot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletContext;

import javabot.admin.AdminApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.testng.annotations.BeforeMethod;

/**
 * Created Jul 29, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class BaseWicketTest extends AbstractTransactionalSpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(BaseWicketTest.class);

    private static final String[] LOCATIONS = {
        "classpath:applicationContext.xml"/*,
        "classpath:applicationContext-test.xml"*/
    };
    private WicketTester tester;

    public BaseWicketTest() {
        setPopulateProtectedVariables(true);
    }

    @Override
    protected String[] getConfigLocations() {
        return LOCATIONS;
    }

    @BeforeMethod
    public void init() {
        tester = new WicketTester(new TestAdminApplication()) {
            @Override
            public void dumpPage() {
                try {
                    FileOutputStream fos = null;
                    try {
                        File file = new File(getPreviousRenderedPage().getPageClass().getSimpleName() + ".html");
                        fos = new FileOutputStream(file);
                    } finally {
                        if(fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    }
                } catch(IOException e) {
                    log.error(e.getMessage(), e);
                    throw new ApplicationException(e.getMessage());
                }
            }
        };
    }

    @Override
    protected ConfigurableApplicationContext loadContext(Object locations) throws Exception {
//        ++this.loadCount;
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocations((String[])locations);
        context.refresh();
        return context;
    }

    private class TestAdminApplication extends AdminApplication {
        @Override
        public void init() {
            ServletContext servletContext = getServletContext();
            XmlWebApplicationContext context = (XmlWebApplicationContext)applicationContext;
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
            context.setServletContext(servletContext);
            super.init();
        }
    }

    public WicketTester getTester() {
        return tester;
    }
}
