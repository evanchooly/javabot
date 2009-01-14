package javabot.javadoc;

import java.io.Writer;
import java.util.List;

import javabot.dao.ClazzDao;
import org.jaxen.dom.DOMXPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLElement;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 * @noinspection unchecked
 */
public class JavadocParser {
    @Autowired
    private ClazzDao dao;

    @Transactional
    public void parse(final Api api, final Writer writer) {
        try {
            try {
                final Document document = Clazz.getDocument(api.getBaseUrl() + "/allclasses-frame.html");
                final List<HTMLElement> result = (List<HTMLElement>) new DOMXPath("//A[@target='classFrame']").evaluate(document);
                for (final HTMLElement element : result) {
                    dao.save(createClazz(api, element));
                }
                for (final Clazz clazz : dao.findAll(api.getName())) {
                    System.out.println("clazz = " + clazz);
                    clazz.populate(dao);
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } finally {
        }
    }

    private Clazz createClazz(final Api api, final HTMLElement element) {
        final Clazz clazz = new Clazz();
        String value = element.getFirstChild().getNodeValue();
        if(value == null) {
            value = element.getFirstChild().getFirstChild().getNodeValue();
        }
        clazz.setClassName(value);
        String title = element.getAttribute("title");
        title = title.substring(title.indexOf("in ") + 3);
        clazz.setPackageName(title);
        clazz.setLongUrl(api.getBaseUrl() + element.getAttribute("href"));
        clazz.setApi(api);
        api.getClasses().add(clazz);
        return clazz;
    }
}