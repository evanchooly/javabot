package javabot.dao;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.GetFactoidOperation;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

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
    private GetFactoidOperation getFactoidOperation;

    @BeforeMethod
    public void setUp() {

        getFactoidOperation = new GetFactoidOperation(new Javabot());
    }


    //Test of Fanooks patches.
    //Muchas gracias.
    @Test(groups = {"operations"})
    public void createCircularSee() {

        factoidDao.addFactoid("test", "see1", "<see>see2");
        factoidDao.addFactoid("test", "see2", "<see>see3");
        factoidDao.addFactoid("test", "see3", "<see>see3");

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        List<Message> results = getFactoidOperation.handleMessage(event);

        Assert.assertEquals("Reference loop detected for factoid '<see>see3'.", results.get(0).getMessage());

        factoidDao.delete("test", "see1");
        factoidDao.delete("test", "see2");
        factoidDao.delete("test", "see3");


    }

    //Test of Fanooks patches.
    //Muchas gracias.
    @Test(groups = {"operations"})
    public void createCircularSee2() {

        factoidDao.addFactoid("test", "see1", "<see>see2");
        factoidDao.addFactoid("test", "see2", "<see>see3");
        factoidDao.addFactoid("test", "see3", "<see>see1");

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        List<Message> results = getFactoidOperation.handleMessage(event);

        Assert.assertEquals("Reference loop detected for factoid '<see>see2'.", results.get(0).getMessage());

        factoidDao.delete("test", "see1");
        factoidDao.delete("test", "see2");
        factoidDao.delete("test", "see3");
    }


    /*
   seetest : "Bzzt $who"
seetest2 : <see> seetest
seetest3 : <see> seetest2
    */

    @Test(groups = {"operations"})
    public void followReferencesCorrectly() {

        factoidDao.addFactoid("test", "see1", "Bzzt $who");
        factoidDao.addFactoid("test", "see2", "<see>see1");
        factoidDao.addFactoid("test", "see3", "<see>see2");

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see3");
        List<Message> results = getFactoidOperation.handleMessage(event);

        Assert.assertEquals("joed, see1 is Bzzt joed", results.get(0).getMessage());

        factoidDao.delete("test", "see1");
        factoidDao.delete("test", "see2");
        factoidDao.delete("test", "see3");
    }

    @Test(groups = {"operations"})
    public void createNormalSee() {

        factoidDao.addFactoid("test", "see1", "<see>see2");
        factoidDao.addFactoid("test", "see2", "<see>see3");
        factoidDao.addFactoid("test", "see3", "w00t");

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        List<Message> results = getFactoidOperation.handleMessage(event);

        Assert.assertEquals("joed, see3 is w00t", results.get(0).getMessage());

        factoidDao.delete("test", "see1");
        factoidDao.delete("test", "see2");
        factoidDao.delete("test", "see3");

    }


}