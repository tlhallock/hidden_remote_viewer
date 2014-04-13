package common.intfce;

import common.GrabException;
import common.message.ControlMessage;

public interface Grabber
{
	String getId();

	ControlMessage grab() throws GrabException;
}
