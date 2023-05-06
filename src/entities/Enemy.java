package entities;

import static untilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import Effect.Animation;
import database.ItemManager;
import gamestates.Playing;

import static untilz.Constants.EnemyConstants.*;
import static untilz.Constants.Directions.*;
import static untilz.Constants.GRAVITY;

import main.Game;
import objects.Item;

public abstract class Enemy extends Entity {
	// VARIBALE
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected boolean inAir;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked = false;
	protected int enemyId;
	protected Playing playing;

	public Enemy(float x, float y, int width, int height, int enemyType, Playing playing) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		this.state = IDLE;
		maxHealth = GetMaxHealth(enemyType);
		currHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 0.5f;
		this.playing = playing;
	}

	public void update(int[][] lvlData, Animation anim) {

		if (anim.isLastFrame())
			switch (this.state) {
			case ATTACK:
				this.state = IDLE;
				break;
			case HURT:
				this.state = IDLE;
				break;
			case DEAD:
				if (NightBorne.getExtermination()) {
					NightBorne.setDeadCount(NightBorne.getDeadCount() + 1);
				}
				playing.getItemManager().add(new Item((int) hitbox.getX(), (int) (hitbox.getY() - 5 * Game.SCALE), 0,
						ItemManager.arrItemTemplate[10]));
				active = false;
				break;
			}
	}

	protected void firstUpdateCheck(int[][] lvlData) {

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x - hitbox.width / 2, (hitbox.y - hitbox.height / 2) + airSpeed, hitbox.width,
				hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOfAboveFloor(hitbox, airSpeed);
			tileY = (int) ((hitbox.y) / Game.TILES_SIZE);
		}

	}

	protected void move(int[][] lvlData) {
		float xSpeed = 0;
		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;
		if (CanMoveHere((hitbox.x - hitbox.width / 2) + xSpeed, hitbox.y - hitbox.height / 2, hitbox.width,
				hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();// Come back if meet a hole
	}

	protected void newState(int enemyState, Animation anim) {
		this.state = enemyState;
		anim.reset();
	}

	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.getHitbox()))
			player.changeHealth(-GetEnemyDmg(enemyType));
		attackChecked = true;
	}

	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) ((player.getHitbox().y) / Game.TILES_SIZE);
		if (playerTileY == tileY) {
			if (isPlayerInRanger(player)) {
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}
		}
		return false;
	}

	protected void turnTowardPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}

	protected boolean isPlayerInRanger(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}

	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 2;
	}

	protected void changeWalkDir() {
		if (walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;

	}

	public boolean isActive() {
		return active;
	}

	public int getEnemyId() {
		return this.enemyId;
	}

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currHealth = maxHealth;
		// newState(IDLE);
		active = true;
		airSpeed = 0;
	}

}
