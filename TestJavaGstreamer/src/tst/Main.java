package tst;

import java.awt.AWTException;

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.TagList;
import org.gstreamer.elements.AppSrc;
import org.gstreamer.swing.VideoComponent;

public class Main
{
	private static Pipeline pipeline;
	static TagList tags;
	
	public static void main(String[] args) throws AWTException
	{
		int width = 1000;
		int height = 1000;
		
		args = Gst.init("AppSrcTest", args);

		/* setup pipeline */
		pipeline = new Pipeline("pipeline");

		final String framerate = "4/1";
		final String bpp = "16";

		final AppSrc appsrc = (AppSrc) ElementFactory.make("appsrc", "source");
		final Element srcfilter = ElementFactory.make("capsfilter", "srcfilter");

		Caps fltcaps = new Caps("video/x-raw-rgb, framerate=" + framerate + ", width=" + width + ", height=" + height + ", bpp=" + bpp + ", depth=" + bpp);
		srcfilter.setCaps(fltcaps);

		final Element videorate = ElementFactory.make("videorate", "videorate");
		final Element ratefilter = ElementFactory.make("capsfilter", "RateFilter");
		ratefilter.setCaps(Caps.fromString("video/x-raw-rgb, framerate=" + framerate));
		
		VideoComponent panel = OutputFrame.showFrame(width, height);
		Element videosink = panel.getElement();
		
		pipeline.addMany(appsrc, srcfilter, videorate, ratefilter, videosink);
		Element.linkMany(appsrc, srcfilter, videorate, ratefilter, videosink);

		appsrc.set("emit-signals", true);
		Capturer.connectCapturer(appsrc, width, height);
		
		pipeline.setState(State.PLAYING);
	}
}
