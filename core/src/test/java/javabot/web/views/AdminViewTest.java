package javabot.web.views;

import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import net.htmlparser.jericho.Source;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class AdminViewTest extends ViewsTest {
    protected HttpServletRequest getRequest() {
        return new MockServletRequest(true);
    }


    protected Source render() throws IOException {
        FreemarkerViewRenderer renderer = new FreemarkerViewRenderer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        renderer.render(new AdminIndexView(getInjector(), getRequest()), Locale.getDefault(), output);
        return new Source(new ByteArrayInputStream(output.toByteArray()));
    }

}
