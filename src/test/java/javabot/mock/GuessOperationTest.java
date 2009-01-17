package javabot.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import javabot.operations.GuessOperation;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author joed
 */
@Test(groups = {"operations"}, enabled = false)
public class GuessOperationTest extends BaseOperationTest {
    private final static String CHANNEL = "##javabot";
    private final static String SENDER = "joed";
    private final static String LOGIN = "joed";
    private final static String HOSTNAME = "localhost";
    private FactoidDao f_dao;

    @BeforeMethod
    public BotOperation createOperation() {
        return new GuessOperation(getJavabot());
    }

    public void testDaoCount() {
        EasyMock.reset(f_dao);
        final Integer expectedCount = 1;
        //f_dao.addFactoid(SENDER,"heya","HEYA",c_dao,"test");
        EasyMock.expect(f_dao.count()).andReturn(expectedCount.longValue());
        EasyMock.replay(f_dao);
        Assert.assertEquals(1, f_dao.count().intValue());
    }

    public void testGuess() {
        EasyMock.reset(f_dao);
        final List<Factoid> listOfFactoids = new LinkedList<Factoid>();
        final Integer id = 100;
        final Factoid factoid = new Factoid();
        factoid.setId(id.longValue());
        factoid.setName("magnificent");
        factoid.setValue("MAGNIFICENT");
        factoid.setUpdated(new Date());
        factoid.setUserName(SENDER);
        listOfFactoids.add(factoid);
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "guess MAGNIFI");
        EasyMock.expect(f_dao.hasFactoid("magnificent")).andReturn(true);
        EasyMock.expect(f_dao.getFactoids()).andReturn(listOfFactoids);
        EasyMock.expect(f_dao.getFactoid("magnificent")).andReturn(factoid);
        EasyMock.expect(f_dao.getFactoid("magnificent")).andReturn(factoid);
        EasyMock.replay(f_dao);
        final List<Message> results = getOperation().handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "I guess the factoid 'magnificent' might be appropriate:");
        Assert.assertEquals(results.get(1).getMessage(), "joed, magnificent is MAGNIFICENT");
    }

    public void testIgnores() {
        //"you", "and", "are", "to", "that", "your", "do", "have", "a", "the", "be", "but", "can", "i", "who", "how", "get", "by", "is", "of", "out", "me", "an", "for", "use", "he", "she", "it"
        EasyMock.reset(f_dao);
        final List<Factoid> listOfFactoids = new LinkedList<Factoid>();
        final Integer id = 100;
        final Factoid factoid = new Factoid();
        factoid.setId(id.longValue());
        factoid.setName("magnificent");
        factoid.setValue("MAGNIFICENT");
        factoid.setUpdated(new Date());
        factoid.setUserName(SENDER);
        listOfFactoids.add(factoid);
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME,
            "guess you and are to that do have magnif a the be but can i who how get by is of out me an for use he she it");
        EasyMock.expect(f_dao.hasFactoid("magnificent")).andReturn(true);
        EasyMock.expect(f_dao.getFactoids()).andReturn(listOfFactoids);
        EasyMock.expect(f_dao.getFactoid("magnificent")).andReturn(factoid);
        EasyMock.expect(f_dao.getFactoid("magnificent")).andReturn(factoid);
        EasyMock.replay(f_dao);
        final List<Message> results = getOperation().handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "I guess the factoid 'magnificent' might be appropriate:");
        Assert.assertEquals(results.get(1).getMessage(), "joed, magnificent is MAGNIFICENT");
    }
}