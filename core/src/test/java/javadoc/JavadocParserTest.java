package javadoc;

import java.io.StringWriter;
import java.net.MalformedURLException;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javabot.javadoc.Clazz;
import javabot.javadoc.JavadocParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test(groups = {"javadoc"})
public class JavadocParserTest extends BaseTest {
    public static final String API_NAME = "Servlet";
    public static final String API_URL_STRING = "http://java.sun.com/products/servlet/2.3/javadoc/";
    public static final String PACKAGES = "";
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

    public void jdk() throws MalformedURLException {
        parse(JavadocParserTest.API_NAME, API_URL_STRING);
    }

    @Test(dependsOnMethods = "jdk")
    public void jee() {
        parse("JEE", "http://java.sun.com/javaee/5/docs/api/");
    }

    public void servlet() {
        final Api api = fetchApi("Servlet", "http://java.sun.com/products/servlet/2.3/javadoc/");
        final JavadocParser parser = new JavadocParser();
        inject(parser);
        parser.parse(api, writer);
    }

    @Test(dependsOnMethods = "servlet")
    public void reprocess() {
        servlet();
    }

    @Test(dependsOnMethods = "jdk")
    public void wicket() {
        parse("Wicket", "http://wicket.apache.org/docs/wicket-1.3.2/wicket/apidocs/");
    }

    @Test(dependsOnMethods = "jdk")
    public void targetted() {
        final Api api = dao.find("JDK");
        Clazz clazz  = new Clazz(api, "java.text", "DateFormat.Field");
        clazz.setLongUrl("http://java.sun.com/javase/6/docs/api/java/text/DateFormat.Field.html");
        clazzDao.save(clazz);
        clazz.populate(clazzDao);
    }

    private void parse(final String apiName, final String url) {
        if (dao.find(apiName) == null) {
            final Api api = fetchApi(apiName, url);
            final JavadocParser parser = new JavadocParser();
            inject(parser);
            parser.parse(api, writer);
        }
    }

    private Api fetchApi(final String name, final String urlString) {
        Api api = dao.find(name);
        if (api != null) {
            dao.delete(api);
        }
        api = new Api(name, urlString, null);
        dao.save(api);
        return api;
    }
}