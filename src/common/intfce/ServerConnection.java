package common.intfce;

import java.io.IOException;

import common.message.ControlMessage;

public interface ServerConnection
{
	String getId();

	void applyMessage(ControlMessage msg) throws IOException;
}
