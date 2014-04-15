package common.message;

import java.io.InputStream;
import java.io.OutputStream;

import common.ControlException;

public interface Message
{
	MessageType getUniqueType();

	void readFrom(InputStream in);

	void writeTo(OutputStream out);

	void performAction() throws ControlException;

	public enum MessageType
	{
		CLOSE_MESSAGE, SCREEN_UPDATE, MOUSE_UPDATE, KEYBOARD_UPDATE
	}
}
