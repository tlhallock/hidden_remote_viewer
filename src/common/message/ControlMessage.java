package common.message;

import java.io.Serializable;

import common.ControlException;

public interface ControlMessage extends Serializable
{
	void performAction() throws ControlException;
}
