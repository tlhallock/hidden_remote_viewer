package image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import common.ControlException;

public class NewImageDiff implements ImageDiff
{
	private final byte[] newImage;

	public NewImageDiff(byte[] imageBytes)
	{
		newImage = imageBytes;
	}

	@Override
	public void apply(ImageHolder image) throws ControlException
	{
		BufferedImage screen;
		try
		{
			screen = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(newImage)));
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new ControlException("Unable to read image!", e);
		}

		image.image = screen;
	}

	@Override
	public void writeTo(OutputStream output)
	{
		
	}

	@Override
	public void readFrom(InputStream input)
	{
		
	}
}
