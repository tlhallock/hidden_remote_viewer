package client;

import java.io.IOException;

import robot.GrabberDriver;

import common.ServiceLocator;
import common.intfce.ClientDisplay;

public class ClientDriver
{
	public static void initialize() throws IOException
	{
		if (ServiceLocator.getClientDisplay() == null)
		{
			System.out.println("Creating display");
			ClientDisplay display = new ClientDisplayImpl();
			ServiceLocator.setClientDisplay(display);
		}

		if (ServiceLocator.getClientThread() == null)
		{
			System.out.println("Creating client thread");
			ClientThreadImpl client = new ClientThreadImpl();
			client.start();
			ServiceLocator.setClientThread(client);
		}

		GrabberDriver.initializeMouseGrabber();
	}

	public static void destroy()
	{
		ClientDisplay display;
		if ((display = ServiceLocator.getClientDisplay()) != null)
		{
			System.out.println("Disposing of displat");
			ServiceLocator.setClientDisplay(null);
			display.destroy();
		}

		ClientThreadImpl client = (ClientThreadImpl) ServiceLocator.getClientThread();
		if (client != null)
		{
			System.out.println("Destroying client thread");
			ServiceLocator.setClientThread(null);
			client.die();
		}

		GrabberDriver.destroyMouseGrabber();
	}
}
