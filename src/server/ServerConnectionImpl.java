package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.ServiceLocator;
import common.intfce.ServerConnection;
import common.message.CloseMessage;
import common.message.ControlMessage;

final class ServerConnectionImpl implements ServerConnection
{
	private ObjectInputStream _input;
	private ObjectOutputStream _output;

	private final String _id;

	private final Thread _listenThread;

	ServerConnectionImpl(Socket socket) throws IOException
	{
		_input = new ObjectInputStream(socket.getInputStream());
		_output = new ObjectOutputStream(socket.getOutputStream());

		_id = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

		_listenThread = new Thread(new ServerListener(_input));
		_listenThread.start();
	}

	public void applyMessage(ControlMessage message) throws IOException
	{
		try
		{
			_output.writeObject(message);
		} catch (IOException e)
		{
			System.err.println("Failed to write message" + message);
			die();
			throw e;
		}

		if (message instanceof CloseMessage)
		{
			die();
		}
	}

	private void die()
	{
		ServiceLocator.setServerConnection(_id, null);

		try
		{
			_input.close();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}

		try
		{
			_output.close();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}

		_listenThread.interrupt();
	}

	@Override
	public String getId()
	{
		return _id;
	}
}
