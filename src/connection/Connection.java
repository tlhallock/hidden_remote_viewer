package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

import common.ControlException;
import common.GrabException;
import common.ServiceLocator;
import common.intfce.Grabber;
import common.intfce.Grabber.GrabberType;
import common.message.CloseMessage;
import common.message.Message;

public class Connection
{
	private final Socket _socket;

	private final ObjectInputStream _in;
	private final ObjectOutputStream _out;
	
	private final Peer _endPoint;

	private ListenThread _listener;
	
	private final HashSet<GrabberType> _grabbers;

	public Connection(Socket socket) throws IOException
	{
		_socket = socket;

		_in = new ObjectInputStream(socket.getInputStream());
		_out = new ObjectOutputStream(socket.getOutputStream());
		
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
			_out.writeObject(msg);
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
		final Object readObject;
		try
		{
			readObject = _in.readObject();
		} catch (ClassNotFoundException | IOException e)
		{
			System.err.println("Unable to read message. Terminating connection");
			e.printStackTrace();
			return false;
		}

		if (!(readObject instanceof Message))
		{
			System.err.println("Received invalid object from server:");
			System.err.println(readObject.getClass().getName());
			System.err.println(readObject);
			
			return true;
		}

		Message message = (Message) readObject;
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
