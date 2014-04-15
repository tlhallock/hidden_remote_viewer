package robot;

import java.util.Timer;
import java.util.TimerTask;


import common.GrabException;
import common.ServiceLocator;
import common.intfce.Grabber;
import common.intfce.Grabber.GrabberType;
import connection.Connection;
import connection.ConnectionRegistry;
import connection.Peer;

public class GrabberDriver
{
	private static Grabber newGrabber(GrabberType id)
	{
		switch(id)
		{
		case KEYBOARD_GRABBER:
			return new KeyboardGrabber();
		case MOUSE_GRABBER:
			return new MouseGrabber();
		case SCREEN_GRABBER:
			return new ScreenGrabber();
			default:
				System.err.println("Unkown grabber: " + id);
				return null;
		}
	}
	
	private static void initializeGrabber(GrabberType id)
	{
		Grabber oldGrabber = ServiceLocator.getGrabber(id);
		if (oldGrabber != null)
		{
			return;
		}
		
		final Grabber grabber = newGrabber(id);
		ServiceLocator.setGrabber(id, grabber);

		Timer timer = new Timer();
		long timeout = 1000;

		ServiceLocator.setTimer(id, timer);

		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				applyGrabber(grabber);
			}
		}, timeout, timeout);
	}

	private static void destroyGrabber(final GrabberType id)
	{
		Grabber grabber = ServiceLocator.getGrabber(id);
		if (grabber == null)
		{
			return;
		}

		ServiceLocator.setGrabber(id, null);

		Timer timer = ServiceLocator.getTimer(id);
		if (timer != null)
		{
			timer.cancel();
			ServiceLocator.setTimer(id, null);
		}
	}

	private static void applyGrabber(Grabber grabber)
	{
		ConnectionRegistry connectionRegistry = ServiceLocator.getConnectionRegistry();
		if (connectionRegistry == null)
		{
			System.err.println("No connection registry");
			return;
		}

		try
		{
			connectionRegistry.apply(grabber);
		} catch (GrabException e)
		{
			System.err.println("Unable to grab!!");
			e.printStackTrace();
		}
		
		grabber.clearCache();
	}
	
	public void removeGrabber(GrabberType id, Peer peer)
	{
		ConnectionRegistry connectionRegistry = ServiceLocator.getConnectionRegistry();
		if (connectionRegistry == null)
		{
			System.err.println("No connection registry!!");
			return;
		}

		Connection connection = connectionRegistry.getConnection(peer);
		if (connection == null)
		{
			System.err.println("Not connected to " + peer);
			return;
		}
		
		connection.removeGrabber(id);
		
		if (Math.random() < 0 /*no more peers have this grabber*/)
		{
			destroyGrabber(id);
		}
	}

	public void applyGrabber(GrabberType grabberId, Peer peer)
	{
		ConnectionRegistry connectionRegistry = ServiceLocator.getConnectionRegistry();
		if (connectionRegistry == null)
		{
			System.err.println("No connection registry!!");
			return;
		}

		Connection connection = connectionRegistry.getConnection(peer);
		if (connection == null)
		{
			System.err.println("Not connected to " + peer);
			return;
		}
		
		GrabberDriver.initializeGrabber(grabberId);
		connection.addGrabber(grabberId);
	}
}
