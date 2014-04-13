package driver;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;

import server.ServerDriver;
import client.ClientDriver;

import common.ServiceLocator;

public class RemoteViewer
{
	public static void main(String[] args) throws AWTException, IOException
	{
		boolean doServer = false;

		ServiceLocator.setRobot(new Robot());
		ServiceLocator.setSettings(new Settings());

		ServiceLocator.setIsServer(doServer);

		if (doServer)
		{
			ServerDriver.initialize();
		} else
		{
			ClientDriver.initialize();
		}
	}
}
