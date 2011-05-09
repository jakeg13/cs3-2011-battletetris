import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Shape
{
	private Block[][] blocks;
	//private Location bottomLeft;
	private MyBoundedEnv env;
	private Color shiny;
	public Shape(int row, int col,Location topLeft,MyBoundedEnv env)
	{
		this.env=env;
		//System.out.println("Row: "+row+" Col: "+col);
		//System.out.println(topLeft);
		//bottomLeft=bl;
		blocks=new Block[row][col];
		for(int i=0;i<row;i++)//get my blocks
			for(int j=0;j<col;j++)
			{
				blocks[i][j]=(Block)env.objectAt(new Location(topLeft.row()+i,topLeft.col()+j));
				blocks[i][j].setShape(this);
				if(blocks[i][j].shine())
					blocks[i][j].flipShine();
			}
		shiny=pickShinyColor();
	}
	public Color pickShinyColor()
	{
		Block square=blocks[0][0];
		int red=square.color().getRed();
		int green=square.color().getGreen();
		int blue=square.color().getBlue();
		if(red==0)
			red=(int)(40*Math.random()+20);
		else if(red==255)
			red-=(int)(40*Math.random()+20);
		if(green<=255-80)
			green=(int)(40*Math.random()+20);
		else if(green==255)
			green-=(int)(40*Math.random()+20);
		if(blue<=255-80)
			blue=(int)(40*Math.random()+20);
		else if(blue==255)
			blue-=(int)(40*Math.random()+20);
		Color rand=new Color(red,green,blue);
		return rand;
	}
	public void setColor(boolean shine)
	{
		if(!shine)
			shiny=color();
		else
			shiny=pickShinyColor();
	}
	public Color shinyColor()
	{
		return shiny;
	}
	public Iterator<Block> getBlocks()
	{
		ArrayList<Block> b=new ArrayList<Block>();
		for(int i=0;i<blocks.length;i++)
			for(int j=0;j<blocks[0].length;j++)
				b.add(blocks[i][j]);
		return b.iterator();
	}
	public Color color()
	{
		return blocks[0][0].color();
	}
	public double shapeMultiplier()
	{
		int size=min(blocks.length,blocks[0].length);
		if(size==2||size==3)
			return 2.0;
		if(size==4)
			return 2.5;
		return 3.5;
	}
	public static double reduce(int num)
	{
		if(num>2)
			return num-1.;
		return num;
	}
	public double power(double a,int b)
	{
		for(int i=1;i<b;i++)
		{
			a*=a;
		}
		return a;
	}
	public int min(int one,int two)
	{
		if(one>two)
			return two;
		return one;
	}
	public boolean isTopLeft(Block a)
	{
		return blocks[0][0]==a;
	}
	public boolean isTopRight(Block a)
	{
		return blocks[0][blocks[0].length-1]==a;
	}
	public boolean isBottomLeft(Block a)
	{
		return blocks[blocks.length-1][0]==a;
	}
	public boolean isBottomRight(Block a)
	{
		return blocks[blocks.length-1][blocks[0].length-1]==a;
	}
	public boolean canMoveDown()//only if all the bottom blocks in a shape can move down, can the whole thing move down.
	{
		for(int i=0;i<blocks[0].length;i++)
		{
			Location from=blocks[blocks.length-1][i].location();
			Location to=new Location(from.row()+1,from.col());
			if(!env.isEmpty(to))
				return false;
		}
		return true;
	}
	public void moveDown()//all move down at once, together.
	{
		if(canMoveDown())
		{
			for(int i=blocks.length-1;i>=0;i--)
				for(int j=0;j<blocks[0].length;j++)
				{
					Location from=blocks[i][j].location();
					Location to=new Location(from.row()+1,from.col());
					env.remove(blocks[i][j]);
					blocks[i][j].setLocation(to);
					env.add(blocks[i][j]);
				}
		}
	}
	public static boolean canFormShape(Block tl, Block bl,Block br,Block tr, MyBoundedEnv env) //criteria for forming a mxn shape
	{
		if(tl==null||bl==null||br==null||tr==null)//none are blank
			return false;
		if((tl.shape()!=null&&!tl.shape().isTopLeft(tl))||(bl.shape()!=null&&!bl.shape().isBottomLeft(bl))||
			(br.shape()!=null&&!br.shape().isBottomRight(br))||(tr.shape()!=null&&!tr.shape().isTopRight(tr)))
				//none are part of shapes yet, or if they are, ARE the corners of the shape
			return false;
		if(tl.color().equals(bl.color())&&br.color().equals(tr.color())&&tl.color().equals(tr.color()))//all same color
		{
			Color a=tl.color();
			for(int i=0;i<bl.location().row()-tl.location().row()+1;i++)
				for(int j=0;j<tr.location().col()-tl.location().col()+1;j++)
				{
					Block check=(Block)env.objectAt(new Location(tl.location().row()+i,tl.location().col()+j));
					if(check==null)
						return false;
					if(!check.color().equals(a))//none are wrong color
						return false;
					if(check.canClear())//none are clear blocks
						return false;
					if(check.timeLeft()!=0)//none are timer blocks
						return false;
					Shape inspect=check.shape();//if part of a shape already, it must be part of one of the shapes of tl, bl, br, or tr
					if(inspect!=null)
					{
						if(inspect!=tl.shape()&&inspect!=bl.shape()&&inspect!=br.shape()&&inspect!=tr.shape())
							return false;
					}
				}
			return true;
		}
		return false;
	}
	/*public void growTaller()
	{
		Location topLeft=blocks[0][0].location();
		int maxUp=0;
		while(env.objectAt(new Location(topLeft.row()-maxUp,topLeft.col()))!=null&&
			(Block)(env.objectAt(new Location(topLeft.row()-maxUp,topLeft.col()))).color().equals(color()))
		{
			maxUp++;
		}
		for(int i=1;i<=maxUp;i++)
		{
			for(int j=0;j<blocks[0].length;j++)
			{
				Block a=(Block)env.objectAt(new Location(topLeft.row()+i,topLeft.col()+j));
				if
			}
		}
	}
	public void growWider()
	{
	}*/

}