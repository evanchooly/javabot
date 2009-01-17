package javabot.dao;

import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import javabot.operations.GetFactoidOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;
//
// Author: joed

// Date  : Apr 15, 2007
public class SeeLoopTest extends BaseOperationTest {
    private final static String CHANNEL = "##javabot";
    private final static String SENDER = "joed";
    private final static String LOGIN = "joed";
    private final static String HOSTNAME = "localhost";
    @Autowired
    private FactoidDao factoidDao;
    private final GetFactoidOperation operation;

    public SeeLoopTest() {
        operation = new GetFactoidOperation(getJavabot());
        inject(operation);
    }

    public BotOperation createOperation() {
        return new GetFactoidOperation(getJavabot());
    }

    //Test of Fanooks patches.
    //Muchas gracias.
    @Test(groups = {"operations"})
    public void createCircularSee() {
        factoidDao.addFactoid("test", "see1", "<see>see2");
        factoidDao.addFactoid("test", "see2", "<see>see3");
        factoidDao.addFactoid("test", "see3", "<see>see3");
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        final List<Message> results = operation.handleMessage(event);
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
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        final List<Message> results = operation.handleMessage(event);
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
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see3");
        final List<Message> results = operation.handleMessage(event);
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
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "see1");
        final List<Message> results = operation.handleMessage(event);
        Assert.assertEquals("joed, see3 is w00t", results.get(0).getMessage());
        factoidDao.delete("test", "see1");
        factoidDao.delete("test", "see2");
        factoidDao.delete("test", "see3");

    }


}