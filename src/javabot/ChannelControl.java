package javabot;

public interface ChannelControl
{
	void partChannel(String channel,String leaveMessage);
	void joinChannel(String channel);
}
