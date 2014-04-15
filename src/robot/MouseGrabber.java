package robot;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.InputStream;
import java.io.OutputStream;

import common.ControlException;
import common.GrabException;
import common.ServiceLocator;
import common.intfce.Grabber;
import common.message.Message;

final class MouseGrabber implements Grabber
{
	private Point prevPoint = null;

	@Override
	public GrabberType getType()
	{
		return GrabberType.MOUSE_GRABBER;
	}

	@Override
	public Message grab() throws GrabException
	{
		Point newPoint = MouseInfo.getPointerInfo().getLocation();
		if (prevPoint == null)
		{
			prevPoint = newPoint;
			return new MouseMover(new Point(0, 0));
		} else
		{
			return new MouseMover(new Point(newPoint.x - prevPoint.x, newPoint.y - prevPoint.y));
		}
	}

	private static final class MouseMover implements Message
	{
		private final Point _delta;

		private MouseMover(Point delta)
		{
			_delta = delta;
		}

		@Override
		public void performAction() throws ControlException
		{
			Robot robot = ServiceLocator.getRobot();
			if (robot == null)
			{
				throw new ControlException("Unable to get robot!");
			}

			Point prevLocation = MouseInfo.getPointerInfo().getLocation();
			robot.mouseMove(prevLocation.x + _delta.x, prevLocation.y + _delta.y);
		}

		@Override
		public MessageType getUniqueType()
		{
			return MessageType.MOUSE_UPDATE;
		}

		@Override
		public void readFrom(InputStream in)
		{
			
		}

		@Override
		public void writeTo(OutputStream out)
		{
			
		}
	}

	@Override
	public void clearCache()
	{
		// TODO Auto-generated method stub
		
	}
}
