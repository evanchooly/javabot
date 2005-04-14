package javabot;

import java.util.Map;

public interface Database
{
	boolean hasFactoid(String key);
	void addFactoid(String sender,String key,String value);
	void forgetFactoid(String sender,String key);
	String getFactoid(String key);
	Map getMap();
	int getNumberOfFactoids();
}
