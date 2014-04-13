package common.intfce;

import java.io.IOException;

import common.message.ControlMessage;

public interface ClientThread
{
	void sendRequest(ControlMessage msg) throws IOException;
}
