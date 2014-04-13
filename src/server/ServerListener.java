package server;

import java.io.IOException;
import java.io.ObjectInputStream;

import common.ControlException;
import common.message.CloseMessage;
import common.message.ControlMessage;

final class ServerListener implements Runnable
{
	private final ObjectInputStream _input;

	public ServerListener(ObjectInputStream input)
	{
		_input = input;
	}

	@Override
	public void run()
	{
		for (;;)
		{
			Object command;
			try
			{
				command = _input.readObject();
			} catch (IOException | ClassNotFoundException e)
			{
				System.err.println("Error reading message, quiting");
				e.printStackTrace();
				try
				{
					_input.close();
				} catch (IOException ex)
				{
					ex.printStackTrace();
				}
				break;
			}

			if (!(command instanceof ControlMessage))
			{
				System.err.println("Received bad message!");
				System.err.println(command);
				continue;
			}

			ControlMessage msg = (ControlMessage) command;
			try
			{
				msg.performAction();
			} catch (ControlException e)
			{
				System.err.println("Unable to perform action!");
				e.printStackTrace();
			}

			if (msg instanceof CloseMessage)
			{
				try
				{
					_input.close();
				} catch (IOException ex)
				{
					ex.printStackTrace();
					break;
				}
			}
		}
	}
}
