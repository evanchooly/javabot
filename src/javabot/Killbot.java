package javabot;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public final class Killbot
{
	public static void main(final String[] args) throws Exception
	{
		Javabot bot=new Javabot();
		String password=bot.getNickPassword();

		Socket socket=new Socket("localhost",2346);
		
		OutputStreamWriter writer=
			new OutputStreamWriter(socket.getOutputStream());
		
		writer.write(password);
		writer.write("\n");
		writer.close();
		socket.close();
	}
}
