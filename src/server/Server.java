package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import common.ServiceLocator;
import common.intfce.ServerConnection;
import common.message.CloseMessage;

final class Server extends Thread
{
	boolean running = true;

	public void run()
	{
		for (;;)
		{
			boolean missingConnection = true;
			Iterator<String> targets = ServiceLocator.getTargets();
			while (targets.hasNext())
			{
				String id = targets.next();
				if (ServiceLocator.getServerConnection(id) == null && !open(id))
				{
					missingConnection = true;
				}
			}

			if (missingConnection)
			{
				waitAgainShort();
			} else
			{
				waitAgainLong();
			}

			if (!running)
			{
				killConnections();
				break;
			}
		}
	}

	private void killConnections()
	{
		Iterator<String> targets = ServiceLocator.getTargets();
		while (targets.hasNext())
		{
			String id = targets.next();

			ServerConnection serverConnection = ServiceLocator.getServerConnection(id);
			if (serverConnection == null)
			{
				continue;
			}

			try
			{
				serverConnection.applyMessage(new CloseMessage());
			} catch (IOException e)
			{
				System.err.println("Unable to close connection!");
				e.printStackTrace();
			}
		}
	}

	private static boolean open(String address)
	{
		final Socket socket;
		try
		{
			String[] parts = address.split(":");
			if (parts.length != 2)
			{
				System.err.println("Bad address, it needs a port: " + address);
				return false;
			}

			int port;
			try
			{
				port = new Integer(parts[1]).intValue();
			} catch (NumberFormatException ex)
			{
				System.err.println("Port is not a number: " + address);
				return false;
			}
			socket = new Socket(parts[0], port);
		} catch (IOException e)
		{
			System.out.println("Unable to connect to " + address);
			return false;
		}

		try
		{
			ServerConnection connection = new ServerConnectionImpl(socket);
			ServiceLocator.setServerConnection(connection.getId(), connection);
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void die()
	{
		running = false;
	}

	private void waitAgainShort()
	{
		waitAgainLong();
	}

	private static void waitAgainLong()
	{
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e1)
		{
			e1.printStackTrace();
			System.exit(-1);
		}
	}
}
