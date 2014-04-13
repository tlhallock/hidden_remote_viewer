package robot;

import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import common.GrabException;
import common.ServiceLocator;
import common.intfce.ClientThread;
import common.intfce.Grabber;
import common.intfce.ServerConnection;
import common.message.ControlMessage;

public class GrabberDriver
{
	private static void initializeGrabber(final Grabber grabber)
	{
		String id = grabber.getId();
		ServiceLocator.setGrabber(id, grabber);

		Timer timer = new Timer();
		long timeout = 1000;

		ServiceLocator.setTimer(id, timer);

		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				if (ServiceLocator.isServer())
				{
					applyGrabberToServerConnections(grabber);
				} else
				{
					applyGrabberToClientConnection(grabber);
				}
			}
		}, timeout, timeout);
	}

	private static void destroyGrabber(final String id)
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
			ServiceLocator.setTimer(grabber.getId(), null);
		}
	}

	private static void applyGrabberToClientConnection(Grabber grabber)
	{
		ClientThread clientThread = ServiceLocator.getClientThread();
		if (clientThread == null)
		{
			System.err.println("No client thread for " + grabber + "!!!");
			return;
		}
		try
		{
			clientThread.sendRequest(grabber.grab());
		} catch (IOException | GrabException e)
		{
			System.err.println("Unable to send request from " + grabber);
			e.printStackTrace();
		}
	}

	private static void applyGrabberToServerConnections(Grabber grabber)
	{
		ControlMessage grab = null;

		Iterator<String> targets = ServiceLocator.getTargets();
		while (targets.hasNext())
		{
			String target = targets.next();
			ServerConnection serverConnection = ServiceLocator.getServerConnection(target);
			if (serverConnection == null)
			{
				continue;
			}

			if (grab == null)
			{
				try
				{
					grab = grabber.grab();
				} catch (GrabException e)
				{
					System.err.println("Unable to grab:");
					e.printStackTrace();
					return;
				}

				if (grab == null)
				{
					return;
				}
			}

			try
			{
				serverConnection.applyMessage(grab);
			} catch (IOException e)
			{
				System.err.println("Unable to write message to " + target + "!");
				e.printStackTrace();
			}
		}
	}

	public static void initializeScreenGrabber()
	{
		if (ServiceLocator.getGrabber(ScreenGrabber.ID) != null)
		{
			return;
		}
		initializeGrabber(new ScreenGrabber());
	}

	public static void destroyScreenGrabber()
	{
		destroyGrabber(ScreenGrabber.ID);
	}

	public static void initializeMouseGrabber()
	{
		if (ServiceLocator.getGrabber(MouseGrabber.ID) != null)
		{
			return;
		}
		initializeGrabber(new MouseGrabber());
	}

	public static void destroyMouseGrabber()
	{
		destroyGrabber(MouseGrabber.ID);
	}
}
