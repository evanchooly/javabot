package javabot.operations;

import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Message;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

/**
 * @author StanAccy
 */
public class LogsOperationTest extends BaseOperationTest
{
	@Test
	public void testChannelLogs() throws Exception
	{
		// Add a known and unique message to the logs so we can validate that we are testing against new data
		String uuid = UUID.randomUUID().toString();
		sendMessage( uuid );

		List<Message> list = sendMessage( "~logs" );

		Assert.assertEquals( list.isEmpty(), false );

		int listSize = list.size();
		Assert.assertEquals( list.get( listSize - 2 ).getMessage().contains( uuid ), true );
		Assert.assertEquals( list.get( listSize - 1 ).getMessage().contains( "~logs" ), true );
	}

	@Test
	public void testNickSpecificLogsWhenNoLogsForNick() throws Exception
	{
		// We generate unique user names so that existing data in the DB doesnt interfere with this unit test
		String uuid = UUID.randomUUID().toString();
		List<Message> list = sendMessage( "~logs " + uuid );

		int listSize = list.size();
		Assert.assertEquals( listSize, 1 );
		String msg = list.get( listSize - 1 ).getMessage();
		Assert.assertEquals( msg.contains( "No logs found for nick: " + uuid ), true );
	}

	@Test
	public void testNickSpecificLogsWhenLogs() throws Exception
	{
		String uuid = UUID.randomUUID().toString();
		IrcUser user = new IrcUser( uuid, "notreal@test.com", "127.0.0.1" );
		getJavabot().processMessage( new IrcEvent( getJavabotChannel(), user, "Hello I'm " + uuid ) );

		List<Message> list = sendMessage( "~logs " + uuid );

		int listSize = list.size();
		Assert.assertEquals( listSize, 1 );
		Assert.assertEquals( list.get( listSize - 1 ).getMessage().contains( uuid ), true );
	}
}
