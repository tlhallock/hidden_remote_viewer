package connection;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public final class Peer implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String _ip;
	private final int _port;
	
	public Peer(String ip, int port)
	{
		_ip = ip;
		_port = port;
	}
	
	public String getAddress()
	{
		return _ip + ":" + _port;
	}
	
	public int hashCode()
	{
		return getAddress().hashCode();
	}
	
	public String toString()
	{
		return getAddress();
	}
	
	public boolean equals(Object peer)
	{
		if (!(peer instanceof Peer))
		{
			return false;
		}
		
		return ((Peer) peer).getAddress().equals(getAddress());
	}

	public Socket open() throws UnknownHostException, IOException
	{
		return new Socket(_ip, _port);
	}
}
