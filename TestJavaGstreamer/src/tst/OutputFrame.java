package tst;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.gstreamer.swing.VideoComponent;

public class OutputFrame
{
	public static VideoComponent showFrame(final int width, final int height) throws AWTException
	{
		VideoComponent panel = new VideoComponent();
		
		JFrame frame = new JFrame("FakeSrcTest");
		panel.setPreferredSize(new Dimension(width, height));
		frame.add(panel, BorderLayout.CENTER);
		frame.setSize(640, 480);
		frame.setLocation(500,500);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		return panel;
	}
}
