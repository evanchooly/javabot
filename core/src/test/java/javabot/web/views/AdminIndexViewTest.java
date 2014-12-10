package javabot.web.views;

import com.antwerkz.sofia.Sofia;
import javabot.dao.AdminDao;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class AdminIndexViewTest extends AdminViewTest {
    @Inject
    private AdminDao adminDao;

    @Test
    public void index() throws IOException {
        Source source = render();

        long count = adminDao.count();
        for (int i = 0; i < count; i++) {
            Assert.assertNotNull(source.getElementById("admin" + i), "Trying to find admin" + i + "\n\n" + source);
        }
        List<Element> labels = source.getAllElements("for", "irc", false);
        Assert.assertFalse(labels.isEmpty());
        Element label = labels.get(0);
        Assert.assertEquals(label.getContent().toString(), Sofia.ircName());
    }
}
