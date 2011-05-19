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

		// bonuses for rowCompletion
		int rowsCompleted = 0;
		for (int i = 0; i < g.length; i++)
			if (rowComplete(g, i))
			{
				rowsCompleted++;
				//sum += (g.length - i) * (g.length - i) * numBlocks;
			}
		
		sum += rowsCompleted * rowsCompleted * numBlocks * numBlocks;
		
		for (Location loc : curLocs)
		{
			int i = loc.row();
			int scale = //5 - rowsCompleted;
			avgHeight(g) - rowsCompleted;
			
			sum += (i-scale)*(i-scale)*(i-scale);
		}
		
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
						penalty++;
						//penalty2++;
						//gotCavern = true;
					}
					continue;
				}
			}
		}
		sum -= 4000 * penalty;
		
		if (penalty2 >= 2)
			sum -= 4000 * penalty2 * penalty2;
		
		return sum;
	}
}
