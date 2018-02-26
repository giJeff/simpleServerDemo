package simpleServerDemo;

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

		while (true)
		{
			String line = in.readLine();
			if (line.contains("NEEDNAME"))
			{
				String name = getName();
				body.setText("User " + name + " has connected to: " + socket.toString() + "\n" +
				name + ", Have a nice day!");
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
