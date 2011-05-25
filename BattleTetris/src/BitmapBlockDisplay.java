import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class BitmapBlockDisplay extends BaseBlockDisplay
{
	private MyBoundedEnv board;
	private JLabel image;
	
	public BitmapBlockDisplay(MyBoundedEnv board)
	{
		this.board = board;
		SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });

		//Wait until display has been drawn
        try
        {
        	while (frame == null || !frame.isVisible())
        		Thread.sleep(1);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	protected void createAndShowGUI() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		image = new JLabel();
		
		frame.getContentPane().add(image);
		
		showBlocks();
		
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void showBlocks() {
		BufferedImage bi = new BufferedImage(board.numCols() * BLOCKWIDTH, board.numRows() * BLOCKHEIGHT,BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = bi.createGraphics();
		graphics.setColor(Color.green);
		graphics.fillRect(0, 0, BLOCKWIDTH, BLOCKHEIGHT);
		graphics.setColor(Color.red);
		graphics.fillRect(50, 75, BLOCKWIDTH, BLOCKHEIGHT);
		graphics.dispose();

		ImageIcon icon = new ImageIcon(bi);
		image.setIcon(icon);
	}

}
