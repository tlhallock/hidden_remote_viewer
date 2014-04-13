package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import common.intfce.ClientDisplay;

final class ClientDisplayImpl implements ClientDisplay
{
	private final JFrame _frame;
	private final JPanel _label;

	private BufferedImage _lastImage;

	ClientDisplayImpl()
	{
		_frame = new JFrame();
		_frame.setBounds(50, 50, 50, 50);
		_frame.setVisible(true);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setLayout(null);

		_label = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g)
			{
				if (_lastImage == null)
				{
					super.paint(g);
					return;
				}
				g.drawImage(_lastImage, 0, 0, null);
			}
		};
		_frame.add(_label);
		_label.setLayout(null);
		_label.setBackground(Color.red);
		_label.setVisible(true);
		_label.setBounds(0, 0, 50, 50);
	}

	public void display(BufferedImage screen)
	{
		_label.setSize(screen.getWidth(), screen.getHeight());
		_lastImage = screen;
		_frame.repaint();
	}

	public void leaveCurrentServer()
	{
		_frame.setTitle("No connection");
		_lastImage = null;
	}

	@Override
	public void setCurrentServer(String serverName)
	{
		_frame.setTitle(serverName);
	}

	@Override
	public void destroy()
	{
		_frame.dispose();
	}
}
