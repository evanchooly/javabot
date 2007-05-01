package javabot.mock;


import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import javabot.dao.model.factoids;
import javabot.operations.GuessOperation2;
import static org.easymock.EasyMock.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class GuessOperationsTest extends UnitilsTestNG {

    private static String CHANNEL = "#TEST";
    private static String SENDER = "joed";
    private static String LOGIN = "joed";
    private static String HOSTNAME = "localhost";

    private FactoidDao f_dao;
    private ChangesDao c_dao;

    private GuessOperation2 guessOperation;

    public void setUp() {
        f_dao = createMock(FactoidDao.class);
        c_dao = createMock(ChangesDao.class);
        guessOperation = new GuessOperation2(f_dao);
    }

    public void testDaoCount() {

        reset(f_dao);

        Integer expectedCount = 1;
        //f_dao.addFactoid(SENDER,"heya","HEYA",c_dao,"test");
        expect(f_dao.getNumberOfFactoids()).andReturn(expectedCount.longValue());
        replay(f_dao);


        Assert.assertEquals(1, f_dao.getNumberOfFactoids().intValue());

    }

    public void testGuess() {
        reset(f_dao);

        List listOfFactoids = new LinkedList();
        Integer id = 100;

        factoids factoid = new factoids();
        factoid.setId(id.longValue());
        factoid.setName("magnificent");
        factoid.setValue("MAGNIFICENT");
        factoid.setUpdated(new Date());
        factoid.setUserName(SENDER);

        listOfFactoids.add(factoid);

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "guess MAGNIFI");
        expect(f_dao.hasFactoid("magnificent")).andReturn(true);
        expect(f_dao.getFactoids()).andReturn(listOfFactoids);
        expect(f_dao.getFactoid("magnificent")).andReturn(factoid);
        expect(f_dao.getFactoid("magnificent")).andReturn(factoid);

        replay(f_dao);

        List<Message> results = guessOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "I guess the factoid 'magnificent' might be appropriate:");
        Assert.assertEquals(results.get(1).getMessage(), "joed, magnificent is MAGNIFICENT");
    }

}