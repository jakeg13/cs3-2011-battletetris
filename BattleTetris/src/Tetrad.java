/** Tetrad.java
 * 
 * A container for a group of Blocks (usually 4) as well as their MyBoundedEnv container
 * 	Useful for manipulating the set of 4 blocks. (translation, rotation, and generation)
 * 
 * Variables:
 * 	MyBoundedEnv theirEnv: the environment of the blocks being created.
 * 	Block[] blocks: the (usually 4) blocks being manipulated. Exactly 1 is a pivot block.
 * 
 * 
 * Constructor:
 * 	Tetrad(MyBoundedEnv env): Randomly pick a block color (and thus shape). Add the created blocks
 * 		to the MyBoundedEnv. See pickColor() and setLocation(Color)
 * 
 * Private Helper Methods for Construction:
 * 	Color pickColor(): randomly picks one of 7 colors
 * 	setLocation(Color): the chosen color determines 1 of 7 Tetris blocks.
 * 						The 0th index block is set to be the pivot block.
 * 						These are NOT added to the environment.
 * 
 * Accessors:
 * 	Block[] blocks(): get the blocks
 * 	boolean areEmpty(Location[]): checks if the env is empty at every Location in the Location[]
 * 
 * 
 * Mutators:
 * 	Tetrad changeEnv(MyBoundedEnv pass): returns itself with pass as the new MyBoundedEnv
 * 	void activate(): toggles activation of the blocks so that they are displayed differently
 * 					One usually has them be active when this is the current Tetrad in Tetris.
 * 	void addToLocations(Location[] locs): attempt to add the blocks to the env at these locations
 * 	Location[] removeBlocks(): get the Location[] of the blocks, after removing them from the env
 * 	boolean translate(int dRow, int dCol): true if blocks were able to move (dRow, dCol).
 * 										false if the blocks could not move without causing conflict
 * 	boolean moveDown(): is translate(1, 0)
 * 
 * 	boolean rotate(): Attempts to rotate the block clockwise. True if successful
 * 						If this fails, they shift left, right, and down and try rotating again.
 * 						If this succeeds, true, else false.
 * 		Notes:
 * 			To avoid infinite recursion, rotate2 is used as a helper during this 2nd step.
 * 			private Location[] turnLocs is used to predict where the blocks will go when rotating
 * 						about the pivot block.
 * 
 * 
 * @author AlexFandrianto
 *
 */

import java.awt.*;

public class Tetrad
{
	private MyBoundedEnv theirEnv;
	private Block[] blocks;
	public Tetrad(MyBoundedEnv their)
	{
		Color a=pickColor();
		this.setLocation(a);
		theirEnv=their;
	}
	public Tetrad changeEnv(MyBoundedEnv pass)
	{
		theirEnv=pass;
		return this;
	}
	private Color pickColor()
	{
		int a=(int)(Math.random()*7)+1;
		if(a==1)
			return Color.red;
		if(a==2)
			return Color.gray;
		if(a==3)
			return Color.cyan;
		if(a==4)
			return Color.yellow;
		if(a==5)
			return Color.magenta;
		if(a==6)
			return Color.blue;
		if(a==7)
			return Color.green;
		if(a==8)
			return Color.black;
		if(a==9)
			return Color.white;
		else
			return Color.orange;
	}
	private void setLocation(Color a)//#0 is the center block
	{
		if(a==Color.red)//I
		{
			blocks=new Block[4];
		blocks[0]=new Block(a);
		blocks[1]=new Block(a);
		blocks[2]=new Block(a);
		blocks[3]=new Block(a);
			blocks[1].setLocation(new Location(0,4));
			blocks[0].setLocation(new Location(1,4));
			blocks[2].setLocation(new Location(2,4));
			blocks[3].setLocation(new Location(3,4));
		}
		if(a==Color.gray)//T
		{
			blocks=new Block[4];
		blocks[0]=new Block(a);
		blocks[1]=new Block(a);
		blocks[2]=new Block(a);
		blocks[3]=new Block(a);
			blocks[1].setLocation(new Location(0,3));
			blocks[0].setLocation(new Location(0,4));
			blocks[2].setLocation(new Location(0,5));
			blocks[3].setLocation(new Location(1,4));
		}
		if(a==Color.cyan)//O
		{
			blocks=new Block[4];
		blocks[0]=new Block(a);
		blocks[1]=new Block(a);
		blocks[2]=new Block(a);
		blocks[3]=new Block(a);
			blocks[1].setLocation(new Location(0,4));
			blocks[0].setLocation(new Location(1,4));
			blocks[2].setLocation(new Location(0,5));
			blocks[3].setLocation(new Location(1,5));
		}
		if(a==Color.yellow)//L
		{
			blocks=new Block[4];
		blocks[0]=new Block(a);
		blocks[1]=new Block(a);
		blocks[2]=new Block(a);
		blocks[3]=new Block(a);
			blocks[1].setLocation(new Location(0,4));
			blocks[0].setLocation(new Location(1,4));
			blocks[2].setLocation(new Location(2,4));
			blocks[3].setLocation(new Location(2,5));
		}
		if(a==Color.magenta)//J
		{
			blocks=new Block[4];
		blocks[0]=new Block(a);
		blocks[1]=new Block(a);
		blocks[2]=new Block(a);
		blocks[3]=new Block(a);
			blocks[1].setLocation(new Location(0,5));
			blocks[0].setLocation(new Location(1,5));
			blocks[2].setLocation(new Location(2,5));
			blocks[3].setLocation(new Location(2,4));
		}
		if(a==Color.blue)//S
		{
			blocks=new Block[4];
		blocks[0]=new Block(a);
		blocks[1]=new Block(a);
		blocks[2]=new Block(a);
		blocks[3]=new Block(a);
			blocks[1].setLocation(new Location(1,3));
			blocks[0].setLocation(new Location(1,4));
			blocks[2].setLocation(new Location(0,4));
			blocks[3].setLocation(new Location(0,5));
		}
		if(a==Color.green)//Z
		{
			blocks=new Block[4];
		blocks[0]=new Block(a);
		blocks[1]=new Block(a);
		blocks[2]=new Block(a);
		blocks[3]=new Block(a);
			blocks[1].setLocation(new Location(0,3));
			blocks[0].setLocation(new Location(0,4));
			blocks[2].setLocation(new Location(1,4));
			blocks[3].setLocation(new Location(1,5));
		}
		if(a==Color.orange)//Spaced out I
		{
			blocks=new Block[3];
		blocks[0]=new Block(a);
		blocks[1]=new Block(a);
		blocks[2]=new Block(a);
			blocks[1].setLocation(new Location(0,4));
			blocks[0].setLocation(new Location(2,4));
			blocks[2].setLocation(new Location(4,4));
		}
		if(a==Color.white)//Anti-Bomb
		{
			blocks=new Block[1];
		blocks[0]=new Block(a);
			blocks[0].setLocation(new Location(0,4));
		}
		if(a==Color.black)//Bomb
		{
			blocks=new Block[1];
		blocks[0]=new Block(a);
			blocks[0].setLocation(new Location(0,4));
		}

		blocks[0].setPivot();
	}
	public void activate()
	{
		for(Block b : blocks)
			b.activate();
	}
	public Block[] blocks()
	{
		return blocks;
	}
	private void addToLocations(Location[] locs)
	{
		for(int i=0;i<locs.length;i++)
		{
			blocks[i].setLocation(locs[i]);
			theirEnv.add(blocks[i]);
		}
	}
	public Location[] removeBlocks()
	{
		Location[] a=new Location[blocks.length];
		for(int i=0;i<blocks.length;i++)
		{
			theirEnv.remove(blocks[i]);
			a[i]=blocks[i].location();
		}
		return a;
	}
	private boolean areEmpty(Location[] locs)
	{
		boolean a=true;
		for(int i=0;i<locs.length;i++)
		{
			if(!theirEnv.isEmpty(locs[i]))
				a=false;
		}
		return a;
	}
	public boolean translate(int deltaRow, int deltaCol)
	{
		Location[] a=removeBlocks();
		Location[] b=new Location[a.length];
		for(int i=0;i<b.length;i++)
		{
			b[i]=new Location(a[i].row()+deltaRow,a[i].col()+deltaCol);
		}

		if(areEmpty(b))
		{
			addToLocations(b);
			return true;
		}
		addToLocations(a);
		return false;
	}
	public boolean moveDown()
	{
		return translate(1, 0);
	}	
	
	
	// Give whether you're rotating clockwise or not.
	// The locations of the original blocks
	// And the pivot point you want.
	private Location[] turnLocs(boolean rotate,Location[] a, int row0, int col0)
	{
		Location[] b=new Location[a.length];
		if(rotate)//clockwise
		{
			for(int i=0;i<b.length;i++)
			{
				b[i]=new Location(row0-col0+a[i].col(),row0+col0-a[i].row());
			}
			return b;
		}
		else//counterclockwise
		{
			for(int i=0;i<b.length;i++)
			{
				b[i]=new Location(row0-a[i].col()+col0,col0-row0+a[i].row());
			}
			return b;
		}
	}
	public boolean rotate()
	{
		int row0=blocks[0].location().row();
		int col0=blocks[0].location().col();
		Location[] a=removeBlocks();
		Location[] b=turnLocs(true, a, row0, col0); /*new Location[a.length];
		for(int i=0;i<b.length;i++)
		{
			b[i]=new Location(row0-col0+a[i].col(),row0+col0-a[i].row());
		}*/
		if(areEmpty(b))
		{
			addToLocations(b);
			return true;
		}
		else
		{
			addToLocations(a);
			if (translate(0, -1))
				if (rotate2())
					return true;
				else
					translate(0, 1);

			if (translate(0, 1))
				if (rotate2())
					return true;
				else
					translate(0,-1);
			
			if (translate(1, 0))
				if (rotate2())
					return true;
				else
					translate(-1,0);
			
		}

		addToLocations(a);
		return false;
	}
	private boolean rotate2()
	{
		int row0=blocks[0].location().row();
		int col0=blocks[0].location().col();
		Location[] a=removeBlocks();
		Location[] b=turnLocs(true, a, row0, col0); /*new Location[a.length];
		for(int i=0;i<b.length;i++)
		{
			b[i]=new Location(row0-col0+a[i].col(),row0+col0-a[i].row());
		}*/
		if(areEmpty(b))
		{
			addToLocations(b);
			return true;
		}
		addToLocations(a);
		return false;
	}



}