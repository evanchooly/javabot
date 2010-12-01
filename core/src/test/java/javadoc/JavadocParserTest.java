package javadoc;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javabot.javadoc.JavadocParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.io.StringWriter;
import java.net.MalformedURLException;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test(groups = {"javadoc"})
public class JavadocParserTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(JavadocParserTest.class);

    public static final String API_NAME = "JDK";
    public static final String API_URL_STRING = "http://java.sun.com/javase/6/docs/api/";
    public static final String JDK_PATH = "/Library/Java/JavaVirtualMachines/1.6.0_22-b04-307.jdk/Contents/Classes";
    public static final String ZIP_LOCATION =
            String.format("file:%s/classes.jar,file:%s/jce.jar,file:%s/jsse.jar", JDK_PATH, JDK_PATH, JDK_PATH);
    public static final String PACKAGES = "java,javax";
    @Autowired
    private ApiDao dao;
    @Autowired
    private ClazzDao clazzDao;
    private final StringWriter writer = new StringWriter() {
        @Override
        public void write(final String str) {
            log.info("str = " + str);
        }
    };

    public void jdk() throws MalformedURLException {
        if (dao.find(API_NAME) == null) {
            final Api api = new Api(API_NAME, API_URL_STRING, PACKAGES, ZIP_LOCATION);
            dao.save(api);
            final JavadocParser parser = new JavadocParser();
            inject(parser);
            parser.parse(api, writer);
        }
    }
}