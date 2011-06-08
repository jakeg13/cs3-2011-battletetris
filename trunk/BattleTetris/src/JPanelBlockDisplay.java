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
import javax.swing.*;

// Used to display the contents of a game board
public class JPanelBlockDisplay extends BaseBlockDisplay
{
	public static int BLOCKWIDTH=25;
	public static int BLOCKHEIGHT=25;

	private MyBoundedEnv board;
	private DrawImage[][] grid;
	private boolean special;

	// Constructs a new display for displaying the given board
	public JPanelBlockDisplay(MyBoundedEnv board)
	{
		this(board, false);
	}
	public JPanelBlockDisplay(MyBoundedEnv board, boolean special)
	{
		this.special = special;
		this.board = board;

		frame = new JPanel();

		grid = new DrawImage[board.numRows()][board.numCols()];

		createAndShowGUI();

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
		//Create and set up the window.
		if(!special)
			frame.setLayout(new GridLayout(board.numRows(), board.numCols()));
		else
			frame.setLayout(new GridLayout(board.numRows()-1, board.numCols()));
		frame.addKeyListener(this);

		//Create each square component.
		for (int row = 0; row < grid.length; row++)
		{
			for (int col = 0; col < grid[row].length; col++)
			{
				grid[row][col] = new DrawImage();
				grid[row][col].setPreferredSize(new Dimension(BLOCKWIDTH, BLOCKHEIGHT));
				frame.add(grid[row][col]);
			}
		}

		//Show the board
		showBlocks();

		//Display the window
		frame.setVisible(true);
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
					grid[row][col].setBorder(null);
					grid[row][col].setImage(PowerUp.POWERUP_NORMAL);
				}
				else // the background is the block's color.
				{
					grid[row][col].setBackground(square.color());
					grid[row][col].setImage(square.getPowerType());

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

		frame.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
	}
}