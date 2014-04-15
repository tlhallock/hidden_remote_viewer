package driver;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Timer;


import common.ServiceLocator;
import connection.ConnectionRegistry;
import connection.PeerMonitor;
import connection.ServerHandler;

public class RemoteViewer
{
	public static void main(String[] args) throws AWTException, IOException
	{
		ServiceLocator.setRobot(new Robot());
		ServiceLocator.setSettings(new Settings());

		ServiceLocator.setConnectionRegistry(new ConnectionRegistry());

		ServerSocket serverSocket = new ServerSocket(2828);
		ServiceLocator.setServerThread(new ServerHandler(serverSocket));

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new PeerMonitor(), 1000, 1000);
	}
}
