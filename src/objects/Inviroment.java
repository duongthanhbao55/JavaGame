package objects;

import java.awt.Graphics;

import Effect.Animation;
import Load.CacheDataLoader;
import main.Game;

import static untilz.Constants.ObjectConstants.TORCH1;
import static untilz.Constants.ObjectConstants.TORCH2;

public class Inviroment extends GameObject {

	private Animation torch1, torch2;

	public Inviroment(int x, int y, int objType) {
		super(x, y, objType);
		loadAnim();
	}

	private void loadAnim() {
		torch1 = CacheDataLoader.getInstance().getAnimation("torch-C");
		torch2 = CacheDataLoader.getInstance().getAnimation("torchB");
	}

	public void update(long currTime) {
		if (objType == TORCH1)
			torch1.Update(currTime);
		if (objType == TORCH2)
			torch2.Update(currTime);
	}

	public void render(Graphics g, int xLvlOffset) {
		if (objType == TORCH1)
			torch1.draw(x - xLvlOffset, y, (int) (16 * Game.SCALE), (int) (32 * Game.SCALE), g);
		if (objType == TORCH2)
			torch2.draw(x - xLvlOffset, y, (int) (16 * Game.SCALE), (int) (32 * Game.SCALE), g);
	}
}
