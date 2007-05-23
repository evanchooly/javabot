package javabot;

import java.util.List;

public interface Responder
{
	List<Message> getResponses
	(
		String channel,
		String sender,
		String login,
		String hostname,
		String message
	);
}
