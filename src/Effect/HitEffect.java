package Effect;

import java.awt.Graphics;

import Load.CacheDataLoader;

public class HitEffect extends VFX {

	private Animation hitEffect = null;
	private boolean isActive = false;

	public HitEffect(int x, int y, int width, int height) {
		super(x, y, width, height);
		LoadAnim();
	}

	private void LoadAnim() {
		hitEffect = CacheDataLoader.getInstance().getAnimation("hitAttack");
	}

	@Override
	public void update(long currTime) {
		if (isActive) {
			hitEffect.Update(currTime);
			checkEffect();
		}

	}

	@Override
	public void render(Graphics g, int xLvlOffset) {
		if (isActive)
			hitEffect.draw(x - xLvlOffset, y, width, height, g);
	}

	private void checkEffect() {
		if (hitEffect.isLastFrame()) {
			isActive = false;
		}
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Animation getHitEffect() {
		return hitEffect;
	}

	public void setHitEffect(Animation hitEffect) {
		this.hitEffect = hitEffect;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return this.isActive;
	}
}
