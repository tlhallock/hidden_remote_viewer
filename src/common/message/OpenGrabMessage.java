package common.message;

import java.io.InputStream;
import java.io.OutputStream;

import robot.GrabberDriver;

import common.ControlException;
import common.ServiceLocator;
import common.intfce.Grabber.GrabberType;

import connection.Peer;

public class OpenGrabMessage implements Message
{
	private final GrabberType _id;
	private final Peer _peer;
	
	public OpenGrabMessage(GrabberType id)
	{
		_id = id;
		_peer = ServiceLocator.getLocalUser();
	}
	
	@Override
	public void performAction() throws ControlException
	{
		GrabberDriver grabberDriver = ServiceLocator.getGrabberDriver();
		if (grabberDriver == null)
		{
			System.err.println("No Grabber driver!!!");
			return;
		}
		
		grabberDriver.applyGrabber(_id, _peer);
	}

	@Override
	public MessageType getUniqueType()
	{
		return null;
	}

	@Override
	public void readFrom(InputStream in)
	{
		
	}

	@Override
	public void writeTo(OutputStream out)
	{
		
	}
}
