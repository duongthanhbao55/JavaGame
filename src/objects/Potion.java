package objects;

import java.awt.Graphics;

import static untilz.Constants.ObjectConstants.*;

import Effect.Animation;
import Load.CacheDataLoader;
import main.Game;

public class Potion extends GameObject {

	private Animation healPotion, manaPotion;
	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;

	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		loadAnim();
		doAnimation = true;
		initHitbox(7, 14);

		xDrawOffset = (int) (0 * Game.SCALE);
		yDrawOffset = (int) (2 * Game.SCALE);
		
		maxHoverOffset = (int)(10 * Game.SCALE);
	}

	private void loadAnim() {
			healPotion = CacheDataLoader.getInstance().getAnimation("healPotion");
			manaPotion = CacheDataLoader.getInstance().getAnimation("manaPotion");
	}

	public void update(long currTime) {
		if (this.objType == HEAL_POTION) {
			healPotion.Update(currTime);
		} else
			manaPotion.Update(currTime);
		
		updateHover();
	}

	public void render(Graphics g, int xLvlOffset) {
		if (this.objType == HEAL_POTION) {
			healPotion.draw((int) ((getHitbox().x - xLvlOffset) - this.xDrawOffset),
					(int) (getHitbox().y - this.yDrawOffset), POTION_WIDTH, POTION_HEIGHT, g);
		} else {
			manaPotion.draw((int) ((getHitbox().x - xLvlOffset) - this.xDrawOffset),
					(int) (getHitbox().y - this.yDrawOffset), POTION_WIDTH, POTION_HEIGHT, g);
		}
			
	}
	private void updateHover() {
		hoverOffset += (0.075f * Game.SCALE * hoverDir);
		if(hoverOffset >= maxHoverOffset) {
			hoverDir = -1;
		}else if(hoverOffset < 0) {
			hoverDir = 1;
		}
		
		hitbox.y = y + hoverOffset;	
	}

}
