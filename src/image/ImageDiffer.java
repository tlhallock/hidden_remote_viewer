package image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImageDiffer
{
	private final String _format = "png";
	
	private int[][] _old;
	private int[][] _new;
	
	public ImageDiffer(int witdh, int height)
	{
		
	}
	
	public ImageDiff getDiff(BufferedImage image)
	{
		return newImageDiff(image);
	}
	
	private NewImageDiff newImageDiff(BufferedImage screen)
	{
		final byte[] imageBytes;
		try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();)
		{
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
		
		return new NewImageDiff(imageBytes);
	}
	
	public void newToOld()
	{
		
	}
	
	
	public void writeImageDiff(ImageDiff diff, OutputStream out) throws IOException
	{
		
	}
	
	public ImageDiff readImageDiff(InputStream in) throws IOException
	{
		return null;
	}
}
