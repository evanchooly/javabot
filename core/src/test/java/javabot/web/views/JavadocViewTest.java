package javabot.web.views;

import io.dropwizard.views.View;
import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import net.htmlparser.jericho.Source;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class JavadocViewTest extends ViewsTest{

    @Test
    public void render() throws IOException {
        JavadocView view = new JavadocView(getInjector(), new MockServletRequest(false));
        Source source = render(view);
    }

}