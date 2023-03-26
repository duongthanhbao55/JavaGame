package Effect;

import java.awt.Graphics;

import Load.CacheDataLoader;

public class HitEffect extends VFX{
	
	private Animation hitEffect = null;
	
	
	public HitEffect(int x, int y, int width, int height) {
		super(x, y, width, height);
		LoadAnim();
	}


	private void LoadAnim() {
		hitEffect = CacheDataLoader.getInstance().getAnimation("hitAttack");
	}
	
	public void update(long currTime) {
		hitEffect.Update(currTime);
	}
	public void render(Graphics g,int xLvlOffset) {
		hitEffect.draw(x - xLvlOffset, y, width, height, g);
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
}
