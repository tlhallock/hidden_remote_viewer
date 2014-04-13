package common;

public class GrabException extends Exception
{
	private static final long serialVersionUID = 1L;

	public GrabException(String message, Exception original)
	{
		super(message, original);
	}
}
