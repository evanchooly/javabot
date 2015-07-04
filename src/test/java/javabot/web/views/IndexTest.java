package javabot.web.views;

import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;

public class IndexTest extends ViewsTest {
    @Test
    public void index() throws IOException {
        find(false);
        find(true);
    }

    protected void find(final boolean loggedIn) throws IOException {
        FreemarkerViewRenderer renderer = new FreemarkerViewRenderer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        renderer.render(new IndexView(getInjector(), new MockServletRequest(loggedIn)), Locale.getDefault(), output);
        Source source = new Source(new ByteArrayInputStream(output.toByteArray()));
        Element a = source.getElementById("id");
        Assert.assertTrue(a == null || loggedIn, format("Should %sfind the newChannel link", loggedIn ? "" : "not "));
    }
}