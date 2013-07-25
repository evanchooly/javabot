package javabot.operations;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.query.Query;
import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Message;
import javabot.dao.LogsDao;
import javabot.model.Logs;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gets logs for channel, optionally filtered by a nickname
 *
 * @author StanAccy
 */
@SPI( BotOperation.class )
public class LogsOperation extends BotOperation
{
	private static final String KEYWORD_LOGS = "logs";
	@Inject
	private LogsDao logsDao;

	@Override
	public List<Message> handleMessage( final IrcEvent event )
	{
		final String message = event.getMessage();
		final List<Message> responses = new ArrayList<>();

		if( message.toLowerCase().startsWith( KEYWORD_LOGS ) )
		{
			final String nickname = message.substring( KEYWORD_LOGS.length() ).trim();

			Query<Logs> query = logsDao.getQuery();

			query.field( "channel" ).equal( event.getChannel() );
			query.order( "-updated" );

			IrcUser sender = event.getSender();
			if( nickname.isEmpty() )
			{
				query.limit( 200 );
			}
			else
			{
				query.field( "nick" ).equal( nickname );
				query.limit( 50 );
			}

			Iterable<Logs> logsIter = query.fetch();
			for( Logs logs : logsIter )
			{
				StringBuilder buf = new StringBuilder();
				DateTime updated = logs.getUpdated();
				String nick = logs.getNick();
				String msg = logs.getMessage();

				buf.append( updated.toString( "HH:mm" ) );
				buf.append( " <" );
				buf.append( nick );
				buf.append( "> " );
				buf.append( msg );

				responses.add( new Message( sender, event, buf.toString() ) );
			}

			if( responses.isEmpty() )
			{
				if( nickname.isEmpty() )
				{
					responses.add( new Message( sender, event, "No logs found." ) );
				}
				else
				{
					responses.add( new Message( sender, event, "No logs found for nick: " + nickname ) );
				}
			}

			Collections.reverse( responses );
		}
		return responses;
	}
}
