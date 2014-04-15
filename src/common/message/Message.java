package common.message;

import java.io.Serializable;

import common.ControlException;

public interface Message extends Serializable
{
	void performAction() throws ControlException;
}
