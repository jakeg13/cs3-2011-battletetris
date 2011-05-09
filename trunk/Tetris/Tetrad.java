import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Used to display the contents of a game board
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
			return Color.orange;
		if(a==9)
			return Color.white;
		else
			return Color.black;
	}
	private void setLocation(Color a)//1 is the center block
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
		}

		addToLocations(a);
		return false;
	}
	public boolean rotate2()
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
	public boolean moveDown()
	{
		Location[] a=removeBlocks();
		Location[] b=new Location[a.length];
		for(int i=0;i<b.length;i++)
		{
			b[i]=new Location(1+a[i].row(),a[i].col());
		}
		if(areEmpty(b))
		{
			addToLocations(b);
			return true;
		}
		addToLocations(a);
		return false;
	}


}