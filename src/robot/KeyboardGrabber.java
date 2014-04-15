package robot;

import common.GrabException;
import common.intfce.Grabber;
import common.message.Message;

class KeyboardGrabber implements Grabber
{
	@Override
	public GrabberType getType()
	{
		return GrabberType.KEYBOARD_GRABBER;
	}

	@Override
	public Message grab() throws GrabException
	{
		return null;
	}

	@Override
	public void clearCache()
	{
		// TODO Auto-generated method stub
		
	}
}
