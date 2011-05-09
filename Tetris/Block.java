import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Used to display the contents of a game board
public class Block implements Locatable
{
	private Color color;
	private Color trueColor;
	private Location location;
	private int timer;
	private boolean canClear;
	private Shape shape;
	private boolean active=false;
	private boolean pivot=false;
	private boolean shine=false;
	public Block(Color colorSet)
	{
		color=colorSet;
		trueColor=color;
		canClear=false;
		shape=null;
	}
	public boolean shine()
	{
		return shine;
	}
	public void flipShine()
	{
		if(shape!=null)
		{
			shine=!shine;
			Block square=this;
			square.shape().setColor(shine);//for the shiny power gems
		}
	}
	public Shape shape()
	{
		return shape;
	}
	public void setShape(Shape other)
	{
		shape=other;
	}
	public double multiplier()
	{
		if(timer!=0)//apparently clearing timers has 0 effect.
			return 0;
		//if(shape==null)
			return 1./*heightBonus()*/; //height advantage is apparently nonexistent for these blocks
		//return shape.shapeMultiplier()*heightBonus();
	}
	public void activate()
	{
		active=!active;
	}
	public boolean active()
	{
		return active;
	}
	public void setPivot()
	{
		pivot=true;
	}
	public boolean pivot()
	{
		return pivot;
	}
	public Color color()
	{
		return color;
	}
	public Color trueColor()
	{
		return trueColor;
	}
	public boolean canClear()
	{
		return canClear;
	}
	public void setColor(Color a)
	{
		color=a;
	}
	public void setTrueColor(Color a)//diamonds only
	{
		trueColor=a;
	}
	public void darker()
	{
		color=new Color(color.getRed()/2,color.getGreen()/2,color.getBlue()/2);//darker();
		//System.out.println(color()+""+trueColor());
		timer=5;
	}
	public void canClear(boolean canIt)
	{
		canClear=canIt;
	}
	public void time()
	{
		if(timer!=0)
		{
			timer--;
			color=new Color((int)(color.getRed()*1.1),(int)(color.getGreen()*1.1),(int)(color.getBlue()*1.1));
			if(timer==1)
				color=new Color((int)(color.getRed()*1.2),(int)(color.getGreen()*1.2),(int)(color.getBlue()*1.2));
			//System.out.println(color()+""+trueColor());
			if(timer==0)
			{
				color=trueColor;
				//System.out.println("the above converted");
			}
		}
	}
	public int timeLeft()
	{
		return timer;
	}
	public Location location()
	{
		return location;
	}
	public void setLocation(Location newLoc)
	{
		location=newLoc;
	}
	public static void main(String[] args)
	{
		MyBoundedEnv env=new MyBoundedEnv(6,5); //6 rows, 5 cols
		BlockDisplay a=new BlockDisplay(env);
		Locatable obj=new Block(Color.red);
		((Block)obj).setLocation(new Location(5, 0));
		env.add(obj);
		a.showBlocks();
		Locatable obj2=new Block(Color.blue);
		((Block)obj2).setLocation(new Location(3, 3));
		env.add(obj2);
		a.showBlocks();
		Locatable obj3=new Block(Color.green);
		((Block)obj3).setLocation(new Location(2, 4));
		env.add(obj3);
		a.showBlocks();
	}
}