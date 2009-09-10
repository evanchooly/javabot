package javabot.javadoc;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javabot.JavabotThreadFactory;
import javabot.dao.ClazzDao;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLElement;
import org.xml.sax.SAXException;

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
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 30000, TimeUnit.SECONDS, workQueue,
        new JavabotThreadFactory(false, "javadoc-thread-"));

    @Transactional
    public void parse(final Api api, final Writer writer) {
        try {
            executor.prestartAllCoreThreads();
            final Document document = Clazz.getDocument(String.format("%s/allclasses-frame.html", api.getBaseUrl()));
            for (final HTMLElement element : (List<HTMLElement>) new DOMXPath("//A[@target='classFrame']")
                .evaluate(document)) {
                try {
                    final List<String> packages = api.getPackages() == null ? Collections.<String>emptyList()
                        : Arrays.asList(api.getPackages());
                    final Clazz clazz = new Clazz(api, element, packages);
                    log.debug(String.format("Found class: %s", clazz));
                    dao.save(clazz);
                } catch (IrrelevantClassException e) {
                    log.debug(e.getMessage());
                }
            }
            final List<Clazz> classes = new ArrayList<Clazz>(dao.findAll(api.getName()));
            while (!classes.isEmpty()) {
                workQueue.add(process(classes.remove(0)));
            }
            while (!workQueue.isEmpty()) {
                writer.write(String.format("Waiting on %s work queue to drain.  %d items left", api.getName(),
                    workQueue.size()));
                Thread.sleep(5000);
            }
            executor.shutdown();
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } catch (JaxenException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } catch (SAXException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private Runnable process(final Clazz clazz) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    for (final Clazz clazz1 : clazz.populate(dao)) {
                        workQueue.add(process(clazz1));
                    }
//                } catch (IrrelevantClassException e) {
//                    log.debug(e.getMessage());
                } catch (RuntimeException e) {
                    log.debug(e.getMessage(), e);
//                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            @Override
            public String toString() {
                return clazz.toString();
            }
        };
    }
}