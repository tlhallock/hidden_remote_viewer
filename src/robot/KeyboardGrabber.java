package robot;

import common.GrabException;
import common.intfce.Grabber;
import common.message.ControlMessage;

class KeyboardGrabber implements Grabber
{
	static String ID = "keyboard.grabber";

	@Override
	public String getId()
	{
		return null;
	}

	@Override
	public ControlMessage grab() throws GrabException
	{
		return null;
	}
}
