package objects;

import Effect.Animation;
import Load.CacheDataLoader;
import main.Game;

public class Potion extends GameObject{

	private Animation healPotion, manaPotion;
	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;
		initHitbox(7,14);
		loadAnim();
		xDrawOffset = (int)(3 * Game.SCALE);
		yDrawOffset = (int)(2 * Game.SCALE);
	}
	
	private void loadAnim() {
		healPotion = CacheDataLoader.getInstance().getAnimation("healPotion");
		healPotion = CacheDataLoader.getInstance().getAnimation("manaPotion");
		
	}

	public void update() {
		
	}
	
}
