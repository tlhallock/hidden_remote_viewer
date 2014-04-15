package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import common.ServiceLocator;

public class ServerHandler extends Thread
{
	private final ServerSocket _serverSocket;

	public ServerHandler(ServerSocket serverSocket)
	{
		_serverSocket = serverSocket;
	}

	public void run()
	{
		for (;;)
		{
			System.out.println("Waiting for server...");
			try (final Socket socket = _serverSocket.accept();)
			{
				System.out.println("Opened connection to " + socket.getInetAddress());
				openConnection(socket);
			} catch (IOException e)
			{
				System.err.println("Unable to accept connection!");
				e.printStackTrace();
				continue;
			}
		}
	}
	
	public void openConnection(Socket socket)
	{
		Connection connection;
		try
		{
			connection = new Connection(socket);
		} catch (IOException e)
		{
			System.err.println("Unable to open connection!");
			e.printStackTrace();
			return;
		}
		
		ConnectionRegistry connectionRegistry = ServiceLocator.getConnectionRegistry();
		if (connectionRegistry == null)
		{
			System.err.println("No Connection registry!!!!");
		}
		else
		{
			connectionRegistry.register(connection);
		}
	}
}
