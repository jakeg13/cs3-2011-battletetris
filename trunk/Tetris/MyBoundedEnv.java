import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Used to display the contents of a game board
public class MyBoundedEnv
{
	private Locatable[][] theGrid;
	private int objectCount;
	public MyBoundedEnv(int row,int col)
	{
		theGrid=new Locatable[row][col];
		objectCount=0;
	}
	public int numRows()
	{
		return theGrid.length;
	}
	public int numCols()
	{
		return theGrid[0].length;
	}
	public boolean isValid(Location loc)
	{
		if(0<=loc.row()&&loc.row()<theGrid.length&&0<=loc.col()&&loc.col()<theGrid[0].length)
			return true;
		return false;
	}
	public int numObjects()
	{
		return objectCount;
	}
	public Locatable[] allObjects()
	{
		Locatable[] a=new Locatable[objectCount];
		for(int row=0;row<theGrid.length;row++)
		{
			for(int col=0;col<theGrid[0].length;col++)
			{
				if(theGrid[row][col]!=null)
					a[notNull(a)]=theGrid[row][col];
			}
		}
		return a;
	}
	private int notNull(Locatable[] a)
	{
		for(int row=0;row<a.length;row++)
		{
			if(a[row]==null)
				return row;
		}
		return -1;
	}
	public boolean isEmpty(Location loc)
	{
		if(isValid(loc))
		{
			if(theGrid[loc.row()][loc.col()]==null)
				return true;
		}
		return false;
	}
	public Locatable objectAt(Location loc)
	{
		if(isValid(loc))
			return theGrid[loc.row()][loc.col()];
		return null;
	}
	public void add(Locatable a)
	{
		int row=a.location().row();
		int col=a.location().col();
		theGrid[row][col]=a;
		objectCount++;
	}
	public void remove(Locatable obj)
	{
		theGrid[obj.location().row()][obj.location().col()]=null;
		objectCount--;
	}
	public void recordMove(Locatable obj,Location oldLoc)
	{
		theGrid[oldLoc.row()][oldLoc.col()]=null;
		objectCount--;
		this.add(obj);
	}
	public void clearAll()
	{
		objectCount=0;
		for(int i=0;i<theGrid.length;i++)
		{
			for(int j=0;j<theGrid[0].length;j++)
			{
				theGrid[i][j]=null;
			}
		}
	}
}