/*
 * Programming graphical user interfaces
 * Example: DrawImage.java
 * Jarkko Leponiemi 2003
 */

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class DrawImage extends JPanel {

	private int current = 0;

	public DrawImage() { }
	
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
			g.drawImage(ContentManager.getImage(current),
			            i.left, i.top,
			            d.width - i.left - i.right, d.height - i.top - i.bottom,
			            this );
		}
	}
}