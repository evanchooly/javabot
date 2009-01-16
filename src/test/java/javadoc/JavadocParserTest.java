package javadoc;

import java.io.StringWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javabot.javadoc.Clazz;
import javabot.javadoc.JavadocParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;
import org.jaxen.JaxenException;
import org.xml.sax.SAXException;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class JavadocParserTest extends BaseTest {
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
    private static final String NAME = "JDK";
    //        final String urlString = new File("/Users/jlee/Desktop/javadoc/docs/api").toURI().toURL().toString();
    private static final String URL_STRING = "http://java.sun.com/javase/6/docs/api";

    @Transactional
    public void parse() throws MalformedURLException {
        final Api api = fetchApi(NAME, URL_STRING);
        final JavadocParser parser = new JavadocParser();
        inject(parser);
        parser.parse(api, Arrays.asList("java", "javax"), writer);
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

    @Transactional
    public void generics() throws JaxenException, IOException, SAXException {
        final Api api = fetchApi("GenericJDK", URL_STRING);
        final Clazz clazz = new Clazz();
        clazz.setApi(api);
        clazz.setClassName("AbstractMap");
        clazz.setPackageName("java.util");
        clazz.setLongUrl("http://java.sun.com/javase/6/docs/api/java/util/AbstractMap.html");
        clazzDao.save(clazz);
        final List<Clazz> list = clazz.populate(clazzDao, writer);
        for (final Clazz clazz1 : list) {
            clazz1.populate(clazzDao, writer);
        }
    }
}
