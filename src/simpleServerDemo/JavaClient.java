package simpleMultiServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JavaClient
{
	BufferedReader in;
	PrintWriter out;
	JFrame frame = new JFrame("JavaClient");
	JTextArea body = new JTextArea(10, 50);

	public JavaClient()
	{
		body.setEditable(false);
		frame.getContentPane().add(new JScrollPane(body), "Center");
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	private String getName()
	{
		return JOptionPane.showInputDialog(frame, "Hi, what is your name?", "Name Enter Window",
				JOptionPane.QUESTION_MESSAGE);
	}

	private void run() throws IOException
	{
		@SuppressWarnings("resource")
		Socket socket = new Socket("127.0.0.1", 12654);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		
		boolean running = true;
		
		// request made from client to start connection
		out.println("CLIENTREQUEST");
		body.append("************************************************************* \n");
		body.append("* The client is requesting a connection from the server. * \n");
		body.append("************************************************************* \n");

		while (running)
		{
			String line = in.readLine();
			// the client is able to speak with the sever 2 step handshake complete  time to execute 
			if (line.contains("NEEDNAME"))
			{
				body.append("**************************************************************************************************** \n");
				body.append("* The server has ackowleged the clients request and is requesting a name from the client. * \n");
				body.append("**************************************************************************************************** \n");
				String name = getName();
				if (name != null)
				{
				body.append("User " + name + " has connected to: " + socket.toString() + "\n" +
				name + ", Have a nice day!");
				}
				else 
				{
					body.append("No name was entered please close the window.");
					running = false;
				}
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		JavaClient client = new JavaClient();
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		client.run();
	}
}