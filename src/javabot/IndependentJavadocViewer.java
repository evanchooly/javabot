package javabot;

import javabot.operations.JavadocOperation;

public class IndependentJavadocViewer
{
	public static void main(String[] args)
	{
		JavadocOperation operation=new JavadocOperation
			("docref.xml","http://java.sun.com/j2se/1.5.0/docs/api/");

		Message message=(Message)operation.handleMessage
		(
			new BotEvent
			(
				"cmdline",
				"cmdline",
				null,
				null,
				"javadoc "+args[0]
			)
		).get(0);

		System.out.println(message.getMessage().split(": ")[1]);
		
	}
}
