package javabot.web.views;

import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import net.htmlparser.jericho.Source;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

public class FactoidsViewTest extends ViewsTest {
    @Inject
    private FactoidDao factoidDao;

    @Test
    public void singleFactoid() throws IOException {
        createFactoids(1);
        Source source = render(0, new Factoid());
        previousDisabled(source);
        nextDisabled(source);
        checkRange(source, 1, 1, 1);
    }

    @Test
    public void factoidFilter() throws IOException {
        createFactoids(10);
        Source source = render(0, new Factoid("name 1", null, null));

        previousDisabled(source);
        nextDisabled(source);
        checkRange(source, 1, 1, 1);

        source = render(0, new Factoid(null, "value 1", null));
        previousDisabled(source);
        nextDisabled(source);
        checkRange(source, 1, 1, 1);

        source = render(0, new Factoid(null, null, "userName 1"));
        previousDisabled(source);
        nextDisabled(source);
        checkRange(source, 1, 1, 1);
    }

    @Test
    public void factoidBadFilter() throws IOException {
        createFactoids(10);
        Source source = render(0, new Factoid("bad filter", null, null));

        previousDisabled(source);
        nextDisabled(source);
        checkRange(source, 0, 0, 0);
    }

    @Test
    public void twoFactoidPages() throws IOException {
        int count = (int) (PagedView.ITEMS_PER_PAGE * 1.5);
        createFactoids(count);

        Source source = render(0, new Factoid());
        previousDisabled(source);
        nextEnabled(source);
        checkRange(source, 1, PagedView.ITEMS_PER_PAGE, count);

        source = render(2, new Factoid());
        previousEnabled(source);
        nextDisabled(source);
        checkRange(source, PagedView.ITEMS_PER_PAGE + 1, count, count);

        source = render(3, new Factoid());
        previousEnabled(source);
        nextDisabled(source);
        checkRange(source, PagedView.ITEMS_PER_PAGE + 1, count, count);
    }

    protected Source render(final int page, final Factoid filter) throws IOException {
        FreemarkerViewRenderer renderer = new FreemarkerViewRenderer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        renderer.render(new FactoidsView(getInjector(), new MockServletRequest(false), page, filter), Locale.getDefault(), output);
        return new Source(new ByteArrayInputStream(output.toByteArray()));
    }

    private void createFactoids(final int count) {
        factoidDao.deleteAll();
        for (int i = 0; i < count; i++) {
            Factoid factoid = new Factoid("name " + i, "value " + i, "userName " + i);
            factoid.setUpdated(LocalDateTime.now());
            factoidDao.save(factoid);
        }
    }
}