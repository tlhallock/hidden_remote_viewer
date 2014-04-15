package robot;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.ControlException;
import common.ServiceLocator;
import common.intfce.ClientDisplay;
import common.intfce.Grabber;
import common.message.Message;

final class ScreenGrabber implements Grabber
{
	private Rectangle _captureRegion;
	private String _format = "png";

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
		final byte[] imageBytes;
		BufferedImage screen;
		try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();)
		{
			screen = getScreen();
			if (screen == null)
			{
				return null;
			}
			ImageIO.write(screen, _format, outStream);
			outStream.close();
			imageBytes = outStream.toByteArray();
		} catch (IOException e)
		{
			System.err.println("Unable to grab image:");
			e.printStackTrace();
			return null;
		}
		return new ScreenControlMessage(imageBytes, screen.getWidth(), screen.getHeight());
	}

	private static final class ScreenControlMessage implements Message
	{
		private static final long serialVersionUID = 1L;

		private final byte[] _image;
		private final int _width;
		private final int _height;

		private ScreenControlMessage(byte[] imageBytes, int width, int height)
		{
			_image = imageBytes;
			_width = width;
			_height = height;
		}

		@Override
		public void performAction() throws ControlException
		{
			ClientDisplay display = ServiceLocator.getClientDisplay();
			if (display == null)
			{
				throw new ControlException("No display to display screen capture!");
			}

			BufferedImage screen;
			try
			{
				screen = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(_image)));
			} catch (IOException e)
			{
				e.printStackTrace();
				throw new ControlException("Unable to read image!", e);
			}

			display.display(screen);
		}

		public String toString()
		{
			return "Image message with width = " + _width + ", height = " + _height;
		}
	}

	@Override
	public void clearCache()
	{
		// TODO Auto-generated method stub
		
	}
}
