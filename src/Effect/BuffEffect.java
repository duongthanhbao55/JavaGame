package Effect;

import java.awt.Graphics;

import Load.CacheDataLoader;
import main.Game;

import static untilz.Constants.VFX.*;

public class BuffEffect extends VFX {

	private Animation healEffect;
	private Animation levelUpEffect;
	private static boolean isHeal;
	private static boolean isLevelUp;
	private static boolean isAttackBuff;
	private static boolean isArmorBuff;

	public BuffEffect(int x, int y, int width, int height) {
		super(x, y, width, height);
		loadAnim();
	}

	private void loadAnim() {
		healEffect = CacheDataLoader.getInstance().getAnimation("HealEffect");
		levelUpEffect = CacheDataLoader.getInstance().getAnimation("LevelUpEffect");
	}

	@Override
	public void update(long currTime) {	
		if (isHeal)
			healEffect.Update(currTime);
		if(isLevelUp)
			levelUpEffect.Update(currTime);
		checkEffect();
	}

	@Override
	public void render(Graphics g, int xLvlOffset) {
		if (isHeal)
			healEffect.draw(x - xLvlOffset, y, HEAL_EFFECT_SIZE, HEAL_EFFECT_SIZE, g);
		if(isLevelUp)
			levelUpEffect.draw(x - xLvlOffset, y, (int)(48 * Game.SCALE), (int)(48 * Game.SCALE), g);
	}
	
	public static void setIsHeal(boolean isHeal) {
		BuffEffect.isHeal = isHeal;
	}
	public static void setIsLevelUp(boolean isLevelUp) {
		BuffEffect.isLevelUp = isLevelUp;
	}
	public void checkEffect() {
		if(healEffect.isLastFrame()) {
			BuffEffect.isHeal = false;
		}
		if(levelUpEffect.isLastFrame()) {
			BuffEffect.isLevelUp = false;
		}
	}
}
