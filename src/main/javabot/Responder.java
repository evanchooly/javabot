package javabot;

import java.util.List;

public interface Responder
{
	public List getResponses(String channel,String sender,String login,String hostname,String message);
}
