package untilz;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import main.Game;
import objects.Projectiles;

public class HelpMethods {
	private static boolean debug = false;
	 private static final Random rand = new Random();
	
	
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if(!IsSolid(x,y,lvlData))
			if(!IsSolid(x+width, y+height,lvlData))
				if(!IsSolid(x+width,y,lvlData))
					if(!IsSolid(x,y+height,lvlData))
						return true;
		return false; 		
	}
	
	private static boolean IsSolid(float x, float y,int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if(x < 0 || x >= maxWidth )
			return true;
		if(y < 0 || y >= lvlData.length * Game.TILES_SIZE)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		
		return IsTileSolid((int)xIndex,(int)yIndex,lvlData);
	}
	
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[(int)yTile][(int)xTile];
		
		if(value <= 128*120 && value > 0) //LIMTIT TILEID COLLISION 
			return true;		
		return false;
	}
	public static boolean IsProjectileHittingLevel(Projectiles p, int[][] lvlData) {
		return IsSolid(p.getHitbox().x,p.getHitbox().y,lvlData);
	}
	
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox,float xSpeed) {
		int currentTile = (int)((hitbox.x )/ Game.TILES_SIZE);
		
		if(xSpeed > 0) {
			//RIGHT
			int tileXPos = currentTile * Game.TILES_SIZE;
			
			int xOffset = (int)(Game.TILES_SIZE - hitbox.width) + (int)hitbox.width/2;//+ hitbox.width because x,y is center of hitbox
			return tileXPos + xOffset - 1;//RIGHT POSITION
		}else {
			//LEFT
			return currentTile * Game.TILES_SIZE + (int)hitbox.width/2;//LEFT POSITION
		}
	}
	
	public static float GetEntityYPosUnderRoofOfAboveFloor(Rectangle2D.Float hitbox,float airSpeed) {
		int currentTile = (int)((hitbox.y + hitbox.height/2) / Game.TILES_SIZE);//This is because the method calculates the tileYPos by taking the integer part of the current position of the hitbox divided by the TILES_SIZE.
																				//To solve this issue, you can change the way to calculate the tileYPos by taking the integer part of the bottom of the hitbox divided by the TILES_SIZE.
		if(airSpeed > 0) {
			//FALLING - TOUCHING FLOOR
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int)(Game.TILES_SIZE - hitbox.height) + (int)hitbox.height/2;
			return tileYPos + yOffset - 1;
		}else {
			//JUMPING
			return currentTile * Game.TILES_SIZE;
		}
	}
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		//check pixel below bottomleft and bottomright
		if(!IsSolid(hitbox.x - hitbox.width/2, hitbox.y + hitbox.height ,lvlData))
			if(!IsSolid(hitbox.x + hitbox.width - hitbox.width/2, hitbox.y + hitbox.height ,lvlData))
				return false;		
		
		return true;
	}
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed,int[][] lvlData) {
		if(xSpeed > 0)
			return IsSolid((hitbox.x + hitbox.width - hitbox.width/2) + xSpeed, hitbox.y + hitbox.height + 1 ,lvlData);
		return IsSolid((hitbox.x - hitbox.width/2) + xSpeed, hitbox.y + hitbox.height + 1 ,lvlData);
	}
	public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		
		isAllTilesClear(xStart,xEnd,y,lvlData);
		for(int i = 0; i < (xEnd - xStart);i++)
		{
			if(!IsTileSolid(xStart + i, y + 1, lvlData))
				return false;	
		}	
		return true;
	}
	
	public static boolean IsSightClear(int [][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox,int yTile) {
		int firstXTile = (int)((firstHitbox.x)/ Game.TILES_SIZE);
		int secondXTile = (int)((secondHitbox.x) / Game.TILES_SIZE);
		
		if(firstXTile > secondXTile)
			return IsAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);		
		else 
			return IsAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
		
	}
	
	public static boolean CanCannonSeePlayer(int [][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int)((firstHitbox.x)/ Game.TILES_SIZE);
		int secondXTile = (int)((secondHitbox.x) / Game.TILES_SIZE);
		
		if(firstXTile > secondXTile)
			return isAllTilesClear(secondXTile, firstXTile, yTile, lvlData);		
		else 
			return isAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
	}
	
	public static boolean isAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
		for(int i = 0; i < (xEnd - xStart);i++)
			if(IsTileSolid(xStart + i, y, lvlData))	
				return false;	
			return true;	
	}
	
	public static int getCollistionPointX(Rectangle2D.Float hitbox1, Rectangle2D.Float hitbox2) {
		 int x = (int)Math.max(hitbox1.x, hitbox2.x);
        return x;
	}
	public static int getCollistionPointY(Rectangle2D.Float hitbox1, Rectangle2D.Float hitbox2) {
       int y = (int)Math.max(hitbox1.y, hitbox2.y);
       return y;
	}
    public static int nextInt(final int max) {
        return HelpMethods.rand.nextInt(max);
    }
    public static boolean generateWithProbability(int probability) {
    	int randomValue = nextInt(probability);
    	return randomValue < 100;
    }
    public static void setDebug(final boolean v) {
        HelpMethods.debug = v;
    }
    public static String strSQL(final String str) {
        return str.replaceAll("['\"\\\\%]", "\\\\$0");
    }
}
