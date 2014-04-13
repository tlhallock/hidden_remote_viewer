package robot;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import common.ControlException;
import common.GrabException;
import common.ServiceLocator;
import common.intfce.Grabber;
import common.message.ControlMessage;

final class MouseGrabber implements Grabber
{
	static final String ID = "mouse.grabber";

	private Point prevPoint = null;

	@Override
	public String getId()
	{
		return ID;
	}

	@Override
	public ControlMessage grab() throws GrabException
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

	private static final class MouseMover implements ControlMessage
	{
		private static final long serialVersionUID = 1L;

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
	}
}
