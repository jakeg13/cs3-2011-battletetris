import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BitmapBlockDisplay extends BaseBlockDisplay
{
	private MyBoundedEnv board;
	private JLabel image;
	private Random rng;
	
	public BitmapBlockDisplay(MyBoundedEnv board)
	{
		rng = new Random();
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
		frame = new JPanel();//new JFrame();
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		image = new JLabel();
		
		frame.add(image);
		
		showBlocks();
		
		//frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void showBlocks() {
		int width = board.numCols() * BLOCKWIDTH + 2;
		int height = board.numRows() * BLOCKHEIGHT + 2;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = bi.createGraphics();
		graphics.setColor(Color.gray);
		graphics.drawRect(0, 0, width - 1, height - 1);
		
		//System.out.println(board.numRows());
		//System.out.println(board.numCols());
		
		
		for (int i = 0; i < 100; i++)
		{
			graphics.setColor(Color.getHSBColor(rng.nextFloat(), rng.nextFloat(), rng.nextFloat()));
			graphics.drawRect(rng.nextInt(width), rng.nextInt(height), 1, 1);
		}
		for (int row = 0; row < board.numRows(); row++)
		{
			for (int col = 0; col < board.numCols(); col++)
			{
				Block block = (Block)board.objectAt(new Location(row, col));
				if (block == null)
				{
					continue;
				}
				
				int x = 1 + col * BLOCKWIDTH;
				int y = 1 + row * BLOCKHEIGHT;
				
				// Set base color
				switch (block.getPowerType())
				{
				case PowerUp.POWERUP_BOMB:
					graphics.setColor(blendColors(block.color(), Color.black));
					break;
				case PowerUp.POWERUP_ANTIBOMB:
					graphics.setColor(blendColors(block.color(), Color.white));
					break;
				case PowerUp.POWERUP_SCRAMBLE:
					graphics.setColor(Color.orange);
					break;
				case PowerUp.POWERUP_REMOVE:
					graphics.setColor(new Color(80, 0, 0));
					break;
				default:
					graphics.setColor(block.color());
				}
				// Draw base block
				graphics.fillRect(x, y, BLOCKWIDTH, BLOCKWIDTH);
			}
		}
		
		graphics.dispose();

		ImageIcon icon = new ImageIcon(bi);
		image.setIcon(icon);
		image.validate();
	}

}
