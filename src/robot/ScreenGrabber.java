package robot;

import image.ImageDiff;
import image.ImageDiffer;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

import window.ScreenCaptureDisplay;

import common.ControlException;
import common.ServiceLocator;
import common.intfce.Grabber;
import common.message.Message;

import connection.Peer;

final class ScreenGrabber implements Grabber
{
	private Rectangle _captureRegion;

	ScreenGrabber()
	{
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = defaultToolkit.getScreenSize();
		_captureRegion = new Rectangle(0, 0, screenSize.width, screenSize.height);
	}

	@Override
	public GrabberType getType()
	{
		return GrabberType.SCREEN_GRABBER;
	}

	private final BufferedImage getScreen()
	{
		Robot robot = ServiceLocator.getRobot();
		if (robot == null)
		{
			System.err.println("No robot!");
			return null;
		}
		return robot.createScreenCapture(_captureRegion);
	}

	@Override
	public Message grab()
	{
		BufferedImage screen = getScreen();
		
		ImageDiffer differ = null;
		ImageDiff diff = differ.getDiff(screen);
		
		return new ScreenControlMessage(diff, ServiceLocator.getLocalUser());
	}

	private static final class ScreenControlMessage implements Message
	{
		private final ImageDiff _diff;
		private final Peer _peer;

		private ScreenControlMessage(ImageDiff diff, Peer peer)
		{
			_diff = diff;
			_peer = peer;
		}

		@Override
		public void performAction() throws ControlException
		{
			ScreenCaptureDisplay display = ServiceLocator.getClientDisplay(_peer);
			if (display == null)
			{
				// need to create a new one...
				throw new ControlException("No display to display screen capture!");
			}
			display.updateDisplay(_diff);
		}

		@Override
		public MessageType getUniqueType()
		{
			return MessageType.SCREEN_UPDATE;
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
		
	}
}
