package javabot;

public class Message
{
	private final String destination;
	private final String message;
	private final boolean action;

	public Message
		(String destination,String message,boolean action)
	{
		this.destination=destination;
		this.message=message;
		this.action=action;
	}

	public String getDestination()
	{
		return destination;
	}

	public String getMessage()
	{
		return message;
	}

	public boolean isAction()
	{
		return action;
	}
}
