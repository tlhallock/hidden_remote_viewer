package connection;

import java.io.IOException;
import java.util.HashSet;
import java.util.TimerTask;

import common.ServiceLocator;

public class PeerMonitor extends TimerTask
{
	private HashSet<Peer> _peers = new HashSet<>();
	
	public void addPeer(Peer peer)
	{
		_peers.add(peer);
	}
	
	public void removePeer(Peer peer)
	{
		_peers.remove(peer);
	}

	@Override
	public void run()
	{
		ConnectionRegistry connectionRegistry = ServiceLocator.getConnectionRegistry();
		if (connectionRegistry == null)
		{
			System.err.println("No connection registry!!");
			return;
		}
		
		for (Peer peer : _peers)
		{
			if (connectionRegistry.isConnected(peer))
			{
				return;
			}
			
			try
			{
				ServerHandler serverThread = ServiceLocator.getServerThread();
				if (serverThread == null)
				{
					System.err.println("No server!");
					return;
				}
				serverThread.openConnection(peer.open());
			} catch (IOException e)
			{
				System.out.println("Unable to connect to " + peer);
				continue;
			}
		}
	}
}
