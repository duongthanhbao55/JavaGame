package Skill;

import static untilz.Constants.Directions.RIGHT;
import static untilz.Constants.Skill.*;

import java.awt.Graphics;

import Effect.Animation;
import Load.CacheDataLoader;
import gamestates.Playing;
import main.Game;

public class WaterSkill extends Skill {

	private Animation startUp1, startUp2;
	private Animation waterStrike, waterSplash;
	private int yOffset;
	private boolean isStartUp1, isStartUp2;
	private boolean isWaterStrike, isWaterSplash;

	protected WaterSkill(int x, int y, int width, int height) {
		super(x, y, width, height);
		isStartUp1 = false;
		isStartUp2 = false;
		isWaterStrike = false;
		isWaterSplash = false;
		yOffset = (int) (32 * Game.SCALE);
		initAnim();
	}

	private void initAnim() {
		startUp1 = CacheDataLoader.getInstance().getAnimation("1WaterStartUp");
		startUp2 = CacheDataLoader.getInstance().getAnimation("2WaterStartUp");
		waterStrike = CacheDataLoader.getInstance().getAnimation("WaterStrike");
		waterSplash = CacheDataLoader.getInstance().getAnimation("WaterSplash");
	}

	private void checkStartUp() {
		if (startUp1.isLastFrame()) {
			isStartUp1 = false;
			startUp1.reset();
			isWaterStrike = true;
			bounds.width = WATER_STRIKE_WIDTH;
			bounds.height = WATER_STRIKE_HEIGHT;
			setAttackBox(bounds.x - bounds.width / 2, bounds.y - bounds.height / 2, bounds.width, bounds.height);
		}
		if (startUp2.isLastFrame()) {
			isStartUp2 = false;
			startUp2.reset();
			isWaterSplash = true;
			bounds.width = WATER_SPLASH_WIDTH;
			bounds.height = WATER_SPLASH_HEIGHT;
			setAttackBox(bounds.x - bounds.width / 2, bounds.y - bounds.height / 2, bounds.width, bounds.height);
		}

	}

	private void checkWaterSkill() {
		if (waterStrike.isLastFrame()) {
			waterStrike.reset();
			isWaterStrike = false;
			setAttackBox(0, 0, 0, 0);
		}
		if (waterSplash.isLastFrame()) {
			waterSplash.reset();
			isWaterSplash = false;
			setAttackBox(0, 0, 0, 0);
		}
	}

	@Override
	public void update(long currTime) {
		if (isStartUp1) {
			startUp1.Update(currTime);
		}
		if (isStartUp2) {
			startUp2.Update(currTime);
		}
		if (isWaterStrike) {
			waterStrike.Update(currTime);
		}
		if (isWaterSplash) {
			waterSplash.Update(currTime);
		}
		checkStartUp();
		checkWaterSkill();
	}

	@Override
	public void render(Graphics g, int xLvlOffset) {
		if (isStartUp1) {
			startUp1.draw((int) (bounds.getX() + flipX()) - xLvlOffset, (int) bounds.getY() + yOffset,
					START_UP1_WIDTH * flipW(), START_UP1_HEIGHT, g);
		}
		if (isStartUp2) {
			startUp2.draw((int) (bounds.getX() + flipX()) - xLvlOffset, (int) bounds.getY() + yOffset,
					START_UP1_WIDTH * flipW(), START_UP1_HEIGHT, g);
		}
		if (isWaterStrike) {
			waterStrike.draw((int) (bounds.getX() + flipX()) - xLvlOffset, (int) bounds.getY(),
					WATER_STRIKE_WIDTH * flipW(), WATER_STRIKE_HEIGHT, g);
		}
		if (isWaterSplash) {
			waterSplash.draw((int) (bounds.getX() + flipX()) - xLvlOffset, (int) bounds.getY(),
					WATER_SPLASH_WIDTH * flipW(), WATER_SPLASH_HEIGHT, g);
		}

		//g.drawRect((int) (attackBox.getX() - xLvlOffset), (int) attackBox.getY(), (int) attackBox.getWidth(),
				//(int) attackBox.getHeight());
	}

	public void Attack(Playing playing) {
		if (waterStrike.getCurrentFrame() == 3 || waterStrike.getCurrentFrame() == 5
				|| waterStrike.getCurrentFrame() == 9 || waterStrike.getCurrentFrame() == 12


				|| waterSplash.getCurrentFrame() == 3 || waterSplash.getCurrentFrame() == 5
				|| waterSplash.getCurrentFrame() == 9) {// UPS_SET 180
			hasDealtDamage = true;
			//playing.getGame().getAudioPlayer().playAttackSound(); sounds attack
		}
		if (hasDealtDamage) {
			playing.checkEnemyHit(attackBox);
			playing.checkObjectHit(attackBox);
			if (isWaterStrike)
				playing.setThrownAway(true);
			else if(isWaterSplash)
				playing.setThrownAway(true);
			hasDealtDamage = false;
		}
	}

	public void setIsStartUp1(boolean isStartUp1) {
		this.isStartUp1 = isStartUp1;
	}

	public void setIsStartUp2(boolean isStartUp2) {
		this.isStartUp2 = isStartUp2;
	}

}
