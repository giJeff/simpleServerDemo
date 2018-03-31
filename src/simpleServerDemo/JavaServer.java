package simpleMultiServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class JavaServer
{
	private static final int PORT_NUMBER = 12654;

	public static void main(String[] args) throws Exception
	{
		System.out.println("Server started.");
		ServerSocket listener = new ServerSocket(PORT_NUMBER);
		try
		{
			while (true)
			{
				new Handler(listener.accept()).start();
			}
		}
		finally
		{
			listener.close();
		}
	}

	private static class Handler extends Thread
	{
		private String user_Entered_Name;
		private Socket connection_Socket;
		private BufferedReader in;
		private PrintWriter out;
		private boolean is_Name_Entered = false;

		public Handler(Socket socket)
		{
			this.connection_Socket = socket;
		}

		public void run()
		{
			try
			{
				in = new BufferedReader(new InputStreamReader(connection_Socket.getInputStream()));
				out = new PrintWriter(connection_Socket.getOutputStream(), true);

				String line = in.readLine();
				// server gets request from client to initiate a connection 
				if (line.contains("CLIENTREQUEST"))
				{

					while (!is_Name_Entered)
					{
						// server acks the client request and sends out a second handshake to ensure this is a good connection
						out.println("NEEDNAME");
						user_Entered_Name = in.readLine();
						if (user_Entered_Name == null)
						{
							is_Name_Entered = false;
						}
						else
						{
							is_Name_Entered = true;
						}
					}
				}
			}
			catch (IOException e)
			{
				System.out.println("The connection was terminated, goodbye.");
			}
			finally
			{
				try
				{
					connection_Socket.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
}