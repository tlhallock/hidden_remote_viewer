package common.intfce;

import java.awt.image.BufferedImage;

public interface ClientDisplay
{
	void setCurrentServer(String serverName);

	void display(BufferedImage screen);

	void leaveCurrentServer();

	void destroy();
}
