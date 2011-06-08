/*
 * Programming graphical user interfaces
 * Example: DrawImage.java
 * Jarkko Leponiemi 2003
 */

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class DrawImage extends JPanel {

	private Image[] imgs = null;
	private int current = 0;

	public DrawImage()
	{
		imgs = new Image[PowerUp.NUM_POWERUPS];
		for (int i=1; i <= PowerUp.NUM_POWERUPS; i++)
		{
			Toolkit toolkit = getToolkit();
			// create the image using the toolkit
			Image img = toolkit.createImage(PowerUp.getImageName(i));

			imgs[i-1] = img;
		}
	}
	public void setImage(int pwrType)
	{
		current = pwrType;
	}

	public void paint(Graphics g) {
		super.paint(g);

		if (current != 0)
		{
			// the size of the component
			Dimension d = getSize();
			// the internal margins of the component
			Insets i = getInsets();
			// draw to fill the entire component
			g.drawImage(imgs[current-1], i.left, i.top, d.width - i.left - i.right,
			            d.height - i.top - i.bottom, this );

		}
	}
}