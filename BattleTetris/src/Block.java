/** Block.java
 * 
 * A Locatable instance that holds color and location for the relevant Display (BlockDisplay)
 * 	to draw.
 * A block can be a pivot block for a Tetrad, so that it will be drawn differently.
 * A block is also drawn differently if it is part of the currently active Tetrad.
 * 
 * Blocks are usually added to MyBoundedEnv's so that they can be displayed in BlockDisplay
 * 
 * @author AlexFandrianto
 *
 */

import java.awt.*; // for Color

public class Block implements Locatable
{
	private Color color;
	private Location location;
	private boolean active=false;
	private boolean pivot=false;
	
	public static final int POWERUP_NORMAL = 0;
	public static final int POWERUP_BOMB = 1;
	public static final int POWERUP_ANTIBOMB = 2;
	
	
	private int powerup_type = POWERUP_NORMAL;
	
	public Block(Color colorSet)
	{
		color=colorSet;
	}
	public void setPowerType(int type)
	{
		powerup_type = type;
	}
	public int getPowerType()
	{
		return powerup_type;
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
	public void setColor(Color a)
	{
		color=a;
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