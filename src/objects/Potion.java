package objects;

import java.awt.Graphics;

import static untilz.Constants.ObjectConstants.*;

import Effect.Animation;
import Load.CacheDataLoader;
import main.Game;

public class Potion extends GameObject {

	private Animation healPotion, manaPotion;

	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		loadAnim();
		doAnimation = true;
		initHitbox(7, 14);

		xDrawOffset = (int) (3 * Game.SCALE);
		yDrawOffset = (int) (2 * Game.SCALE);
		System.out.println(objType);
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
	}

	public void render(Graphics g, int xLvlOffset) {
		if (this.objType == HEAL_POTION) {
			healPotion.draw((int) ((getHitbox().x - xLvlOffset) - this.xDrawOffset),
					(int) (getHitbox().y - this.yDrawOffset), POTION_WIDTH, POTION_HEIGHT, g);
		} else
			manaPotion.draw((int) ((getHitbox().x - xLvlOffset) - this.xDrawOffset),
					(int) (getHitbox().y - this.yDrawOffset), POTION_WIDTH, POTION_HEIGHT, g);
	}

}
