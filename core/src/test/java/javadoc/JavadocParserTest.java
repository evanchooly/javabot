package javadoc;

import java.io.StringWriter;
import java.net.MalformedURLException;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javabot.javadoc.JavadocParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static final String ZIP_LOCATION
        = "file:/System/Library/Frameworks/JavaVM.framework/Classes/classes.jar,file:/System/Library/Frameworks/JavaVM.framework/Classes/jce.jar,file:/System/Library/Frameworks/JavaVM.framework/Classes/jsse.jar";
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