package connection;

import java.util.HashSet;

import common.GrabException;
import common.intfce.Grabber;

public class ConnectionRegistry
{
	// Should be a hash map
	private HashSet<Connection> _connections = new HashSet<>();
	
	public synchronized void register(Connection connection)
	{
		if (_connections.contains(connection))
		{
			connection.die();
		}
		_connections.add(connection);
	}

	public synchronized void unregister(Connection connection)
	{
		if (!_connections.remove(connection))
		{
			System.err.println("Killing nonexistant connection.");
		}
		
		connection.die();
	}
	
	public synchronized Connection getConnection(Peer peer)
	{
		for (Connection connection : _connections)
		{
			if (peer.equals(connection.getPeer()))
			{
				return connection;
			}
		}
		return null;
	}
	
	public boolean isConnected(Peer peer)
	{
		return getConnection(peer) != null;
	}
	
	public synchronized void apply(Grabber grabber) throws GrabException
	{
		for (Connection connection : _connections)
		{
			connection.apply(grabber);
		}
	}
	
	public synchronized void killAll()
	{
		for (Connection connection : _connections)
		{
			connection.die();
		}
		_connections.clear();
	}
}
