package javabot.web.views;

import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import net.htmlparser.jericho.Source;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

public class KarmaViewTest extends ViewsTest {
    @Inject
    private KarmaDao karmaDao;

    @Test
    public void karma() throws IOException {
        createKarma(100);

        FreemarkerViewRenderer renderer = new FreemarkerViewRenderer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        renderer.render(new KarmaView(getInjector(), new MockServletRequest(false), 0), Locale.getDefault(), output);
        Source source = new Source(new ByteArrayInputStream(output.toByteArray()));

        previousDisabled(source);
        nextEnabled(source);

        checkRange(source, 1, 50, 100);
    }

    private void createKarma(final int count) {
        karmaDao.deleteAll();
        for (int i = 0; i < count; i++) {
            Karma karma = new Karma("name " + i, i, "userName " + i);
            karma.setUpdated(LocalDateTime.now());
            karmaDao.save(karma);
        }
    }

}
