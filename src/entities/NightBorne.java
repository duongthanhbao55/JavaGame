package entities;

import static untilz.Constants.Directions.*;

import static untilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Effect.Animation;
import Load.CacheDataLoader;
import database.ItemManager;
import gamestates.Playing;
import objects.Item;
import main.Game;

public class NightBorne extends Enemy {

	ArrayList<Animation> NightborneAnimList = new ArrayList<>();

	// AttackBox
	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX;
	private boolean hurt, dead = false;
	private static boolean isExtermination = false;
	private static int deadCount = 0;

	public NightBorne(float x, float y,int currHealth,boolean active, Playing playing) {
		super(x, y, NIGHTBORNE_SIZE, NIGHTBORNE_SIZE, NIGHTBORNE, playing);
		enemyId = 0;
		this.active = active;
		this.currHealth = currHealth;
		this.refreshTime = 20000000000L;
		this.firstCheck = true;
		initHitbox(20, 30);
		initAttackBox();
		LoadEnemyAnim();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (35 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 4);
	}

	public void reSpawn(long currTime) {
		if (!active) {
			if (currTime - previousTime > refreshTime) {
				if (!firstCheck) {
					this.resetEnemy();
					active = true;		
					firstCheck = true;	
				}
				firstCheck = false;	
				previousTime = currTime;
			}
		}
	}

	private void updateBehavior(int[][] lvlData, Player player) {

		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir) {
			updateInAir(lvlData);
		} else {

			switch (this.state) {
			case IDLE:
				newState(RUNNING, NightborneAnimList.get(this.state));
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, player)) {
					turnTowardPlayer(player);
					if (isPlayerCloseForAttack(player) && this.state != HURT)
						newState(ATTACK, NightborneAnimList.get(this.state));
				}

				move(lvlData);
				break;
			case ATTACK:
				if (NightborneAnimList.get(ATTACK).getCurrentFrame() == 0)
					attackChecked = false;

				if (NightborneAnimList.get(ATTACK).getCurrentFrame() == 10 && !attackChecked) {
					checkPlayerHit(attackBox, player);
				}

				break;
			case HURT:
				
				break;
			case DEAD:

				break;

			}
		}
		// System.out.println(NightBorne.isExtermination);
//		if(NightBorne.isExtermination) {
//			deadCount();
//		}

	}

	private void initHitbox() {
		// TODO Auto-generated method stub

	}

	private void LoadEnemyAnim() {
		NightborneAnimList.add(CacheDataLoader.getInstance().getAnimation("NBidle"));
		NightborneAnimList.add(CacheDataLoader.getInstance().getAnimation("NBrun"));
		NightborneAnimList.add(CacheDataLoader.getInstance().getAnimation("NBattack"));
		NightborneAnimList.add(CacheDataLoader.getInstance().getAnimation("NBhurt"));
		NightborneAnimList.add(CacheDataLoader.getInstance().getAnimation("NBdeath"));
	}

	public void update(long currTime, int[][] lvlData, Player player) {
		NightborneAnimList.get(this.state).Update(currTime);// update animation hurt before update another animation so
															// change this line to first in update
		super.update(lvlData, NightborneAnimList.get(this.state));
		updateBehavior(lvlData, player);
		updateAttackBox();
	}

	public void updateAttackBox() {
		if (walkDir == LEFT) {
			attackBox.x = hitbox.x - attackBox.width - attackBoxOffsetX;
		} else if (walkDir == RIGHT) {
			attackBox.x = hitbox.x + hitbox.width + attackBoxOffsetX;
		}
		attackBox.y = hitbox.y;
	}

	public void render(Graphics g, int xLvlOffset) {
		NightborneAnimList.get(this.state).draw(
				(int) ((getHitbox().getX() - xLvlOffset) - NIGHTBORNE_DRAWOFFSET_X + flipX()),
				(int) (getHitbox().getY() - NIGHTBORNE_DRAWOFFSET_Y), NIGHTBORNE_SIZE * flipW(), NIGHTBORNE_SIZE, g);
		// drawAttackBox(g,xLvlOffset);
		// drawHitbox(g,xLvlOffset);
	}

	public void hurt(int amount) {

		if (!dead) {
			currHealth -= amount;
			if (currHealth <= 0) {
				currHealth = 0;
			} else if (currHealth >= maxHealth) {
				currHealth = maxHealth;
			}
			if (currHealth <= 0) {
				newState(DEAD, NightborneAnimList.get(this.state));
				dead = true;
			} else {
				newState(HURT, NightborneAnimList.get(this.state));
			}
		}

	}

	public int flipX() {
		if (walkDir == RIGHT)
			return 0;
		else
			return width;

	}

	public int flipW() {
		if (walkDir == RIGHT)
			return 1;
		else
			return -1;
	}

	public void setHurt(boolean hurt) {
		this.hurt = hurt;
	}

	public boolean isHurt() {
		return this.hurt;
	}

	public void resetEnemy() {
		super.resetEnemy();
		this.dead = false;
		newState(IDLE, NightborneAnimList.get(this.state));
	}

	public static void setTaskExtermination(boolean isExtermination) {
		if (!isExtermination) {
			NightBorne.deadCount = 0;
		}
		NightBorne.isExtermination = isExtermination;
	}

	public void deadCount() {
		if (NightBorne.isExtermination) {
			if (dead) {
				NightBorne.deadCount++;
			}
		}
	}

	public static boolean getExtermination() {
		return NightBorne.isExtermination;
	}

	public static int getDeadCount() {
		return NightBorne.deadCount;
	}

	public static void setDeadCount(int deadCount) {
		NightBorne.deadCount = deadCount;
	}
}
