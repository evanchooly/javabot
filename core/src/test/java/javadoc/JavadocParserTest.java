package javadoc;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
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
    public static final String API_NAME = "JDK";
    public static final String API_URL_STRING = "http://java.sun.com/javase/6/docs/api";
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
        parse(JavadocParserTest.API_NAME, API_URL_STRING, Arrays.asList("java", "javax"));
    }

    @Test(dependsOnMethods = "jdk")
    public void jee() {
        parse("JEE", "http://java.sun.com/javaee/5/docs/api/", Collections.<String>emptyList());
    }

    @Test(dependsOnMethods = "jdk")
    public void wicket() {
        parse("Wicket", "http://wicket.apache.org/docs/wicket-1.3.2/wicket/apidocs/", Collections.<String>emptyList());
    }

//    public void cipherDoFinal() {
//        final Clazz[] cipher = clazzDao.getClass("javax.crypto", "Cipher");
//        cipher[0].populate(clazzDao);
//        parse("Wicket", "http://wicket.apache.org/docs/wicket-1.3.2/wicket/apidocs/", Collections.<String>emptyList());
//    }

    private void parse(final String apiName, final String url, final List<String> packages) {
        if (dao.find(apiName) == null) {
            final Api api = fetchApi(apiName, url);
            final JavadocParser parser = new JavadocParser();
            inject(parser);
            parser.parse(api, packages, writer);
        }
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