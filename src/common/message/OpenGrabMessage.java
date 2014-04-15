package common.message;

import robot.GrabberDriver;
import common.ControlException;
import common.ServiceLocator;
import common.intfce.Grabber.GrabberType;
import connection.Peer;

public class OpenGrabMessage implements Message
{
	private static final long serialVersionUID = 1L;
	
	private final GrabberType _id;
	private final Peer _peer;
	
	public OpenGrabMessage(GrabberType id, Peer peer)
	{
		_id = id;
		_peer = peer;
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
}
