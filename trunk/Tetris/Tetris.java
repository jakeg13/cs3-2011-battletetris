import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Used to display the contents of a game board
public class Tetris implements ArrowListener
{
	private MyBoundedEnv env;
	private BlockDisplay display;
	private Tetrad rad;
	private MyBoundedEnv env2;
	private BlockDisplay display2;
	private Tetrad rad2;
	private int score;
	private int level;
	private int rowsMoved;
	private boolean paused;

	private boolean[] arrows; // 0 is up, 1 is left, 2 is right, 3 is down

	public static int GAME_ROWS = 22;
	public static int GAME_COLS = 10;

	private int pendingRows = 0;

	private Tetris opp = null;
	public Tetris()
	{
		arrows = new boolean[4];
		for (int i = 0; i < arrows.length; i++)
			arrows[i] = false;

		env=new MyBoundedEnv(GAME_ROWS,GAME_COLS);
		display=new BlockDisplay(env);
		env2=new MyBoundedEnv(4,GAME_COLS);
		display2=new BlockDisplay(env2);
		display2.setTitle("Tetris! Next Shape");
		score=0;
		level=1;
		display.setTitle("Tetris! Level: "+level+" Score: "+score);
		display.setArrowListener(this);
		rad2=new Tetrad(env2);
		newTetrad();
		display.showBlocks();
		rowsMoved=1;
		paused=true;
		while(notLost())
			play();
		rad=null;
		display.setTitle("Tetris! Level: "+level+" Score: "+score+" Game Over!");
	}
	public Tetris(int runLater)
	{
		arrows = new boolean[4];
		for (int i = 0; i < arrows.length; i++)
			arrows[i] = false;

		env=new MyBoundedEnv(GAME_ROWS,GAME_COLS);
		display=new BlockDisplay(env);
		env2=new MyBoundedEnv(4,GAME_COLS);
		display2=new BlockDisplay(env2);
		display2.setTitle("Tetris! Next Shape");
		score=0;
		level=1;
		display.setTitle("Tetris! Level: "+level+" Score: "+score);
		display.setArrowListener(this);
		rad2=new Tetrad(env2);
		newTetrad();
		display.showBlocks();
		rowsMoved=1;
		paused=false;
		//while(notLost())
		//	play();
		//rad=null;
		//display.setTitle("Tetris! Level: "+level+" Score: "+score+" Game Over!");
	}

	public void setOpponent(Tetris other)
	{
		opp = other;
	}
	public void increasePendingRows(int num)
	{
		pendingRows += num;
	}
	public void resetPendingRows()
	{
		pendingRows = 0;
	}
	public void addPendingRows()
	{
		if (pendingRows == 0)
			return;
		// First move every block on the board up by 'pendingRows'
			// Obviously if some go negative y index you've just got to remove them.
		// Fill the remainder with grayish blocks excluding 1 block per row (at random)

		for (int i = 0; i < env.numRows(); i++)
		{
			for (int j = 0; j < env.numCols(); j++)
			{
				Block a = (Block)env.objectAt(new Location(i, j));
				if (a != null)
				{
					env.remove(a);

					if (i >= pendingRows)
						{
						((Block)a).setLocation(new Location(i-pendingRows,j));
						env.recordMove(a,new Location(i-pendingRows,j));
					}
				}
			}
		}

		for (int i = env.numRows() - pendingRows; i < env.numRows(); i++)
		{
			int exclude = (int)(Math.random()*env.numCols());
			for (int j = 0; j < env.numCols(); j++)
			{
				if (j != exclude)
				{
					Location a = new Location(i, j);
					Block b = new Block(Color.GRAY);
					b.setLocation(a);
					env.add(b);
				}
			}
		}

		// added the rows, so we're done
		resetPendingRows();

	}

	public static void main(String[] args)
	{
		Tetris game=new Tetris();
		System.out.println("Game over!");
	}
	public boolean notLost()
	{
		if(rowsMoved!=-1)
			return true;
		display.setTitle("Tetris! Level: "+level+" Score: "+score+" Game Over!");
		return false;
	}
	public void restart()
	{
		env.clearAll();
		env2.clearAll();
		rad = null;
		rad2=new Tetrad(env2);
		rowsMoved = 0;
		level = 0;
		score = 0;
		newTetrad();
		display.showBlocks();
		display2.showBlocks();
	}

	public boolean newTetrad()
	{
		if (rad != null)
			rad.activate();

		rad=rad2.changeEnv(env);

		rad.activate();

		env2.clearAll();
		display2.showBlocks();
		rad2=new Tetrad(env2);
		Locatable[] b=rad2.blocks();
		for(int i=0;i<b.length;i++)
		{
				env2.add(b[i]);
		}
		display2.showBlocks();
		boolean c=true;
		Locatable[] a=rad.blocks();
		for(int i=0;i<a.length;i++)
		{
			if(env.isEmpty(a[i].location()))
				env.add(a[i]);
			else
				c=false;
		}
		return c;
	}
	public void leftPressed()
	{
		if(rad!=null&&rad.translate(0,-1))
			display.showBlocks();
	}
	public void rightPressed()
	{
		if(rad!=null&&rad.translate(0,1))
			display.showBlocks();
	}
	public void upPressed()
	{
		if(rad!=null&&rad.rotate())
			display.showBlocks();
	}
	public void downEnd()
	{
		arrows[3] = false;
	}
	public void qPressed()
	{
	}
	public void ePressed()
	{
	}
	public void sStart()
	{
	}
	public void sEnd()
	{
	}
	public void downStart()
	{
		/*if(rad!=null)
				{
				if(rad.translate(1,0))
				{
					display.showBlocks();
					rowsMoved++;
					score+=level;
					if(score-(level*level*100)>0)
						level++;
				}
				display.setTitle("Tetris! Level: "+level+" Score: "+score);
		}*/
		arrows[3] = true;
		if (opp == null)
			downPressed();
	}
	public void periodPressed()
	{
	}
	public void commaPressed()
	{
	}
	public void downPressed()
	{
		if(rad!=null)
		{
		if(rad.translate(1,0))
		{
			display.showBlocks();
			rowsMoved++;
			score+=level;
			if(score-(level*level*100)>0)
				level++;
		}
		display.setTitle("Tetris! Level: "+level+" Score: "+score);
		}
	}
	public void pPressed()
	{
		paused=!paused;
		if(paused)
			display.setTitle("Tetris! Level: "+level+" Score: "+score+" Paused!");
		else
			display.setTitle("Tetris! Level: "+level+" Score: "+score+" Resuming...");
	}
	public void spacePressed()
	{
		if(rad.blocks()[0].color()==Color.black)
		{
			blowUp();
			boolean a=newTetrad();
			display.showBlocks();
			display.setTitle("Tetris! Level: "+level+" Score: "+score+" Kaboom!");
			if(!a)
			{
				display.setTitle("Tetris! Level: "+level+" Score: "+score+" Game Over!");
				rowsMoved=-10;
			}
		}
		if(rad.blocks()[0].color()==Color.white)
		{
			blowDown();
			display.showBlocks();
			clearCompletedRows();
			boolean a=newTetrad();
			display.showBlocks();
			display.setTitle("Tetris! Level: "+level+" Score: "+score+" Pop!");
			if(!a)
			{
				display.setTitle("Tetris! Level: "+level+" Score: "+score+" Game Over!");
				rowsMoved=-10;
			}
		}
	}
	public void enterPressed()
	{
	}
	public void wPressed()
	{
	}
	public void sPressed()
	{
	}
	public void aPressed()
	{
	}
	public void dPressed()
	{
	}
	public void nullRad()
	{
		rad=null;
	}
	// moves the arrows
	public void act()
	{
		if (arrows[3])
			downPressed();
	}
	public void play()
	{
		try
		{
			if (opp == null)
			{
			Thread.sleep(1000-level*10);
			while(paused)
				Thread.sleep(1000);//1000 milliseconds
			}
			display.showBlocks();
			if(rad.moveDown())
			{
				display.showBlocks();
				rowsMoved++;
			}
			else
			{
				if(rad.blocks().length==1)
				{
					if(rad.blocks()[0].color()==Color.black)
					{
						blowUp();
					}
					if(rad.blocks()[0].color()==Color.white)
					{
						blowDown();
						display.showBlocks();
						clearCompletedRows();
					}
				}
				else
				{
					clearCompletedRows();
				}
				display.showBlocks();


				addPendingRows();

				//if(rowsMoved!=0)
					rowsMoved=0;
				//else
				//	rowsMoved=-1;
				boolean a=newTetrad();
				if(!a)
				{
					display.setTitle("Tetris! Level: "+level+" Score: "+score+" Game Over!");
					rowsMoved=-1;
				}
				display.showBlocks();
				score+=5*level;
				if(score-(level*level*1000)>0)
					level++;
				display.setTitle("Tetris! Level: "+level+" Score: "+score);
/*				if(rad.moveDown())
				{
					display.showBlocks();
					rowsMoved++;
				}*/

			}
		}
		catch(InterruptedException e)
		{
			//ignore this blank space
		}
	}
	private void blowUp()
	{
		Block a=rad.blocks()[0];
		Location loc=a.location();
		int row=loc.row();
		int col=loc.col();
		Locatable b=env.objectAt(new Location(row+1,col));
		Locatable c=env.objectAt(new Location(row-1,col));
		Locatable d=env.objectAt(new Location(row,col+1));
		Locatable e=env.objectAt(new Location(row,col-1));
		Locatable f=env.objectAt(new Location(row+1,col+1));
		Locatable g=env.objectAt(new Location(row-1,col-1));
		Locatable h=env.objectAt(new Location(row+1,col-1));
		Locatable i=env.objectAt(new Location(row-1,col+1));
		if(b!=null)
		{
			env.remove(b);
			score+=level*5;
		}
		if(c!=null)
		{
			env.remove(c);
			score+=level*5;
		}
		if(d!=null)
		{
			env.remove(d);
			score+=level*5;
		}
		if(e!=null)
		{
			env.remove(e);
			score+=level*5;
		}
		if(f!=null)
		{
			env.remove(f);
			score+=level*5;
		}
		if(g!=null)
		{
			env.remove(g);
			score+=level*5;
		}
		if(h!=null)
		{
			env.remove(h);
			score+=level*5;
		}
		if(i!=null)
		{
			env.remove(i);
			score+=level*5;
		}
		env.remove(a);
		score+=level*5;
		display.setTitle("Tetris! Level: "+level+" Score: "+score);
	}
	private void blowDown()
	{
		Block a=rad.blocks()[0];
		Location loc=a.location();
		int row=loc.row();
		int col=loc.col();
		if(env.isEmpty(new Location(row+1,col)))
		{
			if(Math.random()<.3)
			{
				Block b=new Block(Color.white);
				b.setLocation(new Location(row+1,col));
				env.add(b);
				score+=level*5;
			}
		}
		if(env.isEmpty(new Location(row-1,col)))
		{
			if(Math.random()<.3)
			{
				Block b=new Block(Color.white);
				b.setLocation(new Location(row-1,col));
				env.add(b);
				score+=level*5;
			}
		}
		if(env.isEmpty(new Location(row,col+1)))
		{
			if(Math.random()<.3)
			{
				Block b=new Block(Color.white);
				b.setLocation(new Location(row,col+1));
				env.add(b);
				score+=level*5;
			}
		}
		if(env.isEmpty(new Location(row,col-1)))
		{
			if(Math.random()<.3)
			{
				Block b=new Block(Color.white);
				b.setLocation(new Location(row,col-1));
				env.add(b);
				score+=level*5;
			}
		}
		if(env.isEmpty(new Location(row+1,col+1)))
		{
			if(Math.random()<.3)
			{
				Block b=new Block(Color.white);
				b.setLocation(new Location(row+1,col+1));
				env.add(b);
				score+=level*5;
			}
		}
		if(env.isEmpty(new Location(row+1,col-1)))
		{
			if(Math.random()<.3)
			{
				Block b=new Block(Color.white);
				b.setLocation(new Location(row+1,col-1));
				env.add(b);
				score+=level*5;
			}
		}
		if(env.isEmpty(new Location(row-1,col+1)))
		{
			if(Math.random()<.3)
			{
				Block b=new Block(Color.white);
				b.setLocation(new Location(row-1,col+1));
				env.add(b);
				score+=level*5;
			}
		}
		if(env.isEmpty(new Location(row-1,col-1)))
		{
			if(Math.random()<.3)
			{
				Block b=new Block(Color.white);
				b.setLocation(new Location(row-1,col-1));
				env.add(b);
				score+=level*5;
			}
		}
		display.showBlocks();
		display.setTitle("Tetris! Level: "+level+" Score: "+score);
	}
	private boolean isCompletedRow(int row)
	{
		int a=0;
		int col=env.numCols();
		for(int j=0;j<col;j++)
		{
			if(!env.isEmpty(new Location(row,j)))
				a++;
		}
		if(a==col)
			return true;
		return false;
	}
	private void clearRow(int row)
	{
		for(int i=0;i<env.numCols();i++)
		{
			env.remove(env.objectAt(new Location(row,i)));
		}
		for(int row2=row;row2>=0;row2--)
		{
			for(int i=0;i<env.numCols();i++)
			{
				Locatable a=env.objectAt(new Location(row2,i));
				if(a!=null)
				{
					env.remove(a);
					((Block)a).setLocation(new Location(row2+1,i));
					env.recordMove(a,new Location(row2+1,i));
				}
			}
		}
	}
	public void clearCompletedRows()
	{
		int a=0;
		for(int i=0;i<env.numRows();i++)
		{
			if(isCompletedRow(i))
			{
				clearRow(i);
				a++;
			}
		}
		if(a==1)
			score+=40*level;
		if(a==2)
			score+=100*level;
		if(a==3)
			score+=300*level;
		if(a==4)
			score+=1200*level;
		if(score-(level*level*100)>0)
			level++;
		display.setTitle("Tetris! Level: "+level+" Score: "+score);

		// Penalize opponent!
		if (opp != null && a > 1)
			opp.increasePendingRows(a - 1);
	}

	public void setLocationEnvTop(int x,int y)
	{
		display2.setLocation(x,y);
	}
	public void setLocationEnvBottom(int x,int y)
	{
		display.setLocation(x,y);
	}

}