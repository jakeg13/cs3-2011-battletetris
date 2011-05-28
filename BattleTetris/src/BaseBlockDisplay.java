import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;


abstract public class BaseBlockDisplay implements BlockDisplay
{
	protected static final int BLOCKWIDTH=25;
	protected static final int BLOCKHEIGHT=25;
	private ArrowListener listener;
	protected JFrame frame;
	
	@Override
	public final void setArrowListener(ArrowListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public final void keyPressed(KeyEvent e)
	{
		if (listener == null)
		{
			return;
		}
		
		try
		{
			Thread.sleep(10);
			switch (e.getKeyCode())
			{
			case KeyEvent.VK_LEFT:
				listener.leftPressed();
				break;
			case KeyEvent.VK_RIGHT:
				listener.rightPressed();
				break;
			case KeyEvent.VK_DOWN:
				listener.downStart();
				break;
			case KeyEvent.VK_COMMA:
				listener.commaPressed();
				break;
			case KeyEvent.VK_UP:
				listener.upPressed();
				break;
			case KeyEvent.VK_PERIOD:
				listener.periodPressed();
				break;
			case KeyEvent.VK_SPACE:
				listener.spacePressed();
				break;
			case KeyEvent.VK_ENTER:
				listener.enterPressed();
				break;
			case KeyEvent.VK_P:
				listener.pPressed();
				break;
			case KeyEvent.VK_Q:
				listener.qPressed();
				break;
			case KeyEvent.VK_W:
				listener.wPressed();
				break;
			case KeyEvent.VK_E:
				listener.ePressed();
				break;
			case KeyEvent.VK_S:
				listener.sStart();
				break;
			case KeyEvent.VK_A:
				listener.aPressed();
				break;
			case KeyEvent.VK_D:
				listener.dPressed();
				break;
			}
		}
		catch(InterruptedException f)
		{
		}
	}

	@Override
	public final void keyReleased(KeyEvent e)
	{
		if (listener == null)
		{
			return;
		}
		
		try
		{
			Thread.sleep(10);
			switch (e.getKeyCode())
			{
			case KeyEvent.VK_DOWN:
				listener.downEnd();
				break;
			case KeyEvent.VK_S:
				listener.sEnd();
				break;
			}
		}
		catch(InterruptedException f)
		{
		}
	}

	@Override
	public final void keyTyped(KeyEvent e)
	{
		// Do nothing
	}

	@Override
	public final void setLocation(int x, int y)
	{
		frame.setLocation(x,y);
	}

	@Override
	public void setTitle(String title)
	{
		frame.setTitle(title);
	}
	
	/**
	 * Calculates the RGB average of two colors
	 * @param a First color
	 * @param b Second color
	 * @return A color with the average of the R, G, and B components of the arguments.
	 */
	protected static Color blendColors(Color a, Color b)
	{
		return new Color(( a.getRed()   + b.getRed()   ) / 2,
		                 ( a.getGreen() + b.getGreen() ) / 2,
		                 ( a.getBlue()  + b.getBlue()  ) / 2);
	}
}