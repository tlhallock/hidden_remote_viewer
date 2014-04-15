package common.message;

import java.io.InputStream;
import java.io.OutputStream;

import common.ControlException;

public class CloseMessage implements Message
{
	@Override
	public void performAction() throws ControlException
	{
	}

	@Override
	public void readFrom(InputStream in)
	{
		
	}

	@Override
	public void writeTo(OutputStream out)
	{
		
	}

	@Override
	public MessageType getUniqueType()
	{
		return MessageType.CLOSE_MESSAGE;
	}
}
