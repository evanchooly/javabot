package javabot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PortListener implements Runnable
{
	Thread thread=new Thread(this);
	private int port;
	private String password;

	public PortListener(int port,String password)
	{
		this.port=port;
		this.password=password;
	}
	
	public void start()
	{
		thread.start();
	}

	public void run()
	{
		try
		{
			ServerSocket serverSocket=new ServerSocket(port);

			while (true)
			{
				Socket socket=serverSocket.accept();

				InputStream inputStream=socket.getInputStream();
				
				InputStreamReader streamReader=
					new InputStreamReader(inputStream);
				
				BufferedReader reader=
					new BufferedReader(streamReader);

				String line=reader.readLine();
				System.out.println(password+"=="+line+"?");
				
				if (password.equals(line))
					System.exit(0);

				socket.close();
			}
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
	}
}
