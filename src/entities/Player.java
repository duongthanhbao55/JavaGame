package entities;

import Effect.Animation;
import Load.CacheDataLoader;
import gamestates.Playing;
import main.Game;
import untilz.LoadSave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static untilz.HelpMethods.*;
import static untilz.Constants.PlayerConstants.*;
import static untilz.Constants.GRAVITY;



public class Player extends Entity {

	// VARIABLE
	private boolean moving = false, isAttacking = false;
	private boolean hasDealtDamage = false;
	private boolean changeFrame = false;
	private boolean left, right, jump;

	private int[][] lvlData;
	private float xDrawOffset = 2 * Game.SCALE;
	private float yDrawOffset = 1 * Game.SCALE;

	// JUMPING / GRAVITY
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;

	// StatusBarUI
	private BufferedImage statusBarImg;
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);// offsetX in status bar
	private int healthBarYStart = (int) (14 * Game.SCALE);// offsetY
	private float lostHealthWidth = healthBarWidth;


	private int healthWidth = healthBarWidth;
	private int damage = DEFAULT_DAMAGE;

	// Attack Box

	private int attackBoxWidth = (int) (20 * Game.SCALE);
	private int attackBoxHeight = (int) (20 * Game.SCALE);

	// Flip
	private int flipX = 0;
	private int flipW = 1;

	private int currFrame = 0;
	private Playing playing;

	ArrayList<Animation> animList = new ArrayList<Animation>();

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height, playing);
		this.playing = playing;
		try {
			CacheDataLoader.getInstance().readXML(CacheDataLoader.PLAYER_FRAME);
			CacheDataLoader.getInstance().LoadXMLAnim(CacheDataLoader.PLAYER_ANIMATION);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.state = IDLE;
		this.maxHealth = 100;
		this.currHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;	
		loadAnim();

		initHitbox(20,28);
		initAttackBox();
	}
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(hitbox.x, hitbox.y, attackBoxWidth, attackBoxHeight);
	}

	private void loadAnim() {
		animList.add(CacheDataLoader.getInstance().getAnimation("idle"));
		animList.add(CacheDataLoader.getInstance().getAnimation("run"));
		animList.add(CacheDataLoader.getInstance().getAnimation("attack1"));
		animList.add(CacheDataLoader.getInstance().getAnimation("jump"));
		animList.add(CacheDataLoader.getInstance().getAnimation("falling"));

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}

	// UPDATE
	public void update(long currTime) {
		if (currHealth <= 0) {
			playing.setGameOver(true);
			return;
		}

		updateAttackBox();
		updateHealthBar();
		updatePos();
		
		if (isAttacking)
			Attack();

		updateAniamtion();
		setAnimation();
		animList.get(this.state).Update(currTime);
		// updateHitbox();
	}

	private void updateAniamtion() {
		if (animList.get(this.state).isLastFrame()) {
			animList.get(this.state).reset();
		}
	}

	private void changeFrame() {
		int preFrame = animList.get(ATTACK_1).getCurrentFrame();
		if (preFrame != currFrame)
			changeFrame = true;
		else
			changeFrame = false;
		currFrame = preFrame;
	}

	private void Attack() {
		if (animList.get(ATTACK_1).isLastFrame()) {
			isAttacking = false;
			return;
		}
		changeFrame();
		if (changeFrame) {
			if (animList.get(ATTACK_1).getCurrentFrame() == 3) {// UPS_SET 180
				hasDealtDamage = true;
			}
			if (hasDealtDamage) {
				playing.checkEnemyHit(attackBox);
				hasDealtDamage = false;
			}
		}

	}

	private void updateAttackBox() {
		if (right) {
			attackBox.x = hitbox.x + hitbox.width;
		} else if (left) {
			attackBox.x = hitbox.x - attackBoxWidth;
		}
		attackBox.y = hitbox.y - hitbox.height / 2 + hitbox.height / 2;// added hitbox.height/2 because when hitboxes
																		// are drawn on the screen not their exact
																		// position
	}

	private void updateHealthBar() {
		
		healthWidth = (int) ((currHealth / (float) maxHealth) * healthBarWidth);	
		if(healthWidth < lostHealthWidth) {
			lostHealthWidth -= 0.3;
		}
	}

	private void updatePos() {
		moving = false;

		if (jump) {
			jump();
		}

		if (!inAir)
			if ((!left && !right) || (left && right))
				return;

		float xSpeed = 0;

		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}

		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (!inAir) {
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;
		}

		if (inAir) {
			if (CanMoveHere((hitbox.x - hitbox.width / 2), (hitbox.y - hitbox.height / 2) + airSpeed, hitbox.width,
					hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;// increase our speed
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOfAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		} else
			updateXPos(xSpeed);

		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;

	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere((hitbox.x - hitbox.width / 2) + xSpeed, (hitbox.y - hitbox.height / 2), hitbox.width,
				hitbox.height, lvlData)) {// x+xSpeed,y+ySpeed is next position of player
			if (!isAttacking)
				hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}

	}

	public void changeHealth(int value) {
		currHealth += value;
		if (currHealth <= 0) {
			currHealth = 0;
		} else if (currHealth >= maxHealth) {
			currHealth = maxHealth;
		}
	}

	private void setAnimation() {
		int startAni = this.state;

		if (moving)
			this.state = RUNNING;
		else
			this.state = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				this.state = JUMP;
			else
				this.state = FALLING;
		}

		if (isAttacking) {
			this.state = ATTACK_1;
		}

		if (startAni != this.state) {
			animList.get(this.state).reset();
		}
	}

	// RENDER
	public void render(Graphics g, int xLvlOffset) {
		animList.get(this.state).draw((int) (hitbox.x - xDrawOffset) - xLvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset), width * flipW, height, g);
		// drawHitbox(g, xLvlOffset);
		// drawAttackBox(g, xLvlOffset);
		renderUI(g);
	}

	private void renderUI(Graphics g) {
		
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.YELLOW);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, (int)lostHealthWidth, healthBarHeight);	
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		
	}

	// FUNCTION

	public void LoadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	// GETTER VS SETTER
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}


	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void setAttack1(boolean isAttacking) {
		this.isAttacking = isAttacking;
	}

	public void setAttackBoxSize(int width, int height) {
		this.attackBox.width = width;
		this.attackBox.height = height;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setAttackBoxPos(int x, int y) {
		this.attackBox.x = x;
		this.attackBox.y = y;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		isAttacking = false;
		moving = false;
		this.state = IDLE;
		currHealth = maxHealth;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		
	}
	
}
