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
import org.testng.annotations.BeforeMethod;
import org.unitils.UnitilsTestNG;

/**
 * @author joed
 */
@Test(groups = {"operations"})
public class ChannelOperationsTest extends UnitilsTestNG {
    private ChannelDao channelDao;

    @BeforeMethod()
    public void setUp() {
        channelDao = createMock(ChannelDao.class);
        Channel config = new Channel();
        config.setName("test");
        config.setId(1L);
        config.setUpdated(new Date());
        channelDao.save(config);

    }

    @Test
    public void testDaoCount() {
        reset(channelDao);
        Channel config = new Channel();
        config.setName("test");
        config.setId(1L);
        config.setUpdated(new Date());
        expect(channelDao.get("test")).andReturn(config);
        replay(channelDao);
        Assert.assertEquals(1, channelDao.get("test").getId().intValue());
    }

    public void testDaoCount2() {
        List<Channel> listOfChannels = new LinkedList<Channel>();
        reset(channelDao);
        Channel config = new Channel();
        config.setName("test");
        config.setId(1L);
        config.setUpdated(new Date());
        listOfChannels.add(config);
        expect(channelDao.getChannels()).andReturn(listOfChannels);
        replay(channelDao);
        Assert.assertEquals(1, channelDao.getChannels().size()
        );
    }

    public void testDaoCount3() {
        List<Channel> listOfChannels = new ArrayList<Channel>();
        List<String> ret_this = new ArrayList<String>();
        ret_this.add("test");
        reset(channelDao);
        Channel config = new Channel();
        config.setName("test");
        config.setId(1L);
        config.setUpdated(new Date());
        listOfChannels.add(config);
        expect(channelDao.configuredChannels()).andReturn(ret_this);
        replay(channelDao);
        Assert.assertEquals("test", channelDao.configuredChannels().get(0)
        );
    }

    public void testAlreadyHaveFactoid() {
        reset(channelDao);
        replay(channelDao);

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
        f_dao.addFactoid("joed", "magnificent", "MAGNIFICENT", channelDao, "test");

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
        f_dao.addFactoid("joed", "magnificent", "bla bla bla bla bla", channelDao, "test");

        replay(f_dao);

        List<Message> results = addOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "Okay, joed.");

    }
*/

}