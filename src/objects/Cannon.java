package objects;

import java.awt.Graphics;

import Effect.Animation;
import Load.CacheDataLoader;
import main.Game;

import static untilz.Constants.ObjectConstants.CANNON_WIDTH;
import static untilz.Constants.ObjectConstants.CANNON_HEIGHT;
import static untilz.Constants.ObjectConstants.LEFT_CANNON;
import static untilz.Constants.ObjectConstants.RIGHT_CANNON;


public class Cannon extends GameObject{

	Animation cannon;
	private int tileY;
	public Cannon(int x, int y, int objType) {
		super(x, y, objType);		
		initHitbox(40,26);
		tileY = y / Game.TILES_SIZE;
		xDrawOffset = 0;
		yDrawOffset = 0;
		loadAnim();
	}
	public void loadAnim() {
		cannon = CacheDataLoader.getInstance().getAnimation("Cannon");
	}
	public void update(long currTime) {
		if(doAnimation) {
			cannon.Update(currTime);
		}
		
		if(cannon.isLastFrame()) {
			doAnimation = false;
		}
		
		
		//System.out.println(doAnimation);
	}
	public void render(Graphics g, int xLvlOffset) {		
			
			cannon.draw((int)(getHitbox().x - xLvlOffset) - xDrawOffset + flipX(), (int)(getHitbox().y - yDrawOffset), CANNON_WIDTH * flipW(), CANNON_HEIGHT, g);
	}
	public void reset() {
		super.reset();
		cannon.reset();
	}
	public int flipX() {
		if(objType == LEFT_CANNON) 
			return 0;
		else
			return (int)getHitbox().width;
	}
	
	public int flipW() {
		if(objType == LEFT_CANNON)
			return 1;
		else
			return -1;
	}
	public int getTileY() {
		return tileY;
	}
}
