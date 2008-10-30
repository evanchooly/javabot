package javadoc;

import java.io.File;

import javabot.BaseWicketTest;
import javabot.dao.ClazzDao;
import javabot.javadoc.StructureDoclet;
import org.testng.annotations.Test;

/**
 * Created Jul 23, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
@Test
public class StructureDocletTest extends BaseWicketTest {
    protected ClazzDao clazzDao;

    public void processSources() {
        File file = new File("src.zip");
        if(file.exists()) {
            new StructureDoclet().parse(file, "Java6", "http://java.sun.com/javase/6/docs/api/", "java javax");
            clazzDao.getEntityManager().flush();
            clazzDao.getEntityManager().close();
            setComplete();
            endTransaction();
        }
    }
}