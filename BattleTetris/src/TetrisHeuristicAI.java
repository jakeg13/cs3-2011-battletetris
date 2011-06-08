import java.awt.Color;

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

	protected Tetris tetris;
	protected MyBoundedEnv board;
	
	protected Tetrad curRad = null; // doesn't match tetris.currentRad() if we need to rethink
	protected int[] desValues = new int[3]; // for if we've thought. How we want to move the rad
	protected int[] curValues = new int[3]; // needs to reach desValues
	
	
	public TetrisHeuristicAI(Tetris t)
	{
		t.setAI(this);
		tetris = t;
		board = tetris.board();
	}

	@Override
	public void think() {
		// TODO Auto-generated method stub
		
		////System.out.println("thinking");
		
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
					////System.out.println(countDown);
					for (int L = 0; L < currentLocs.length; L++)
					{
						Location loc = currentLocs[L];
						////System.out.println(loc);
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
				
				boolean addedToBoard = true;
				// You're down all the way now. So how's the board looking?
				for (Location loc : currentLocs)
				{
					if (board.isValid(loc))
						g[loc.row()][loc.col()] = 1; // into board
					else
						addedToBoard = false;
				}
				if (!addedToBoard)
				{
					for (Location loc : currentLocs)
					{
						if (board.isValid(loc))
							g[loc.row()][loc.col()] = 0; // out of board
					}
					continue;
				}
				
				int[][] gflip = new int[g.length][g[0].length];
				if (curRad.blocks()[0].color() == Color.white)
				{
					for (int rx = currentLocs[0].row()-1; rx <= currentLocs[0].row()+1; rx++)
						for (int j = currentLocs[0].col()-1; j<= currentLocs[0].col()+1; j++)
							if (board.isValid(new Location(rx, j)) && g[rx][j] == 0 && rx != j)
							{
								g[rx][j] = 1;
								gflip[rx][j] = -1;
							}
				}
				else if (curRad.blocks()[0].color() == Color.black)
				{
					for (int rx = currentLocs[0].row()-1; rx <= currentLocs[0].row()+1; rx++)
						for (int j = currentLocs[0].col()-1; j<= currentLocs[0].col()+1; j++)
							if (board.isValid(new Location(rx, j)) && g[rx][j] == 1)
							{
								g[rx][j] = 0;
								gflip[rx][j] = 1;
							}
				}
				
				int weight = computeBoardWeight(g, currentLocs);
				//System.out.println("Candidates: " + weight + " " + r + " " + i + " " + avgHeight(g));
				
				for (int rx = 0; rx < g.length; rx++)
					for (int j = 0; j < g[0].length; j++)
						g[rx][j] += gflip[rx][j];
				
				
				if (weight > bestWeight)
				{
					bestWeight = weight;
					bestR = r;
					bestI = i;
				}
				
				for (Location loc : currentLocs)
				{
					g[loc.row()][loc.col()] = 0; // out of board
				}
				
			}
		}
		
		desValues[0] = bestWeight;
		desValues[1] = bestR;
		desValues[2] = bestI;
		//System.out.println(desValues[0] + " " + desValues[1] + " " + desValues[2]);
		curValues = new int[3];
	}
	
	protected int doubleAdd(int[][] g)
	{
		int sum = 0;
		for (int i = 0; i < g.length; i++)
			for (int j = 0; j < g[0].length; j++)
				sum += g[i][j];
		return sum;
	}
	protected int avgHeight(int[][] g)
	{
		int[] heights = new int[g[0].length];
		for (int j = 0; j < g[0].length; j++)
		{
			boolean seen = false;
			for (int i = 0; i < g.length; i++)
			{
				if (!seen && g[i][j] != 0)
				{
					heights[j] = g.length - i;
					seen = true;
					
				}
			}
		}
		
		int sum = 0;
		for (int a : heights)
			sum += a;
		
		return sum / g[0].length;
	}
	
	protected int computeBoardWeight(int[][] g, Location[] curLocs)
	{		
		int penalty = 0; // num holes
		int penalty2 = 0; // num caverns [columns with 3 blanks on top of each other]
							// 1 is okay. 2 is poor. But 3 => lose
		int sum = 0;
		int numBlocks = doubleAdd(g);
		
		int[] rowBlockCount = new int[g.length];
		for (int i = 0; i < g.length; i++)
			for (int j = 0; j < g[0].length; j++)
				if (g[i][j] == 1)
					rowBlockCount[i]++;
		
		int[] seenBlockInCol = new int[g[0].length];
		
		int[] gotCavern = new int[g[0].length];
		for (int i = 0; i < g.length; i++)
		{
			for (int j = 0; j < g[0].length; j++)
			{
				if (g[i][j] == 0)
				{
					if (blockAbove(g, i, j, seenBlockInCol))
						penalty++;
					else if (insideCavern(g, i, j, seenBlockInCol, rowBlockCount) && gotCavern[j] != 1)
					{
						//penalty2++;
						//gotCavern[j] = 1;
						penalty++;
					}
					continue;
				}
				else if (!rowComplete(g, i, rowBlockCount))
					seenBlockInCol[j] = 1;
				
				int scale = 0;//5; //numBlocks / g[0].length; // want blocks to be within 3 of avg.
				
				sum += (i-scale)*(i-scale)*(i-scale);
			}
		}
		
		sum -= 4000 * penalty;
		
		if (penalty2 >= 2)
			sum -= 4000 * penalty2 * penalty2;
		
		// bonuses for rowCompletion
		int rowsCompleted = 0;
		for (int i = 0; i < g.length; i++)
			if (rowComplete(g, i, rowBlockCount))
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
		
		sum += rowsCompleted * rowsCompleted * 3000; // note that you get less than if you make a hole
		
		return sum;
	}
	
	protected boolean rowComplete(int[][] g, int row, int[] rowBlockCount)
	{
		return rowBlockCount[row] == g[0].length;
	}
	
	protected boolean blockAbove(int[][] g, int row, int col, int[] seenBlockInCol)
	{
		return seenBlockInCol[col] == 1;
	}
	
	protected boolean insideCavern(int[][] g, int row, int col, int[] seenBlockInCol, int[] rowBlockCount)
	{
		if (blockAbove(g, row, col, seenBlockInCol))
			return false;// then this is just a hole
		
		int i = row - 3; // only the 3rd block to the left and right
		for (int j = col-1; j <= col+1; j++)
			if (board.isValid(new Location(i, j)))
			{
				if (j == col && g[i][j] == 1 && !rowComplete(g, i, rowBlockCount))
					return false; // is not part of a cavern
				if (j != col && g[i][j] == 0 && !blockAbove(g, i, j, seenBlockInCol))
					return false; // is not part of a cavern (unless very unlucky)
			}
			else if (i < 0)
				return false;
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
