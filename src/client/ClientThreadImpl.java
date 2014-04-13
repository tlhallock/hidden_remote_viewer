package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import common.ControlException;
import common.ServiceLocator;
import common.intfce.ClientDisplay;
import common.intfce.ClientThread;
import common.message.CloseMessage;
import common.message.ControlMessage;

final class ClientThreadImpl extends Thread implements ClientThread
{
	private final ServerSocket _serverSocket;
	private ObjectOutputStream _connection;

	ClientThreadImpl() throws IOException
	{
		_serverSocket = new ServerSocket(2828);
	}

	@Override
	public void run()
	{
		for (;;)
		{
			System.out.println("Waiting for server...");
			try (final Socket socket = _serverSocket.accept();)
			{
				System.out.println("Opened connection to " + socket.getInetAddress());
				ClientDisplay clientDisplay = ServiceLocator.getClientDisplay();
				if (clientDisplay == null)
				{
					System.err.println("No client display to show connection!");
					continue;
				}

				synchronized (clientDisplay)
				{
					clientDisplay.setCurrentServer(socket.getInetAddress().getHostAddress());
					handleConnection(socket);
					clientDisplay.leaveCurrentServer();
				}
			} catch (IOException e)
			{
				System.err.println("Unable to accept connection!");
				e.printStackTrace();
				continue;
			}
		}
	}

	public void die()
	{

	}

	public void sendRequest(ControlMessage msg) throws IOException
	{
		ObjectOutputStream out = _connection;

		if (out == null)
		{
			return;
		}

		try
		{
			out.writeObject(msg);
		} catch (IOException ex)
		{
			System.err.println("Client failed to write message");
			ex.printStackTrace();

			try
			{
				out.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void handleConnection(Socket socket)
	{
		try (final ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()); final ObjectInputStream input = new ObjectInputStream(socket.getInputStream());)
		{
			_connection = output;

			for (;;)
			{
				final Object readObject;
				try
				{
					readObject = input.readObject();
				} catch (ClassNotFoundException | IOException e)
				{
					System.err.println("Unable to read message. Terminating connection");
					e.printStackTrace();
					break;
				}

				if (!(readObject instanceof ControlMessage))
				{
					System.err.println("Received invalid object from server:");
					System.err.println(readObject.getClass().getName());
					System.err.println(readObject);
				}

				ControlMessage message = (ControlMessage) readObject;
				System.out.println("Received message: " + message);

				try
				{
					message.performAction();
				} catch (ControlException e)
				{
					System.err.println("Unable to run message:");
					e.printStackTrace();
				}

				if (message instanceof CloseMessage)
				{
					break;
				}
			}
			_connection = null;
		} catch (IOException e)
		{
			_connection = null;
			System.err.println("Error closing object streams");
			e.printStackTrace();
		}
	}
}
