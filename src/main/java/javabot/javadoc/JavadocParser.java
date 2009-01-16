package javabot.javadoc;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javabot.dao.ClazzDao;
import org.jaxen.dom.DOMXPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(JavadocParser.class);
    @Autowired
    private ClazzDao dao;

    @Transactional
    public void parse(final Api api, final List<String> packages, final Writer writer) {
        try {
            final Document document = Clazz.getDocument(api.getBaseUrl() + "/allclasses-frame.html");
            for (final HTMLElement element : (List<HTMLElement>) new DOMXPath("//A[@target='classFrame']")
                .evaluate(document)) {
                try {
                    dao.save(new Clazz(api, element, packages));
                } catch (IrrelevantClassException e) {
                    log.debug(e.getMessage(), e);
                }
            }
            final List<Clazz> classes = new ArrayList<Clazz>(dao.findAll(api.getName()));
            while(!classes.isEmpty()) {
                classes.addAll(classes.remove(0).populate(dao, writer));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}