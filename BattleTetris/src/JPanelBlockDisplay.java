/** JPanelBlockDisplay.java
 * 
 * From the 2D board (MyBoundedEnv), BlockDisplay draws a 2D grid of the Blocks in the game.
 * 	The JFrame holds the JPanel[][] grid for this purpose.
 * 	Implements KeyListener, sending any key events to an ArrowListener (like Tetris/TetrisControl)
 * 
 * We have selected BLOCKWIDTH and BLOCKHEIGHT to be 25, for square 25 pixel blocks.
 * 
 * Variables:
 * 	MyBoundedEnv board: the board whose internal Blocks are accessed for display
 * 	JPanel[][] grid: a 2D array that maps to the board's Locatables.
 * 						Every block is displayed in its own JPanel
 * 
 * 	JFrame frame: Holds all the JPanels. Is the one who sends KeyEvents to BlockDisplay
 * 	boolean special: If this display is meant to hide certain top rows. (Unused currently)
 * 	ArrowListener listener: the listener is asked to perform the relevant action based
 * 							on KeyEvents that BlockDisplay receives
 * 
 * 
 * Constructors:
 * 	JPanelBlockDisplay(MyBoundedEnv board): Displays the given Environment.
 * 										Sets up the JPanel[][] grid and JFrame
 * 										Uses private method createAndShowGUI()
 * 	JPanelBlockDisplay(MyBoundedEnv board, boolean lie): Displays the given Environment
 * 			but hides 1 row if lie = true. (unused currently)
 * 
 * 
 * Update GUI:
 * 	showBlocks(): forces the GUI to redraw the blocks on the screen.
 * 	setTitle(String): changes the title of the JFrame to be this given String
 * 	setLocation(int x, int y): sets the pixelX, pixelY location of the JFrame window on your monitor
 * 
 * KeyListener:
 * 	KeyTyped : ignored
 * 	KeyReleased : used for S and Down currently. Triggers ArrowListener's sEnd and downEnd
 * 	KeyPressed: Used for many keyPressed methods in ArrowListener.
 * 					Instead calls sStart() and downStart() though.
 * 
 * Other Methods:
 * 	setArrowListener(ArrowListener): this listener actuates responses to interpreted KeyEvents.
 * 
 * @author AlexFandrianto
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Used to display the contents of a game board
public class JPanelBlockDisplay implements BlockDisplay
{
	public static int BLOCKWIDTH=25;
	public static int BLOCKHEIGHT=25;

	private MyBoundedEnv board;
	private JPanel[][] grid;
	private JFrame frame;
	private boolean special=false;
	private ArrowListener listener;

	// Constructs a new display for displaying the given board
	public JPanelBlockDisplay(MyBoundedEnv board)
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
	public JPanelBlockDisplay(MyBoundedEnv board, boolean lie)
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
    
    public static Color blendColors(Color a, Color b)
    {
    	return new Color((a.getRed() + b.getRed())/2, (a.getGreen() + b.getGreen())/2, (a.getBlue() + b.getBlue())/2);
    }

	//Redraws the board to include the pieces and border colors.
	public void showBlocks()
	{
		for (int row = 0; row < grid.length; row++)
			for (int col = 0; col < grid[row].length; col++)
			{
				Location loc=new Location(row,col);
				if(special) //hide 1 row
					loc = new Location(row+1, col);

				Block square = (Block)board.objectAt(loc);
				if (square == null) //use a dark gray background if no block is there.
				{
					grid[row][col].setBackground(Color.DARK_GRAY);
					grid[row][col].setForeground(new Color(Color.GRAY.getRed(),Color.GRAY.getGreen(),Color.GRAY.getBlue(),200));
					grid[row][col].setBorder(null);
				}
				else // the background is the block's color.
				{
					grid[row][col].setBackground(square.color());
					
					if (square.getPowerType() == Block.POWERUP_BOMB)
						grid[row][col].setBackground(blendColors(square.color(), Color.BLACK));
					else if (square.getPowerType() == Block.POWERUP_ANTIBOMB)
						grid[row][col].setBackground(blendColors(square.color(), Color.WHITE));

					// the block's border varies depending on if it is active and if it is the pivot
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