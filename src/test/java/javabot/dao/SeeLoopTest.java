package javabot.dao;

import javabot.BotEvent;
import javabot.Message;
import javabot.operations.GetFactoidOperation;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

//

// Author: joed

// Date  : Apr 15, 2007
public class SeeLoopTest extends BaseServiceTest {

    private final static String CHANNEL = "#TEST";
    private final static String SENDER = "joed";
    private final static String LOGIN = "joed";
    private final static String HOSTNAME = "localhost";


    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
    private ChangesDao changesDao;

    private GetFactoidOperation getFactoidOperation;

    @BeforeMethod
    public void setUp() {

        getFactoidOperation = new GetFactoidOperation(factoidDao);
    }


    //Test of Fanooks patches.
    //Muchas gracias.
    @Test(groups = {"operations"})
    public void createCircularSee() {

        factoidDao.addFactoid("test", "see1", "<see>see2", changesDao);
        factoidDao.addFactoid("test", "see2", "<see>see3", changesDao);
        factoidDao.addFactoid("test", "see3", "<see>see3", changesDao);

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        List<Message> results = getFactoidOperation.handleMessage(event);

        Assert.assertEquals("Reference loop detected for factoid '<see>see3'.", results.get(0).getMessage());

        factoidDao.forgetFactoid("test", "see1", changesDao);
        factoidDao.forgetFactoid("test", "see2", changesDao);
        factoidDao.forgetFactoid("test", "see3", changesDao);


    }

    //Test of Fanooks patches.
    //Muchas gracias.
    @Test(groups = {"operations"})
    public void createCircularSee2() {

        factoidDao.addFactoid("test", "see1", "<see>see2", changesDao);
        factoidDao.addFactoid("test", "see2", "<see>see3", changesDao);
        factoidDao.addFactoid("test", "see3", "<see>see1", changesDao);

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        List<Message> results = getFactoidOperation.handleMessage(event);

        Assert.assertEquals("Reference loop detected for factoid '<see>see2'.", results.get(0).getMessage());

        factoidDao.forgetFactoid("test", "see1", changesDao);
        factoidDao.forgetFactoid("test", "see2", changesDao);
        factoidDao.forgetFactoid("test", "see3", changesDao);
    }


    /*
   seetest : "Bzzt $who"
seetest2 : <see> seetest
seetest3 : <see> seetest2
    */

    @Test(groups = {"operations"})
    public void followReferencesCorrectly() {

        factoidDao.addFactoid("test", "see1", "Bzzt $who", changesDao);
        factoidDao.addFactoid("test", "see2", "<see>see1", changesDao);
        factoidDao.addFactoid("test", "see3", "<see>see2", changesDao);

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see3");
        List<Message> results = getFactoidOperation.handleMessage(event);

        Assert.assertEquals("joed, see1 is Bzzt joed", results.get(0).getMessage());

        factoidDao.forgetFactoid("test", "see1", changesDao);
        factoidDao.forgetFactoid("test", "see2", changesDao);
        factoidDao.forgetFactoid("test", "see3", changesDao);
    }

    @Test(groups = {"operations"})
    public void createNormalSee() {

        factoidDao.addFactoid("test", "see1", "<see>see2", changesDao);
        factoidDao.addFactoid("test", "see2", "<see>see3", changesDao);
        factoidDao.addFactoid("test", "see3", "w00t", changesDao);

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        List<Message> results = getFactoidOperation.handleMessage(event);

        Assert.assertEquals("joed, see3 is w00t", results.get(0).getMessage());

        factoidDao.forgetFactoid("test", "see1", changesDao);
        factoidDao.forgetFactoid("test", "see2", changesDao);
        factoidDao.forgetFactoid("test", "see3", changesDao);

    }


}