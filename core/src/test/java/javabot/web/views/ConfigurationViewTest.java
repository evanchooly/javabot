package javabot.web.views;

import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import javabot.Javabot;
import javabot.dao.ConfigDao;
import javabot.model.Config;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeSet;

import static java.util.Arrays.asList;
import static org.testng.Assert.*;

public class ConfigurationViewTest extends ViewsTest {
    @Inject
    private ConfigDao configDao;
    @Inject
    private Provider<Javabot> javabot;

    @Test
    public void configuration() throws IOException {
        Config config = configDao.get();
        config.setOperations(new ArrayList<>());
        configDao.save(config);

        FreemarkerViewRenderer renderer = new FreemarkerViewRenderer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        renderer.render(new ConfigurationView(getInjector(), new MockServletRequest(false)), Locale.getDefault(), output);
        Source source = new Source(new ByteArrayInputStream(output.toByteArray()));

        String operation = "Javadoc";
        Element enable = source.getElementById("enable" + operation);
        assertNotNull(enable, source.toString());
        assertEquals(enable.getAttributeValue("class"), "active");

        Element disable = source.getElementById("disable" + operation);
        assertNotNull(disable, source.toString());
        assertEquals(disable.getAttributeValue("class"), "inactive");

        config = configDao.get();
        config.setOperations(asList(operation));
        configDao.save(config);

        output = new ByteArrayOutputStream();
        renderer.render(new ConfigurationView(getInjector(), new MockServletRequest(false)), Locale.getDefault(), output);
        source = new Source(new ByteArrayInputStream(output.toByteArray()));

        enable = source.getElementById("enable" + operation);
        assertNotNull(enable, source.toString());
        assertEquals(enable.getAttributeValue("class"), "inactive");

        disable = source.getElementById("disable" + operation);
        assertNotNull(disable, source.toString());
        assertEquals(disable.getAttributeValue("class"), "active");

    }

}
