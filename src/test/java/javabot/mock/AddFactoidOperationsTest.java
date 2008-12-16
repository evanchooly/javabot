package javabot.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import javabot.operations.AddFactoidOperation;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

@Test(groups = {"operations"})
public class AddFactoidOperationsTest extends UnitilsTestNG {
    private static final String CHANNEL = "#TEST";
    private static final String SENDER = "joed";
    private static final String LOGIN = "joed";
    private static final String HOSTNAME = "localhost";
    private FactoidDao factoidDao;
    private AddFactoidOperation addOperation;

    @BeforeMethod
    public void setUp() {
        factoidDao = createMock(FactoidDao.class);
        addOperation = new AddFactoidOperation(new Javabot(), factoidDao, createMock(ChangeDao.class));
    }

    public void testDaoCount() {
        reset(factoidDao);
        Integer expectedCount = 1;
        expect(factoidDao.count()).andReturn(expectedCount.longValue());
        replay(factoidDao);
        Assert.assertEquals(1, factoidDao.count().intValue());
    }

    public void testAlreadyHaveFactoid() {
        reset(factoidDao);
        List<Factoid> listOfFactoids = new LinkedList<Factoid>();
        Integer id = 100;
        Factoid factoid = new Factoid();
        factoid.setId(id.longValue());
        factoid.setName("magnificent");
        factoid.setValue("MAGNIFICENT");
        factoid.setUpdated(new Date());
        factoid.setUserName(SENDER);
        listOfFactoids.add(factoid);
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "magnificent is MAGNIFICENT");
        expect(factoidDao.hasFactoid("magnificent")).andReturn(true);
        expect(factoidDao.getFactoid("magnificent")).andReturn(factoid);
        expect(factoidDao.getFactoid("magnificent")).andReturn(factoid);
        replay(factoidDao);
        List<Message> results = addOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "I already have a factoid with that name, joed");
    }

    public void testAddFactoid() {
        reset(factoidDao);
        List<Factoid> listOfFactoids = new LinkedList<Factoid>();
        Factoid factoid = new Factoid();
        listOfFactoids.add(factoid);
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "magnificent is MAGNIFICENT");
        expect(factoidDao.hasFactoid("magnificent")).andReturn(false);
        factoidDao.addFactoid("joed", "magnificent", "MAGNIFICENT");
        replay(factoidDao);
        List<Message> results = addOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "Okay, joed.");
    }

    public void testAddLongFactoid() {
        reset(factoidDao);
        List<Factoid> listOfFactoids = new LinkedList<Factoid>();
        Factoid factoid = new Factoid();
        listOfFactoids.add(factoid);
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "magnificent is bla bla bla bla bla");
        expect(factoidDao.hasFactoid("magnificent")).andReturn(false);
        factoidDao.addFactoid("joed", "magnificent", "bla bla bla bla bla");
        replay(factoidDao);
        List<Message> results = addOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "Okay, joed.");
    }
}