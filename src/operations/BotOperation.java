package operations;

import java.util.List;

public interface BotOperation
{
	/**
		Returns a list of BotOperation.Message, empty if the operation
		was not applicable to the message passed.
		
		It should never return null.
	*/
	public List handleMessage(BotEvent event);
}
