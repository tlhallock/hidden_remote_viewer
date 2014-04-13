package server;

import java.io.IOException;

import robot.GrabberDriver;

import common.ServiceLocator;

public class ServerDriver
{
	public static void initialize() throws IOException
	{
		ServiceLocator.addTarget("127.0.0.1:2828");

		if (ServiceLocator.getServerThread() == null)
		{
			Server server = new Server();
			server.start();
			ServiceLocator.setServerThread(server);
		}

		GrabberDriver.initializeScreenGrabber();
	}

	public static void destroy()
	{
		Server server;
		if ((server = (Server) ServiceLocator.getServerThread()) != null)
		{
			ServiceLocator.setServerThread(null);
			server.die();
		}

		GrabberDriver.destroyScreenGrabber();
	}
}
