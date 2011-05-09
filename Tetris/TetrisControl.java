import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;

// Used to display the contents of a game board
public class TetrisControl implements ArrowListener
{
	public static int INTERVAL = 50; // ms

	private Tetris player;
	private Tetris opp;
	private boolean enterPressed=false;
	private boolean spacePressed=false;
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
		//player.newTriad();
		//opp.newTriad();
		//player.updateAll();
		//opp.updateAll();


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
		enterPressed=false;
		spacePressed=false;
//		player.update();
//		opp.update();
		/*while(!started&&time>0)
		{
			play.setTitle("TriadTowers Input Module: "+" Character Select "+time/1000);
			int wait=250;
			pause(wait);
			time-=wait;
			if(enterPressed&&spacePressed)
				started=true;
		}*/
		started = true;
		enterPressed=true;
		spacePressed=true;
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
				/*if(startTime!=0&&startTime%60000==0&&startTime<300000)
					System.out.println("\n\n\n*****<<<<<Speeding up!!!>>>>>*****\n\n\n");
				if(startTime==300000)
					System.out.println("\n\n\n*****<<<<<Beware! No more defense!!! Double Time!>>>>>*****\n\n\n");
				if(startTime>=300000)
				{
//					player.setDefense(false);
//					opp.setDefense(false);
				}*/
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
	public static void main(String[] args)
	{
		TetrisControl game=new TetrisControl();
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
		if(!started&&!enterPressed)
		{
/*			oppOfLeft++;
			if(oppOfLeft>=DropPattern.NUMCHARACTERS)
				oppOfLeft=0;
			opp.setCharacterID(oppOfLeft);
			player.setCharacterID(oppOfRight);
			player.update();
			opp.update();*/
		}
		else
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
		enterPressed=true;
		spacePressed=true;
		update();
		player.pPressed();
		opp.pPressed();
	}
	public void spacePressed()
	{
		if(!started)
		{
			spacePressed=true;
			//opp.penalty();
		}
	}
	public void enterPressed()
	{
		if(!started)
		{
			enterPressed=true;
			//player.penalty();
		}
	}
	public void restart()
	{
		started=false;
		player.setOpponent(opp);
		opp.setOpponent(player);
		player.restart();
		opp.restart();
		//player.updateAll();
		//opp.updateAll();
		spacePressed=false;
		enterPressed=false;
		startTime=0;
		//player.setDefense(true);
		//opp.setDefense(true);
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
		if(!started&&!spacePressed)
		{
/*			oppOfRight++;
			if(oppOfRight>=DropPattern.NUMCHARACTERS)
				oppOfRight=0;
			opp.setCharacterID(oppOfLeft);
			player.setCharacterID(oppOfRight);
			player.update();
			opp.update();*/
		}
		else
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
				/*if(startTime>=60000)
					timeWait=INTERVAL/6*5;
				if(startTime>=120000)
					timeWait=INTERVAL/7*5;
				if(startTime>=180000)
					timeWait=INTERVAL/8*5;
				if(startTime>=240000)
					timeWait=INTERVAL/9*5;
				if(startTime>=300000)
					timeWait=INTERVAL/10*5;*/
				player.act();
				opp.act();

				/*if(player.waited()>=RIGHTWAIT)
				{
					player.play(true);
					update();
				}
				if(opp.waited()>=LEFTWAIT)
				{
					opp.play(true);
					update();
				}*/

				if (startTime % (10 * INTERVAL) == 0)
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
//				player.addWait(INTERVAL);//so that the program thinks you've waited longer than you actually have
//				opp.addWait(INTERVAL);//after 150 seconds.
				startTime+=timeWait;
				//System.out.println(startTime);
			}
		}
		catch(InterruptedException e)
		{
			//ignore this blank space
		}
	}

	public void downEnd()
	{
		if(!started&&!enterPressed)
		{
/*			oppOfLeft--;
			if(oppOfLeft<0)
				oppOfLeft=DropPattern.NUMCHARACTERS-1;
			opp.setCharacterID(oppOfLeft);
			player.setCharacterID(oppOfRight);
			player.update();
			opp.update();
*/		}
		else
			if(!paused)
				player.downEnd();
	}

	public void sEnd()
	{
		if(!started&&!spacePressed)
		{
/*			oppOfRight--;
			if(oppOfRight<0)
				oppOfRight=DropPattern.NUMCHARACTERS-1;
			opp.setCharacterID(oppOfLeft);
			player.setCharacterID(oppOfRight);
			player.update();
			opp.update();
*/		}
		else
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

}