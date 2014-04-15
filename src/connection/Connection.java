package connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

import common.ControlException;
import common.GrabException;
import common.ServiceLocator;
import common.intfce.Grabber;
import common.intfce.Grabber.GrabberType;
import common.message.CloseMessage;
import common.message.Message;
import common.message.MessageDriver;

public class Connection
{
	private final Socket _socket;

	private final InputStream _in;
	private final OutputStream _out;
	
	private final Peer _endPoint;

	private ListenThread _listener;
	
	private final HashSet<GrabberType> _grabbers;

	public Connection(Socket socket) throws IOException
	{
		_socket = socket;

		_in = socket.getInputStream();
		_out = socket.getOutputStream();
		
		_endPoint = new Peer(_socket.getInetAddress().getHostAddress(), _socket.getPort());
		
		_grabbers = new HashSet<>();
		
		_listener = new ListenThread();
		_listener.start();
	}

	public int hashCode()
	{
		return _endPoint.hashCode();
	}
	
	public boolean equals(Object other)
	{
		if (other instanceof Connection)
		{
			return false;
		}
		
		return ((Connection)other)._endPoint.equals(_endPoint);
	}

	public Peer getPeer()
	{
		return _endPoint;
	}

	public void apply(Grabber grabber) throws GrabException
	{
		if (!_grabbers.contains(grabber.getType()))
		{
			return;
		}
		
		Message msg = grabber.grab();
		try
		{
			MessageDriver.writeMessage(msg, _out);
		} catch (IOException e)
		{
			System.err.println("Unable to send control message! terminating connection.");
			e.printStackTrace();
			die();
		}
	}

	private boolean dead;
	public void die()
	{
		if (dead)
		{
			return;
		}
		
		dead = true;
		
		ConnectionRegistry connectionRegistry = ServiceLocator.getConnectionRegistry();
		if (connectionRegistry == null)
		{
			System.err.println("No connection registry!");
		}
		else
		{
			connectionRegistry.unregister(this);
		}
		
		if (_listener != null 
				&& !Thread.currentThread().equals(_listener))
		{
			_listener.interrupt();
		}
		
		try
		{
			_out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			_in.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			_socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void addGrabber(GrabberType id)
	{
		_grabbers.add(id);
	}
	
	public void removeGrabber(GrabberType id)
	{
		_grabbers.remove(id);
	}

	private boolean handleNextMessage()
	{
		final Message message;
		try
		{
			message = MessageDriver.readMessage(_in);
		} catch (IOException e)
		{
			System.err.println("Unable to read message. Terminating connection");
			e.printStackTrace();
			return false;
		}

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
			System.out.println("Receive close message.");
			return false;
		}

		return true;
	}

	private class ListenThread extends Thread
	{
		@Override
		public void run()
		{
			while (handleNextMessage())
				;

			die();
		}
	}
}
