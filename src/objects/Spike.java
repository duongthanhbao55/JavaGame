package objects;

import static untilz.Constants.Directions.RIGHT;
import static untilz.Constants.ObjectConstants.SPIKE_HEIGHT;
import static untilz.Constants.ObjectConstants.SPIKE_WIDTH;


import java.awt.Graphics;

import Effect.Animation;
import Load.CacheDataLoader;
import main.Game;

public class Spike extends GameObject{

	Animation spike;
	public Spike(int x, int y, int objType) {
		super(x, y, objType);
		
		initHitbox(32,16);
		xDrawOffset = 0;
		yDrawOffset = 16;
		loadAnim();
	}
	
	public void loadAnim() {
		spike = CacheDataLoader.getInstance().getAnimation("Spike");
	}
	public void update(long currTime) {
		spike.Update(currTime);
		
	}
	public void render(Graphics g, int xLvlOffset) {
		spike.draw((int) ((getHitbox().x - xLvlOffset) - this.xDrawOffset), (int) (getHitbox().y - this.yDrawOffset),
				SPIKE_WIDTH, SPIKE_HEIGHT, g);
		drawHitbox(g, xLvlOffset);
	}

	

}
