package objects;

import static untilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Graphics;

import Effect.Animation;
import Load.CacheDataLoader;
import main.Game;

public class GameContainer extends GameObject {

	private Animation Box, Barrel;

	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		loadAnim();
		createHitBox();
	}

	private void loadAnim() {
		Box = CacheDataLoader.getInstance().getAnimation("Box");
		Barrel = CacheDataLoader.getInstance().getAnimation("Barrel");
	}

	private void createHitBox() {
		if (objType == BOX) {
			initHitbox(25, 18);
			xDrawOffset = (int) (-1 * Game.SCALE);
			yDrawOffset = (int) (6 * Game.SCALE);
		} else {
			initHitbox(23, 25);
			xDrawOffset = (int) (-1 * Game.SCALE);
			yDrawOffset = (int) (3 * Game.SCALE);
		}

	}

	public void update(long currTime) {
		if (Box.isLastFrame() || Barrel.isLastFrame()) {
			doAnimation = false;
			active = false;
		}

		if (doAnimation) {
			if (this.objType == BOX) {
				Box.Update(currTime);
			} else
				Barrel.Update(currTime);
		}
	}

	public void render(Graphics g, int xLvlOffset) {
		if (this.objType == BOX) {
			Box.draw((int) ((getHitbox().x - xLvlOffset) - this.xDrawOffset), (int) (getHitbox().y - this.yDrawOffset),
					CONTAINER_WIDTH, CONTAINER_HEIGHT, g);
		} else {
			Barrel.draw((int) ((getHitbox().x - xLvlOffset) - this.xDrawOffset),
					(int) (getHitbox().y - this.yDrawOffset), CONTAINER_WIDTH, CONTAINER_HEIGHT, g);
		}

	}

	public void reset() {
		super.reset();
		if (this.objType == BOX) {
			Box.reset();
		} else {
			Barrel.reset();
		}

	}

}
