/** TetrisHeuristicAI implements TetrisAI's think() and actuate() methods
 * 
 *  Meant to use the Tetris heuristic from http://www.vidarholen.net/contents/junk/tetris/
 *  
 *  Board Weight = SUM[all spaces with tiles]{tile_y ^ 3 - SUM[all spaces w/o tiles with tile above]}
 * 
 * 	Goal is to maximize board weight when checking all possible orientations of current Tetrad
 * 
 * @author AlexFandrianto
 *
 */

public class TetrisHeuristicAI2 extends  TetrisHeuristicAI {
	
	
	public TetrisHeuristicAI2(Tetris t)
	{
		super(t);
	}
	protected int computeBoardWeight(int[][] g, Location[] curLocs)
	{		
		int penalty = 0; // num holes
		int penalty2 = 0; // num caverns [columns with 3 blanks on top of each other]
							// 1 is okay. 2 is poor. But 3 => lose
		int sum = 0;
		int numBlocks = doubleAdd(g);
		
		for (int j = 0; j < g[0].length; j++)
		{
			boolean gotCavern = false;
			for (int i = 0; i < g.length; i++)
			{
				if (g[i][j] == 0)
				{
					if (blockAbove(g, i, j))
						penalty++;
					else if (insideCavern(g, i, j) && !gotCavern)
					{
						//penalty++;
						penalty2++;
						gotCavern = true;
					}
					continue;
				}
				
				int scale = 5; //numBlocks / g[0].length; // want blocks to be within 3 of avg.
				
				sum += (i-scale)*(i-scale)*(i-scale);
			}
		}
		
		sum -= 4000 * penalty;
		
		if (penalty2 >= 2)
			sum -= 4000 * penalty2 * penalty2;
		
		// bonuses for rowCompletion
		int rowsCompleted = 0;
		for (int i = 0; i < g.length; i++)
			if (rowComplete(g, i))
			{
				rowsCompleted++;
				//sum += (g.length - i) * (g.length - i) * numBlocks;
				for (int j = 0; j < g[0].length; j++)
				{
					Location loc = new Location(i, j);
					
					// If it matches the location of one of our current blocks
					// Then if that block is actually a power up block, give a bonus!
					for (int k = 0; k < curLocs.length; k++)
					{
						if (curLocs[k].equals(loc))
						{
							Block b = curRad.blocks()[k];
							if (b.getPowerType() != PowerUp.POWERUP_NORMAL)
								sum += numBlocks * numBlocks;
						}
					}
				}
			}
		
		sum += rowsCompleted * rowsCompleted * numBlocks * numBlocks;
		
		return sum;
	}
	
	protected boolean insideCavern(int[][] g, int row, int col)
	{
		if (blockAbove(g, row, col))
			return false;// then this is just a hole
		
		for (int i = row - 2; i >= row - 2; i--) // only the 3rd block to the left and right
		{
			for (int j = col-1; j <= col+1; j++)
				if (board.isValid(new Location(i, j)))
				{
					if (j == col && g[i][j] == 1 && !rowComplete(g, i))
						return false; // is not part of a cavern
					if (j != col && g[i][j] == 0 && !blockAbove(g, i, j))
						return false; // is not part of a cavern (unless very unlucky)
				}
				else if (i < 0)
					return false;
		}
		return true;
	}
}
