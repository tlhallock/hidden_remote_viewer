package common;

public class ControlException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ControlException(String message, Exception original)
	{
		super(message, original);
	}

	public ControlException(String message)
	{
		super(message);
	}
}
