package javabot.web.views;

import com.google.inject.Injector;
import io.dropwizard.views.View;
import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import javabot.BaseTest;
import net.htmlparser.jericho.Source;
import org.testng.Assert;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class ViewsTest extends BaseTest {

    @Inject
    private Injector injector;

    protected void checkRange(final Source source, final int from, final int to, final int of) {
        String content = source.getElementById("currentPage").getContent().toString().trim();
        Assert.assertEquals(content, String.format("Displaying %d to %d of %d", from, to, of));
    }

    protected void nextEnabled(final Source source) {
        Assert.assertFalse(source.getElementById("nextPage").getStartTag().getAttributeValue("class").contains("disabled"),
                           "Next page should not be disabled");
    }

    protected void nextDisabled(final Source source) {
        Assert.assertTrue(source.getElementById("nextPage").getStartTag().getAttributeValue("class").contains("disabled"),
                          "Next page should be disabled");
    }

    protected void previousDisabled(final Source source) {
        Assert.assertTrue(source.getElementById("previousPage").getStartTag().getAttributeValue("class").contains("disabled"),
                          "Previous page should be disabled");
    }

    protected void previousEnabled(final Source source) {
        Assert.assertFalse(source.getElementById("previousPage").getStartTag().getAttributeValue("class").contains("disabled"),
                           "Previous page should not be disabled");
    }

    public Injector getInjector() {
        return injector;
    }

    protected Source render(final View view) throws IOException {
        FreemarkerViewRenderer renderer = new FreemarkerViewRenderer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        renderer.render(view, Locale.getDefault(), output);
        return new Source(new ByteArrayInputStream(output.toByteArray()));
    }
}