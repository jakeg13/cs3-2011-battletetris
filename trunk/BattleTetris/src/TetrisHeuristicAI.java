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

public class TetrisHeuristicAI implements TetrisAI {

	private Tetris tetris;
	private MyBoundedEnv board;
	
	private Tetrad curRad = null; // doesn't match tetris.currentRad() if we need to rethink
	private int[] desValues = new int[3]; // for if we've thought. How we want to move the rad
	private int[] curValues = new int[3]; // needs to reach desValues
	
	
	public TetrisHeuristicAI(Tetris t)
	{
		t.setAI(this);
		tetris = t;
		board = tetris.board();
	}

	@Override
	public void think() {
		// TODO Auto-generated method stub
		
		//System.out.println("thinking");
		
		if (curRad == tetris.currentRad())
		{
			tetris.downEnd();
			return;
		}

		curRad = tetris.currentRad();
		
		Tetrad b = tetris.currentRad();
		Location[] retLocs = b.removeBlocks();
		int[][] g = board.intGrid();
		b.addToLocations(retLocs);
		
		
		int bestWeight = -10000000;
		int bestR = 0;
		int bestI = -4;

		for (int r = 0; r <= 3; r++) // number of rotations you can have
		{
			// reset currentLocs to the same as current Tetrad
			Location[] cLocs = new Location[retLocs.length];
			for (int i = 0; i < retLocs.length; i++)
				cLocs[i] = retLocs[i];
			
			for (int turns = 0; turns < r; turns++)
				cLocs = b.turnLocs(true, cLocs, cLocs[0].row(), cLocs[0].col());
			
			
			for (int i = -5; i <= 5; i++) // number of column shifts you can have
			{
				Location[] currentLocs = new Location[cLocs.length];
				for (int br = 0; br < cLocs.length; br++)
					currentLocs[br] = cLocs[br];
				
				boolean valid = true;
				// shift it accordingly
				for (int j = 0; j < currentLocs.length; j++)
				{
					Location loc = currentLocs[j];
					currentLocs[j] = new Location(loc.row(), loc.col()+i);
					
					if (loc.col()+i >= g[0].length || loc.col()+i < 0)
						valid = false;
				}
				
				if(!valid)
					continue;
				
				// now push down all the way
				boolean down = true;
				int countDown = 0;
				while (down)
				{
					//System.out.println(countDown);
					for (int L = 0; L < currentLocs.length; L++)
					{
						Location loc = currentLocs[L];
						//System.out.println(loc);
						if (loc.row() >= 0 && (loc.row() >= g.length || g[loc.row()][loc.col()] == 1))
						{
							down = false; // shift back up 1 since we went 1 too far
						}
					}
					
					if (down)
					{
						for (int L = 0; L < currentLocs.length; L++)
						{
							Location loc = currentLocs[L];
							currentLocs[L] = new Location(loc.row()+1, loc.col());
						}
						countDown++;
					}
					else
					{
						for (int L = 0; L < currentLocs.length; L++)
						{
							Location loc = currentLocs[L];
							currentLocs[L] = new Location(loc.row() - 1, loc.col());
						}
					}
				}
				
				// You're down all the way now. So how's the board looking?
				for (Location loc : currentLocs)
				{
					if (board.isValid(loc))
						g[loc.row()][loc.col()] = 1; // into board
				}
				
				int weight = computeBoardWeight(g);
				
				if (weight > bestWeight)
				{
					bestWeight = weight;
					bestR = r;
					bestI = i;
				}
				
				for (Location loc : currentLocs)
				{
					if (board.isValid(loc))
						g[loc.row()][loc.col()] = 0; // out of board
				}
				
			}
		}
		
		desValues[0] = bestWeight;
		System.out.println(desValues[0] + " " + desValues[1] + " " + desValues[2]);
		desValues[1] = bestR;
		desValues[2] = bestI;
		curValues = new int[3];
	}
	
	private int doubleAdd(int[][] g)
	{
		int sum = 0;
		for (int i = 0; i < g.length; i++)
			for (int j = 0; j < g[0].length; j++)
				sum += g[i][j];
		return sum;
	}
	private int computeBoardWeight(int[][] g)
	{		
		int penalty = 0; // num holes
		int penalty2 = 0; // num caverns [columns with 3 blanks on top of each other]
							// 1 is okay. 2 is poor. But 3 => lose
		int sum = 0;
		int numBlocks = doubleAdd(g);
		
		for (int i = 0; i < g.length; i++)
			for (int j = 0; j < g[0].length; j++)
			{
				if (g[i][j] == 0)
				{
					if (blockAbove(g, i, j))
						penalty++;
					else if (insideCavern(g, i, j))
						penalty2++;
					continue;
				}
				
				int scale = 0; //numBlocks / g[0].length; // want blocks to be within 3 of avg.
				
				sum += (i-scale)*(i-scale)*(i-scale);
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
			}
		
		sum += rowsCompleted * rowsCompleted * numBlocks;
		
		return sum;
	}
	
	private boolean rowComplete(int[][] g, int row)
	{
		for (int i = 0; i < g[0].length; i++)
			if (g[row][i] == 0)
				return false;
		return true;
	}
	
	private boolean blockAbove(int[][] g, int row, int col)
	{
		for (int i = row; i >= 0; i--)
		{
			if (g[i][col] == 1 && !rowComplete(g, i))
				return true;
		}
		return false;
	}
	
	private boolean insideCavern(int[][] g, int row, int col)
	{
		if (blockAbove(g, row, col))
			return false;// then this is just a hole
		
		for (int i = row; i >= row - 3; i--)
		{
			for (int j = col-1; j <= col+1; j++)
				if (board.isValid(new Location(i, j)))
				{
					if (j == col && g[i][j] == 1 && !rowComplete(g, i))
						return false; // is not part of a cavern
					if (j != col && g[i][j] == 0 && !blockAbove(g, i, j))
						return false; // is not part of a cavern (unless very unlucky)
				}
		}
		
		return true;
	}

	@Override
	public void actuate() {
		// TODO Auto-generated method stub
		if (curValues[1] < desValues[1])
		{
			curRad.rotate();
			curValues[1]++;
			return;
		}
			
		if (curValues[2] < desValues[2])
		{
			curRad.translate(0, 1);
			curValues[2]++;
			return;
		}
		if (curValues[2] > desValues[2])
		{
			curRad.translate(0, -1);
			curValues[2]--;
			return;
		}
		
		tetris.downStart();
	}
	
}
