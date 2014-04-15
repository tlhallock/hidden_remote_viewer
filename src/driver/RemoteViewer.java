package driver;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Timer;

import common.ServiceLocator;

import connection.ConnectionRegistry;
import connection.Peer;
import connection.PeerMonitor;
import connection.ServerHandler;

public class RemoteViewer
{
	public static void main(String[] args) throws AWTException, IOException
	{
		ServiceLocator.setSettings(new Settings());
		
		String ip = InetAddress.getLocalHost().getHostName();
		int port = 2828;
		ServiceLocator.setLocalUser(new Peer(ip, port));
		
		ServiceLocator.setRobot(new Robot());
		
		ServiceLocator.setConnectionRegistry(new ConnectionRegistry());

		ServerSocket serverSocket = new ServerSocket(port);
		ServiceLocator.setServerThread(new ServerHandler(serverSocket));

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new PeerMonitor(), 1000, 1000);
	}
}
