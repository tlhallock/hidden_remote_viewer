package tst;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Timer;
import java.util.TimerTask;

import org.gstreamer.Buffer;
import org.gstreamer.elements.AppSrc;

public class Capturer implements AppSrc.NEED_DATA, AppSrc.ENOUGH_DATA
{
	private static Robot robot;

	private int width;
	private int height;
	private int image_length;

	private Rectangle captureRegion;

	private AppSrc appsrc;
	
	int[][] old;
	
	private Capturer(AppSrc appsrc, int width, int height)
	{
		captureRegion = new Rectangle(50, 50, width, height);

		this.width = width;
		this.height = height;
		old = new int[width][height];
		
		image_length = width * height * 2;
		this.appsrc = appsrc;
	}

	Buffer getBuffer()
	{
		long start_time = System.currentTimeMillis();
		Buffer buffer = new Buffer(image_length);

		BufferedImage capture = robot.createScreenCapture(captureRegion);
		ColorModel colorModel = capture.getColorModel();
		System.out.println("screen capture: " + (System.currentTimeMillis() - start_time));
		start_time = System.currentTimeMillis();
		
		boolean changed = true;

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int rgb = capture.getRGB(j, i);
//				if (old[j][i] != rgb)
//				{
//					changed = true;
//					old[j][i] = rgb;
//				}

				final int red = colorModel.getRed(rgb);
				final int green = colorModel.getGreen(rgb);
				final int blue = colorModel.getBlue(rgb);

				final int bpr = 5;
				final int bpg = 6;
				final int bpb = 5;

				final int rc = (int) (red * (((1 << bpr) - 1) / (double) 255));
				final int gc = (int) (green * (((1 << bpg) - 1) / (double) 255));
				final int bc = (int) (blue * (((1 << bpb) - 1) / (double) 255));

				int color = (rc << (bpg + bpb)) | (gc << bpb) | bc;
				buffer.getByteBuffer().put((byte) ((color >> 0) & 0xff));
				buffer.getByteBuffer().put((byte) ((color >> 8) & 0xff));
			}
		}
		
		if (!changed)
		{
			return null;
		}
		
		System.out.println("buffer creation: " + (System.currentTimeMillis() - start_time));
		
		return buffer;
	}
	
	public void needData(AppSrc elem, int size)
	{
		Buffer buffer;

		final Object syncObject = new Object();
		synchronized (syncObject)
		{
			while ((buffer = getBuffer()) == null)
			{
				try
				{
					syncObject.wait(500);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		appsrc.pushBuffer(buffer);
	}

	public void enoughData(AppSrc elem)
	{
		System.out.println("NEED_DATA: Element=" + elem.getNativeAddress());
	}

	public static void connectCapturer(final AppSrc appsrc, final int width, final int height) throws AWTException
	{
		if (robot == null)
		{
			robot = new Robot();
		}

		Capturer c = new Capturer(appsrc, width, height);
		appsrc.connect((AppSrc.NEED_DATA) c);
		appsrc.connect((AppSrc.ENOUGH_DATA) c);
	}
}
