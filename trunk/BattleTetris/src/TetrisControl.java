/** TetrisControl.java
 * 
 * TetrisControl sets up two subordinate Tetris instances, setting them as opponents.
 * TetrisControl handles the timing and synchronization of the Tetris games using INTERVAL.
 * 	Every factor * INTERVAL milliseconds, both Tetris instances are asked to 'play()'
 * 	Every INTERVAL milliseconds, the Tetris instances, are asked to 'act()' instead based on
 * 		keyboard input. (Like the (s or) down button being held)
 * 
 *	Upon being created, a TetrisControl calls run():
 *		run(): starts up a game of Battle Tetris. Pieces are setup and game begins paused.
 *			Upon starting, the game loops forever until one side loses.
 *			Each loop calls play()
 *
 *		play(): Pauses for an INTERVAL before calling act() on each Tetris.
 *				After factor (where factor is a constant between 5 to 10) * INTERVAL ms,
 *					play() is called on each Tetris instance in a random (fair) order.
 *				Factor is reduced from 10 to 5 gradually as time passes, to speed up gameplay.
 *
 *	TetrisControl's main:
 *		resets the game every 10 seconds, by pausing and calling restart() and run()
 *
 *		restart(): Clears the Tetris instances of their boards and readies a new round of
 *					Battle Tetris
 * 
 */

import java.awt.*;

// Used to display the contents of a game board
public class TetrisControl implements ArrowListener
{
	public static int INTERVAL = 50; // ms

	private Tetris player;
	private Tetris opp;
	private BlockDisplay play;
	private boolean paused;
	private boolean started=false;
	private int startTime=0;
	public TetrisControl()
	{
		MyBoundedEnv env=new MyBoundedEnv(1,9);
		Block temp=null;
		for(int i=0;i<env.numCols();i++)
		{
			if(i%4==0)
				temp=new Block(Color.red);
			if(i%4==1)
				temp=new Block(Color.green);
			if(i%4==2)
				temp=new Block(Color.blue);
			if(i%4==3)
				temp=new Block(Color.yellow);
			temp.setLocation(new Location(0,i));
			env.add(temp);
		}
		opp=new Tetris(1);
		opp.setLocationEnvTop(50,0);
		opp.setLocationEnvBottom(50,160);
		player=new Tetris(1);
		player.setLocationEnvTop(800,0);
		player.setLocationEnvBottom(800,160);
		player.setOpponent(opp);
		opp.setOpponent(player);


		play=new BlockDisplay(env);
		play.setArrowListener(this);
		play.setLocation(450,100);

		update();
		run();
	}
	public TetrisControl(int b)
	{
		MyBoundedEnv env=new MyBoundedEnv(1,9);
		Block temp=null;
		for(int i=0;i<env.numCols();i++)
		{
			if(i%4==0)
				temp=new Block(Color.red);
			if(i%4==1)
				temp=new Block(Color.green);
			if(i%4==2)
				temp=new Block(Color.blue);
			if(i%4==3)
				temp=new Block(Color.yellow);
			temp.setLocation(new Location(0,i));
			env.add(temp);
		}
		opp=new Tetris(1);
		opp.setLocationEnvTop(50,0);
		opp.setLocationEnvBottom(50,160);
		player=new Tetris(1);
		player.setLocationEnvTop(800,0);
		player.setLocationEnvBottom(800,160);
		player.setOpponent(opp);
		opp.setOpponent(player);
		
		//new TetrisHeuristicAI(opp);
		//new TetrisHeuristicAI(player);


		play=new BlockDisplay(env);
		play.setArrowListener(this);
		play.setLocation(450,100);

		update();
		run();
	}
	public void update()
	{
/*		if(paused)
			play.setTitle("Left: "+player.counter()/TriadTowers.DROPRATIO+" TriadTowers Input Module: "+"Right: "+opp.counter()/TriadTowers.DROPRATIO+" Paused!");
		else
			play.setTitle("Left: "+player.counter()/TriadTowers.DROPRATIO+" TriadTowers Input Module: "+"Right: "+opp.counter()/TriadTowers.DROPRATIO);
*/	}
	
	public static void pause(int wait)
	{
		try
		{
			Thread.sleep(wait);
		}
		catch(InterruptedException e)
		{
		}
	}
	public void run()
	{
		paused=true;
		player.pPressed();
		opp.pPressed();
		started = true;
		startTime=0;
		boolean pNotLost=true;
		boolean oNotLost=true;
		while(pNotLost&&oNotLost)
		{
			play();
			if(paused)
				update();
			else
			{
				started=true;
				
				pNotLost=player.notLost();
				oNotLost=opp.notLost();
//				player.updateAll();
//				opp.updateAll();
			}
		}
		String a;
		if(!pNotLost)
		{
			a="Left Side Player Wins!";
			opp.nullRad();
			player.nullRad();
//			opp.update();
			System.out.println("\n\n"+a);
		}
		else
		{
			a="Right Side Player Wins!";
			player.nullRad();
			opp.nullRad();
//			player.update();
			System.out.println("\n\n"+a);
		}
		play.setTitle("TriadTowers: "+a);
		System.out.println("***Player Statistics***");
//		opp.printStats("Left side player");
//		player.printStats("Right side player");
		System.out.println("Time spent: "+startTime/1000+" seconds");
	}
	public void play()
	{
		try
		{
			if(paused)
			{
				Thread.sleep(1000);
			}
			else
			{
				int timeWait=INTERVAL;
				
				// As time passes, the game goes faster.
				int factor = 10;
				if(startTime>=60000)
					factor=9;
				if(startTime>=120000)
					factor=8;
				if(startTime>=180000)
					factor=7;
				if(startTime>=240000)
					factor=6;
				if(startTime>=300000)
					factor=5;
				
				player.act();
				opp.act();

				// the Tetris board only moves every 'factor' INTERVALs of time.
				if (startTime % (factor * INTERVAL) == 0)
				{
					if(Math.random()>.5)//for fairness
					{
						player.play();
						opp.play();
					}
					else
					{
						opp.play();
						player.play();
					}
				}
				update();

				Thread.sleep(timeWait);
				startTime+=timeWait;
			}
		}
		catch(InterruptedException e)
		{
		}
	}
	
	/* Reset the game boards */
	public void restart()
	{
		started=false;
		player.setOpponent(opp);
		opp.setOpponent(player);
		player.restart();
		opp.restart();
		//player.updateAll();
		//opp.updateAll();
		startTime=0;
		//player.setDefense(true);
		//opp.setDefense(true);
	}
	

	public void leftPressed()
	{
		if(!paused)
			player.leftPressed();
	}
	public void rightPressed()
	{
		if(!paused)
			player.rightPressed();
	}
	public void commaPressed()
	{
		if(!paused)
			player.commaPressed();
	}
	public void periodPressed()
	{
		if(!paused)
			player.periodPressed();
	}
	public void upPressed()
	{
		if(!paused)
				player.upPressed();
	}
	public void downPressed()//unused as of the 3rd update
	{
		if(!paused)
			player.downPressed();
	}
	public void pPressed()
	{
		paused=!paused;
		update();
		player.pPressed();
		opp.pPressed();
	}
	public void spacePressed()
	{
		if(!started)
		{
			//spacePressed=true;
			//opp.penalty();
		}
	}
	public void enterPressed()
	{
		if(!started)
		{
			//enterPressed=true;
			//player.penalty();
		}
	}
	public void qPressed()
	{
		if(!paused)
		opp.commaPressed();
	}
	public void ePressed()
	{
		if(!paused)
		opp.periodPressed();
	}
	public void wPressed()
	{
		if(!paused)
				opp.upPressed();
	}
	public void sPressed()//unused as of the 3rd update
	{
		if(!paused)
			opp.downPressed();
	}
	public void aPressed()
	{
		if(!paused)
		opp.leftPressed();
	}
	public void dPressed()
	{
		if(!paused)
		opp.rightPressed();
	}

	public void downEnd()
	{
		if(!paused)
			player.downEnd();
	}

	public void sEnd()
	{
		if(!paused)
			opp.downEnd();
	}

	public void downStart()
	{
		if(!paused)
			player.downStart();
	}

	public void sStart()
	{
		if(!paused)
			opp.downStart();
	}

	// Create a Tetris Control instance, which calls run();
	// After the game ends, continuously wait 10 seconds before restarting and calling run() again
	public static void main(String[] args)
	{
		TetrisControl game=new TetrisControl(1);
		while(true)
		{
			try
			{
				Thread.sleep(10000);
			}
			catch(Exception e)
			{
			}
			game.restart();
			game.run();
		}
	}
	
}