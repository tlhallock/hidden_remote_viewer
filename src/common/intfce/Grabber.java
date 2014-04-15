package common.intfce;

import common.GrabException;
import common.message.Message;

public interface Grabber
{
	// This class should be a delegate passed into the GrabberType enum's constructor...
	GrabberType getType();

	Message grab() throws GrabException;

	void clearCache();
	
	public enum GrabberType
	{
		MOUSE_GRABBER("Control the mouse"),
		KEYBOARD_GRABBER("Control the keyboard"),
		SCREEN_GRABBER("Receive screen changes"),
		;
		
		private final String _description;
		
		GrabberType(String description)
		{
			_description = description;
		}
		
		String getDescription()
		{
			return _description;
		}
	}
}
