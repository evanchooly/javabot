package javadoc;

import java.io.StringWriter;
import java.net.MalformedURLException;

import javabot.BaseTest;
import javabot.dao.ApiDao;
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
@Test
public class JavadocParserTest extends BaseTest {
    @Autowired
    private ApiDao dao;

    @Transactional
    public void parse() throws MalformedURLException {
        final String name = "JDK";
//        final String urlString = new File("/Users/jlee/Desktop/javadoc/docs/api").toURI().toURL().toString();
        final String urlString = "http://java.sun.com/javase/6/docs/api";
        Api api = dao.find(name);
        if (api != null) {
            dao.delete(api);
        }
        api = new Api(name, urlString);
        dao.save(api);
        final JavadocParser parser = new JavadocParser();
        inject(parser);
        parser.parse(api, new StringWriter() {
            @Override
            public void write(final String str) {
                System.out.println(str);
            }
        });
    }
}
