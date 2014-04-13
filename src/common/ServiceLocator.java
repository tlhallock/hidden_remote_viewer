package common;

import java.awt.Robot;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

import common.intfce.ClientDisplay;
import common.intfce.ClientThread;
import common.intfce.Grabber;
import common.intfce.ServerConnection;

import driver.Settings;

public final class ServiceLocator
{
	private static ClientDisplay _display = null;

	public static void setClientDisplay(ClientDisplay display)
	{
		_display = display;
	}

	public static ClientDisplay getClientDisplay()
	{
		return _display;
	}

	private static ClientThread _clientThread = null;

	public static void setClientThread(ClientThread clientThread)
	{
		_clientThread = clientThread;
	}

	public static ClientThread getClientThread()
	{
		return _clientThread;
	}

	private static Thread _serverThread = null;

	public static void setServerThread(Thread serverThread)
	{
		_serverThread = serverThread;
	}

	public static Thread getServerThread()
	{
		return _serverThread;
	}

	private static HashMap<String, Timer> _timers = new HashMap<>();

	public static void setTimer(String id, Timer timer)
	{
		_timers.put("timers." + id, timer);
	}

	public static Timer getTimer(String id)
	{
		return _timers.get("timers." + id);
	}

	private static HashMap<String, ServerConnection> _serverConnection = new HashMap<>();

	public static void setServerConnection(String id, ServerConnection connection)
	{
		_serverConnection.put("server.connection" + id, connection);
	}

	public static ServerConnection getServerConnection(String id)
	{
		return _serverConnection.get("server.connection" + id);
	}

	private static Robot _robot;

	public static void setRobot(Robot robot)
	{
		_robot = robot;
	}

	public static Robot getRobot()
	{
		return _robot;
	}

	private static Set<String> _targets = new HashSet<>();

	public static void addTarget(String address)
	{
		_targets.add(address);
	}

	public static Iterator<String> getTargets()
	{
		return _targets.iterator();
	}

	private static Settings _settings;

	public static void setSettings(Settings settings)
	{
		_settings = settings;
	}

	public Settings getSettings()
	{
		return _settings;
	}

	private static HashMap<String, Grabber> _grabbers = new HashMap<>();

	public static void setGrabber(String id, Grabber grabber)
	{
		_grabbers.put("grabber." + id, grabber);
	}

	public static Grabber getGrabber(String id)
	{
		return _grabbers.get(id);
	}

	private static boolean _isServer;

	public static void setIsServer(boolean isServer)
	{
		_isServer = isServer;
	}

	public static boolean isServer()
	{
		return _isServer;
	}
}
