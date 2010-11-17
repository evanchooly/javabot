package javabot.operations;

import java.util.Arrays;
import java.util.List;

import javabot.Message;
import org.testng.Assert;
import org.unitils.spring.annotation.SpringApplicationContext;

@SpringApplicationContext("classpath:applicationContext.xml")
public abstract class BaseCamelTest /*extends BaseTest*/ {
    protected void scanForResponse(final String message, final String target) {
        final List<Message> list = sendMessage(message);
        boolean found = false;
        for (final Message response : list) {
            found |= response.getMessage().contains(target);
        }

        Assert.assertTrue(found, String.format("Should find the response in the responses."
            + "\n** expected: \n%s"
            + "\n** got: \n%s", target, list));
    }

    protected void testMessage(final String message, final String... responses) {
        final List<Message> list = sendMessage(message);

        Assert.assertEquals(list.size(), responses.length, String.format("Should get expected response count back. "
            + "\n** expected: \n%s"
            + "\n** got: \n%s", Arrays.toString(responses), list));
        for (final String response : responses) {
            Assert.assertEquals(list.remove(0).getMessage(), response);
        }
        Assert.assertTrue(list.isEmpty(), "All responses should be matched.");
    }

    @SuppressWarnings("unchecked")
    protected List<Message> sendMessage(final String message) {
//        final Object future = template.requestBody("direct:in", message);
//        final List<Message> list = (List<Message>) template.requestBody("direct:in", message);
//        try {
//            list = (List<Message>) future.get(10, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//        final Future<Object> future = template
//            .asyncRequestBody("direct:out", new BotEvent(null, null, null, null, null));
//        return list;
        return null;
    }

    protected void testMessageList(final String message, final List<String> responses) {
        final List<Message> list = sendMessage(message);
        boolean found = false;
        for (final Message response : list) {
            found |= responses.contains(response.getMessage());
        }

        Assert.assertTrue(found, String.format("Should get one response from the list of possibilities"
            + "\n** expected: \n%s"
            + "\n** got: \n%s", responses, list));
    }

    public String getForgetMessage(final String factoid) {
        return String.format("I forgot about %s, %s.", factoid, null /*TEST_USER*/);
    }

    protected String getFoundMessage(final String factoid, final String value) {
        return String.format("%s, %s is %s", null /*TEST_USER*/, factoid, value);
    }

    protected void forgetFactoid(final String name) {
        testMessage(String.format("~forget %s", name), getForgetMessage(name));
    }
}