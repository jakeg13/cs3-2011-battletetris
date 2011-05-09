import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

// Used to display the contents of a game board
public class BlockDisplay implements KeyListener
{
	public static int BLOCKWIDTH=25;
	public static int BLOCKHEIGHT=25;

	private MyBoundedEnv board;
	private JPanel[][] grid;
	private JFrame frame;
	private boolean special=false;
	private ArrowListener listener;

	// Constructs a new display for displaying the given board
	public BlockDisplay(MyBoundedEnv board)
	{
		this.board = board;
		grid = new JPanel[board.numRows()][board.numCols()];

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
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
	public BlockDisplay(MyBoundedEnv board, boolean lie)
	{
		special=lie;
		//special=false;
		this.board = board;
		grid = new JPanel[board.numRows()-1][board.numCols()];

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
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

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI()
    {
        if(frame==null)
        {

        //Create and set up the window.
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if(!special)
        	frame.getContentPane().setLayout(new GridLayout(board.numRows(), board.numCols()));
        else
        	frame.getContentPane().setLayout(new GridLayout(board.numRows()-1, board.numCols()));
        frame.addKeyListener(this);

		//Create each square component.
        for (int row = 0; row < grid.length; row++)
        	for (int col = 0; col < grid[row].length; col++)
        	{
				grid[row][col] = new JPanel();
				//grid[row][col].setBackground(Color.WHITE);
				grid[row][col].setPreferredSize(new Dimension(BLOCKWIDTH, BLOCKHEIGHT));
				frame.getContentPane().add(grid[row][col]);
			}
		}

		//Show the board
		showBlocks();

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

	//Redraws the board to include the pieces and border colors.
	public void showBlocks()
	{
		for (int row = 0; row < grid.length; row++)
			for (int col = 0; col < grid[row].length; col++)
			{
				Location loc=new Location(row,col);
				if(special)
					loc = new Location(row+1, col);

				Block square = (Block)board.objectAt(loc);
				if (square == null)
				{
					//grid[row][col].setBackground(Color.WHITE);
					//if(row==0&&col!=TriadTowers.TWOPLAYERCOLS/2+TriadTowers.TWOPLAYERCOLS%2&&special)
					//{
						grid[row][col].setBackground(Color.DARK_GRAY);
						grid[row][col].setForeground(new Color(Color.GRAY.getRed(),Color.GRAY.getGreen(),Color.GRAY.getBlue(),200));
					//}
					//else
					//	grid[row][col].setBackground(Color.WHITE);
					grid[row][col].setBorder(null);
				}
				else
				{
					grid[row][col].setBackground(square.color());

					if(square.active())
					{
						if(square.pivot())
							grid[row][col].setBorder(BorderFactory.createLineBorder(Color.GRAY.brighter(),3));
						else
							grid[row][col].setBorder(BorderFactory.createLineBorder(square.color().darker(),2));
					}
					else
						grid[row][col].setBorder(BorderFactory.createLineBorder(square.color().darker(),3));
				}
			}
	}

	// Sets the title of the window.
	public void setTitle(String title)
	{
		frame.setTitle(title);
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void keyReleased(KeyEvent e)
	{
		try
		{
			Thread.sleep(10);
		if (listener == null)
			return;
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_DOWN)
			listener.downEnd();
		else if (code == KeyEvent.VK_S)
			listener.sEnd();
		}
		catch(InterruptedException f)
		{
		}
	}

	public void keyPressed(KeyEvent e)
	{
		try
		{
			Thread.sleep(10);
		if (listener == null)
			return;
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_LEFT)
			listener.leftPressed();
		else if (code == KeyEvent.VK_RIGHT)
			listener.rightPressed();
		else if (code == KeyEvent.VK_DOWN)
			listener.downStart();
		else if (code == KeyEvent.VK_COMMA)
			listener.commaPressed();
		else if (code == KeyEvent.VK_UP)
			listener.upPressed();
		else if (code == KeyEvent.VK_PERIOD)
			listener.periodPressed();
		else if (code == KeyEvent.VK_SPACE)
			listener.spacePressed();
		else if (code == KeyEvent.VK_ENTER)
			listener.enterPressed();
		else if (code == KeyEvent.VK_P)
			listener.pPressed();
		else if (code == KeyEvent.VK_Q)
			listener.qPressed();
		else if (code == KeyEvent.VK_W)
			listener.wPressed();
		else if (code == KeyEvent.VK_E)
			listener.ePressed();
		else if (code == KeyEvent.VK_S)
			listener.sStart();
		else if (code == KeyEvent.VK_A)
			listener.aPressed();
		else if (code == KeyEvent.VK_D)
		{
			listener.dPressed();
		}
		}
		catch(InterruptedException f)
		{
		}
	}

	public void setArrowListener(ArrowListener listener)
	{
		this.listener = listener;
	}

	public void setLocation(int x, int y)
	{
		frame.setLocation(x,y);
	}
}