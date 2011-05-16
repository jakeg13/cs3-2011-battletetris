/** MyBoundedEnv.java
 * 
 * A container for a 2D grid of Locatables. (In this case, Blocks)
 * 	Locatable[_ROW_][_COL_] theGrid stores all of the Locatables.
 * 	objectCount = number of objects in theGrid. Always updated to remain consistent.
 * 
 * Useful for finding organizing Locatables without conflict.
 * 
 * Note: Usually when displayed, a MyBoundedEnv has row 0 at the top counting up while going towards
 * 		the bottom of the screen.
 * 
 * Constructor:
 * 	MyBoundedEnv(int row, int col): Initialize theGrid to be row x col with objectCount=0.
 * 
 * Accessors:
 * 	int numRows(): # of rows in theGrid
 * 	int numCols(): # of cols in theGrid
 * 	boolean isValid(Location): True if the location given is inside theGrid
 * 	int numObjects(): returns objectCount
 * 	Locatable[] allObjects(): returns every (non-null) Locatable in theGrid
 * 								{uses notNull helper method, which is written poorly}
 * 
 * 	boolean isEmpty(Location): returns if the loc is valid and if no Locatable is there
 * 	Locatable objectAt(Location): returns the Locatable at the location (or null if nothing)
 * 
 * Mutators:
 * 	add(Locatable): attempt to add to theGrid. If successful, objectCount++
 * 	remove(Locatable): attempt to remove from theGrid. If successful, object--
 * 	recordMove(Locatable, Location): empty the old location and add the Locatable back in.
 * 	clearAll(): empty theGrid and reset objectCount. (could alternatively make a new grid instead)
 * 
 * @author AlexFandrianto
 *
 */

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
		for(int i=0;i<a.length;i++)
		{
			if(a[i]==null)
				return i;
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
		if (theGrid[oldLoc.row()][oldLoc.col()] != null)
		{
			theGrid[oldLoc.row()][oldLoc.col()]=null;
			objectCount--;
		}
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
	
	public int[][] intGrid()
	{
		int[][] a = new int[theGrid.length][theGrid[0].length];
		for (int i = 0; i < theGrid.length; i++)
			for (int j = 0; j < theGrid[0].length; j++)
				if (theGrid[i][j] != null)
					a[i][j] = 1;
				else
					a[i][j] = 0;
		return a;
	}
}