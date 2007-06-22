package javabot.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javabot.dao.ChannelDao;
import javabot.model.Channel;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

/**
 * @author joed
 */
@Test(groups = {"operations"})
public class ChannelOperationsTest extends UnitilsTestNG {
    private ChannelDao c_dao;

    @org.testng.annotations.BeforeMethod()
    public void setUp() {
        c_dao = createMock(ChannelDao.class);
        Channel config = new Channel();
        config.setChannel("test");
        config.setId((long)1);
        config.setUpdated(new Date());
        c_dao.saveOrUpdate(config);

    }

    @Test
    public void testDaoCount() {
        reset(c_dao);
        Channel config = new Channel();
        config.setChannel("test");
        config.setId((long)1);
        config.setUpdated(new Date());
        expect(c_dao.getChannel("test")).andReturn(config);
        replay(c_dao);
        Assert.assertEquals(1, c_dao.getChannel("test").getId().intValue()
        );
    }

    public void testDaoCount2() {
        List<Channel> listOfChannels = new LinkedList<Channel>();
        Integer id = 100;
        reset(c_dao);
        Channel config = new Channel();
        config.setChannel("test");
        config.setId((long)1);
        config.setUpdated(new Date());
        listOfChannels.add(config);
        expect(c_dao.getChannels()).andReturn(listOfChannels);
        replay(c_dao);
        Assert.assertEquals(1, c_dao.getChannels().size()
        );
    }

    public void testDaoCount3() {
        List<Channel> listOfChannels = new ArrayList<Channel>();
        List<String> ret_this = new ArrayList<String>();
        ret_this.add("test");
        Integer id = 100;
        reset(c_dao);
        Channel config = new Channel();
        config.setChannel("test");
        config.setId((long)1);
        config.setUpdated(new Date());
        listOfChannels.add(config);
        expect(c_dao.configuredChannels()).andReturn(ret_this);
        replay(c_dao);
        Assert.assertEquals("test", c_dao.configuredChannels().get(0)
        );
    }

    public void testAlreadyHaveFactoid() {
        reset(c_dao);
        replay(c_dao);

    }

    /*
    public void testAddFactoid() {
        reset(f_dao);

        List<Factoid> listOfFactoids = new LinkedList<Factoid>();
        Integer id = 100;

        Factoid factoid = new Factoid();


        listOfFactoids.add(factoid);

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "magnificent is MAGNIFICENT");

        expect(f_dao.hasFactoid("magnificent")).andReturn(false);
        f_dao.addFactoid("joed", "magnificent", "MAGNIFICENT", c_dao, "test");

        replay(f_dao);

        List<Message> results = addOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "Okay, joed.");

    }

    public void testAddLongFactoid() {
        reset(f_dao);

        List<Factoid> listOfFactoids = new LinkedList<Factoid>();
        Integer id = 100;

        Factoid factoid = new Factoid();
        listOfFactoids.add(factoid);

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "magnificent is bla bla bla bla bla");

        expect(f_dao.hasFactoid("magnificent")).andReturn(false);
        f_dao.addFactoid("joed", "magnificent", "bla bla bla bla bla", c_dao, "test");

        replay(f_dao);

        List<Message> results = addOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "Okay, joed.");

    }
*/

}