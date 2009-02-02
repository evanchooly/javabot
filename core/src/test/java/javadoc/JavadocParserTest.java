package javadoc;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javabot.javadoc.JavadocParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test(groups = {"javadoc"})
public class JavadocParserTest extends BaseTest {
    public static final String API_NAME = "JDK";
    public static final String API_URL_STRING = "http://java.sun.com/javase/6/docs/api";
//    public static final String API_NAME = "JSF";
//    public static final String API_URL_STRING = "http://java.sun.com/javaee/javaserverfaces/1.2_MR1/docs/api";
    @Autowired
    private ApiDao dao;
    @Autowired
    private ClazzDao clazzDao;
    private final StringWriter writer = new StringWriter() {
        @Override
        public void write(final String str) {
            System.out.println(str);
        }
    };

    @Transactional
    public void parse() throws MalformedURLException {
        if (dao.find(JavadocParserTest.API_NAME) == null) {
            final Api api = fetchApi(API_NAME, API_URL_STRING);
            final JavadocParser parser = new JavadocParser();
            inject(parser);
            parser.parse(api, Arrays.asList("java", "javax"), writer);
        }
    }

    public void servlets() {
        final Api api = fetchApi("Servlet", "http://java.sun.com/products/servlet/2.3/javadoc/");
        final JavadocParser parser = new JavadocParser();
        inject(parser);
        parser.parse(api, Collections.<String>emptyList(), writer);
    }
    private Api fetchApi(final String name, final String urlString) {
        Api api = dao.find(name);
        if (api != null) {
            dao.delete(api);
        }
        api = new Api(name, urlString);
        dao.save(api);
        return api;
    }

}
