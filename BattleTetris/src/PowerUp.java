import java.awt.Color;


public class PowerUp {
	
	public static final int POWERUP_NORMAL = 0;
	public static final int POWERUP_BOMB = 1;
	public static final int POWERUP_ANTIBOMB = 2;
	public static final int POWERUP_SCRAMBLE = 3;
	public static final int POWERUP_REMOVE = 4;
	
	public static final int NUM_POWERUPS = 4;
	
	private Tetris tetris;
	private MyBoundedEnv env1;
	private MyBoundedEnv env2;
	private int powerUpType;
	
	private int roundsStayed = 0;
	
	public PowerUp(Tetris tet, MyBoundedEnv env1, MyBoundedEnv env2, int type)
	{
		tetris = tet;
		powerUpType = type;
		this.env1 = env1;
		this.env2 = env2;
	}
	public static String getImageName(int powerUp)
	{
		switch(powerUp)
		{
			case POWERUP_NORMAL:
				return "";
			case POWERUP_BOMB:
				return "src/images/Bomb Icon.jpg";
			case POWERUP_ANTIBOMB:
				return "src/images/poof_icon.png";
			case POWERUP_SCRAMBLE:
				return "src/images/icon_scramble.gif";
			case POWERUP_REMOVE:
				return "src/images/Arrow down Icon.jpg";
			default:
				return "";
		}
	}
	public static boolean mineOrOpp(int powerUp)
	{
		return (powerUp == POWERUP_NORMAL
				|| powerUp == POWERUP_BOMB
				|| powerUp == POWERUP_ANTIBOMB
				|| powerUp == POWERUP_REMOVE);
	}
	public boolean expiring()
	{
		if (powerUpType == POWERUP_SCRAMBLE)
			if (roundsStayed == 1)
				return true;
			else
				return false;
		return true;
	}
	public void increaseTime()
	{
		roundsStayed++;
	}
	
	public void changeRad()
	{
		switch(powerUpType)
		{
			case POWERUP_NORMAL:
				break;
			case POWERUP_BOMB:				
			case POWERUP_ANTIBOMB:
				tetris.setNextRad(new Tetrad(env2, powerUpType));
			case POWERUP_SCRAMBLE:
			case POWERUP_REMOVE:
			default:
				break;
		}
	}
	public void beforeAction()
	{
		switch(powerUpType)
		{
			case POWERUP_NORMAL:
				break;
			case POWERUP_BOMB:
				break;
			case POWERUP_ANTIBOMB:
				break;
			case POWERUP_SCRAMBLE:
				scramble();
				break;
			case POWERUP_REMOVE:
				removeRow();
				break;
			default:
				break;
		}
	}
	public void afterAction()
	{
		switch(powerUpType)
		{
			case POWERUP_NORMAL:
				break;
			case POWERUP_BOMB:
				blowUp();
				break;
			case POWERUP_ANTIBOMB:
				blowDown();
				break;
			case POWERUP_SCRAMBLE:
			case POWERUP_REMOVE:
			default:
				break;
		}
	}
	
	
	private void blowUp()
	{
		Block a=tetris.currentRad().blocks()[0];
		Location loc=a.location();
		int row=loc.row();
		int col=loc.col();
		for (int i = row - 1; i <= row+1; i++)
			for (int j = col - 1; j <= col+1; j++)
			{
				Location l = new Location(i, j);
				Locatable lb = env1.objectAt(l);
				if (lb != null)
				{
					env1.remove(lb);
				}
			}
	}
	private void blowDown()
	{
		Block a=tetris.currentRad().blocks()[0];
		Location loc=a.location();
		int row=loc.row();
		int col=loc.col();
		for (int i = row - 1; i <= row+1; i++)
			for (int j = col - 1; j <= col+1; j++)
			{
				Location l = new Location(i, j);
				if (env1.isValid(l) && env1.isEmpty(l))
				{
					Block b=new Block(Color.white);
					b.setLocation(l);
					env1.add(b);
				}
			}
	}
	
	private void scramble()
	{
		for (int i = 2 * env1.numRows() / 3; i < env1.numRows(); i++)
			for (int k = env1.numCols() - 1; k > 0; k--)
			{
				int j = (int)Math.floor(Math.random() * (k+1));
				
				Location loc1 = new Location(i, k);
				Location loc2 = new Location(i, j);
				
				Block l1 = (Block)env1.objectAt(loc1);
				Block l2 = (Block)env1.objectAt(loc2);
				
				if (l1 != null)
					env1.remove(l1);
				if (l2 != null)
					env1.remove(l2);
				
				if (l1 != null)
				{
					l1.setLocation(loc2);
					env1.add(l1);
				}
				if (l2 != null)
				{
					l2.setLocation(loc1);
					env1.add(l2);
				}
			}
	}
	private void removeRow()
	{
		for(int i = env1.numRows() - 1; i >= 0; i--)
			for (int j = 0; j < env1.numCols(); j++)
			{
				Location loc = new Location(i, j);
				Location locNew = new Location(i+1, j);
				Block b = (Block)env1.objectAt(loc);
				
				if (b != null)
				{
					env1.remove(b);
				
					if (env1.isValid(locNew))
					{
						b.setLocation(locNew);
						env1.add(b);
					}
				}
			}
	}
}
