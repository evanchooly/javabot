package javabot.web.views;

import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import javabot.dao.ChangeDao;
import javabot.model.Change;
import net.htmlparser.jericho.Source;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

public class ChangesViewTest extends ViewsTest {
    @Inject
    private ChangeDao changeDao;

    @Test
    public void changes() throws IOException {
        createChanges(30);

        FreemarkerViewRenderer renderer = new FreemarkerViewRenderer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        renderer.render(new ChangesView(getInjector(), new MockServletRequest(false), 0, new Change()), Locale.getDefault(), output);
        Source source = new Source(new ByteArrayInputStream(output.toByteArray()));

        previousDisabled(source);
        nextDisabled(source);
        checkRange(source, 1, 30, 30);

        output = new ByteArrayOutputStream();
        renderer.render(new ChangesView(getInjector(), new MockServletRequest(false), 0, new Change("change 2")), Locale.getDefault(), output);
        source = new Source(new ByteArrayInputStream(output.toByteArray()));

        previousDisabled(source);
        nextDisabled(source);
        checkRange(source, 1, 11, 11);
    }

    private void createChanges(final int count) {
        changeDao.deleteAll();
        for (int i = 0; i < count; i++) {
            Change change = new Change("change " + i);
            change.setChangeDate(LocalDateTime.now());
            changeDao.save(change);
        }

    }
}
