package javabot;

public class BotEvent
{
	private Javabot bot;
	private String channel;
	private String sender;
	private String login;
	private String hostname;
	private String message;

	public BotEvent
	(
		Javabot bot,
		String channel,
		String sender,
		String login,
        	String hostname,
		String message
	)
	{
        	this.bot=bot;
		this.channel=channel;
		this.sender=sender;
		this.login=login;
		this.hostname=hostname;
		this.message=message;
	}
	
	public Javabot getBot()
	{
		return bot;
	}

	public String getChannel()
	{
        	return channel;
	}

	public String getSender()
	{
		return sender;
	}

	public String getLogin()
	{
		return login;
	}

	public String getHostname()
	{
		return hostname;
	}

	public String getMessage()
	{
		return message;
	}
}
