package common;

import java.awt.Robot;
import java.util.HashMap;
import java.util.Timer;

import robot.GrabberDriver;
import window.ScreenCaptureDisplay;

import common.intfce.Grabber;
import common.intfce.Grabber.GrabberType;

import connection.ConnectionRegistry;
import connection.Peer;
import connection.ServerHandler;
import driver.Settings;

public final class ServiceLocator
{
	private static final HashMap<Peer, ScreenCaptureDisplay> _displays = new HashMap<>();

	public static void setClientDisplay(Peer peer, ScreenCaptureDisplay display)
	{
		_displays.put(peer, display);
	}

	public static ScreenCaptureDisplay getClientDisplay(Peer peer)
	{
		return _displays.get(peer);
	}

	private static ServerHandler _serverThread = null;

	public static void setServerThread(ServerHandler serverThread)
	{
		_serverThread = serverThread;
	}

	public static ServerHandler getServerThread()
	{
		return _serverThread;
	}

	private static HashMap<GrabberType, Timer> _timers = new HashMap<>();

	public static void setTimer(GrabberType id, Timer timer)
	{
		_timers.put(id, timer);
	}

	public static Timer getTimer(GrabberType id)
	{
		return _timers.get(id);
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

	private static Settings _settings;

	public static void setSettings(Settings settings)
	{
		_settings = settings;
	}

	public Settings getSettings()
	{
		return _settings;
	}

	private static HashMap<GrabberType, Grabber> _grabbers = new HashMap<>();

	public static void setGrabber(GrabberType id, Grabber grabber)
	{
		_grabbers.put(id, grabber);
	}

	public static Grabber getGrabber(GrabberType type)
	{
		return _grabbers.get(type);
	}

	private static ConnectionRegistry _connections;

	public static void setConnectionRegistry(ConnectionRegistry connections)
	{
		_connections = connections;
	}

	public static ConnectionRegistry getConnectionRegistry()
	{
		return _connections;
	}

	private static GrabberDriver _driver;

	public static void setGrabber(GrabberDriver driver)
	{
		_driver = driver;
	}

	public static GrabberDriver getGrabberDriver()
	{
		return _driver;
	}

	private static Peer _localUser;

	public static Peer getLocalUser()
	{
		return _localUser;
	}

	public static void setLocalUser(Peer peer)
	{
		_localUser = peer;
	}
}
