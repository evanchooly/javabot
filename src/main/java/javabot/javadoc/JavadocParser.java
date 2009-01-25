package javabot.javadoc;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 30000, TimeUnit.SECONDS, workQueue);

    @Transactional
    public void parse(final Api api, final List<String> packages, final Writer writer) {
        try {
            final Document document = Clazz.getDocument(api.getBaseUrl() + "/allclasses-frame.html");
            for (final HTMLElement element : (List<HTMLElement>) new DOMXPath("//A[@target='classFrame']")
                .evaluate(document)) {
                try {
                    final Clazz clazz = new Clazz(api, element, packages);
                    writer.write("Found class: " + clazz);
                    dao.save(clazz);
                } catch (IrrelevantClassException e) {
                    log.debug(e.getMessage(), e);
                }
            }
            final List<Clazz> classes = new ArrayList<Clazz>(dao.findAll(api.getName()));
            while(!classes.isEmpty()) {
                executor.submit(process(writer, classes.remove(0)));
            }
            while (!workQueue.isEmpty()) {
                writer.write("Waiting on " + api.getName() + " work queue to drain.  " + workQueue.size() + " items left");
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Runnable process(final Writer writer, final Clazz clazz) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    for (final Clazz clazz1 : clazz.populate(dao, writer)) {
                        executor.submit(process(writer, clazz1));
                    }
                } catch (IrrelevantClassException e) {
                    log.debug(e.getMessage(), e);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        };
    }
}