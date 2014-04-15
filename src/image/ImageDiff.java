package image;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

import common.ControlException;

public interface ImageDiff
{
	public class ImageHolder { public BufferedImage image; }
	
	public void apply(ImageHolder image) throws ControlException;

	public void writeTo(OutputStream output);
	
	public void readFrom(InputStream input);
}
