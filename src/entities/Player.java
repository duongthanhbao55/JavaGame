package entities;

import Effect.Animation;
import Effect.BuffEffect;
import Load.CacheDataLoader;
import Map.PhysicalMap;
import Task.Task;
import audio.AudioPlayer;
import database.MySQL;
import database.User;
import gamestates.Playing;
import main.Game;
import objects.Equipment;
import objects.InventoryManager;
import untilz.LoadSave;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import static untilz.HelpMethods.*;
import static untilz.Constants.PlayerConstants.*;
import static untilz.Constants.GRAVITY;
import static untilz.Constants.Exp;

public class Player extends Entity {

	// INFO
	private int playerId;
	private String Name;
	private int Level;
	private long currEXP;
	private int mapId;
	private int gold;

	private boolean moving = false, isAttacking = false;
	private boolean hasDealtDamage = false;
	private boolean changeFrame = false;
	private boolean left, right, jump;
	private boolean interact = false;

	private int[][] lvlData;
	private float xDrawOffset = 2 * Game.SCALE;
	private float yDrawOffset = 1 * Game.SCALE;

	// JUMPING / GRAVITY
	private float jumpSpeed = -2.3f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;

	// TASK
	protected int charID;
	protected byte ctaskId = 0;
	protected byte ctaskIndex = -1;
	protected short ctaskCount = 0;
	protected boolean isDoTask = false;
	protected boolean isDoneTask = false;
	protected boolean isDefaultTask = false;

	// StatusBarUI
	private BufferedImage statusBarImg;
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (152 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);// offsetX in status bar
	private int healthBarYStart = (int) (14 * Game.SCALE);// offsetY
	private float lostHealthWidth = healthBarWidth;

	private int manaBarWidth = (int) (103 * Game.SCALE);
	private int manaBarHeight = (int) (2.6 * Game.SCALE);
	private int manaBarXStart = (int) (45 * Game.SCALE);
	private int manaBarYStart = (int) (34 * Game.SCALE);
	private float lostManaWidth = manaBarWidth;

	private int expBarWidth = (int)(103 * Game.SCALE);
	private int expBarHeight = (int)(2.6 * Game.SCALE);
	private int expBarXStart = (int)(45*Game.SCALE);
	private int expBarYStart = (int)(47 * Game.SCALE);
	
	
	private int healthWidth = healthBarWidth;
	private int manaWidth = manaBarWidth;
	private int expWidth = expBarWidth;
	private int attack = DEFAULT_ATTACK;
	private int defend = DEFAULT_DEF;
	private int maxMana = DEFAULT_MANA;
	private float restoreHPSpeed = 1.0f;
	private int def = defend;
	private int damage = attack;
	private int mana = maxMana;
	protected int currMana;
	private float dmg_down;

	private long previousTime1 = 0;
	private long previousTime2 = 0;
	private long restoreTime = 1000000000L;

	// Attack Box

	private int attackBoxWidth = (int) (20 * Game.SCALE);
	private int attackBoxHeight = (int) (20 * Game.SCALE);

	// Flip
	private int flipX = 0;
	private int flipW = 1;
	private int tileY = 0;

	private int currFrame = 0;
	private Playing playing;
	private String descriptionTask = "";

	ArrayList<Animation> animList = new ArrayList<Animation>();

	public Player(User user, float x, float y, int width, int height) {
		super(x, y, width, height);
		tileY = (int) (x / Game.TILES_SIZE);
		this.state = IDLE;
		this.maxHealth = DEFAULT_MAXHEALTH;
		this.currHealth = maxHealth;
		maxMana = DEFAULT_MANA;
		currMana = maxMana;
		this.walkSpeed = Game.SCALE * 1.0f;
		this.currEXP = 0;
		this.Level = 1;
		this.mapId = 0;
		loadAnim();
		initHitbox(20, 28);
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
		animList.add(CacheDataLoader.getInstance().getAnimation("death"));

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}

	// UPDATE
	public void update(long currTime) {
		if (currHealth <= 0) {

			if (state != DEAD) {
				animList.get(state).reset();
				state = DEAD;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);

			} else if (animList.get(state).isLastFrame()
					&& animList.get(state).getDeltaTime() >= animList.get(state).getDelayFrames().get(0) - 1) {
				playing.setGameOver(true);
			} else {
				animList.get(this.state).Update(currTime);
			}
			return;
		}

		updateAttackBox();
		restoreHealth(currTime);
		updateHealthBar();

		restoreMana(currTime);
		updateManaBar();

		updateExpBar();
		
		updatePos();
		if (moving) {
			checkSpikesTouched();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
		}

		if (isAttacking)
			Attack();
		if (isDoTask)
			Task.doTask(this, playing);

		updateAniamtion();
		setAnimation();
		animList.get(this.state).Update(currTime);
		// updateHitbox();
	}

	private void restoreHealth(long currTime) {
		if (currTime - previousTime1 > restoreTime) {
			if (currHealth < maxHealth) {
				currHealth += restoreHPSpeed;
			} else if (currHealth < 0) {
				currHealth = 0;
			}
			previousTime1 = currTime;
		}

	}

	private void restoreMana(long currTime) {
		if (currTime - previousTime2 > restoreTime) {
			if (currMana < maxMana) {
				currMana += 1;
			} else if (currMana < 0) {
				currMana = 0;
			}
			previousTime2 = currTime;
		}

	}

	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
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
				playing.getGame().getAudioPlayer().playAttackSound();
			}
			if (hasDealtDamage) {
				playing.checkEnemyHit(attackBox);
				playing.checkObjectHit(attackBox);
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

		if (healthWidth < lostHealthWidth) {
			lostHealthWidth -= 0.3;
		} else if (healthWidth >= lostHealthWidth) {// update when health change
			lostHealthWidth = healthWidth;
		}

	}

	private void updateManaBar() {
		manaWidth = (int) ((currMana / (float) maxMana) * manaBarWidth);

		if (manaWidth < lostManaWidth) {
			lostManaWidth -= 0.3;
		} else if (manaWidth >= lostManaWidth) {
			lostManaWidth = manaWidth;
		}
	}
	
	private void updateExpBar() {
		expWidth = (int)((currEXP / (float)Game.exps[Level]) * manaBarWidth);
	}

	private void updatePos() {
		moving = false;

		if (!interact) {

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
				if (!IsEntityOnFloor(hitbox, lvlData)) {
					inAir = true;
				}
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

	}

	private void jump() {
		if (inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);

	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere((hitbox.x - hitbox.width / 2) + xSpeed, (hitbox.y - hitbox.height / 2), hitbox.width,
				hitbox.height, lvlData)) {// x+xSpeed,y+ySpeed is next position of player
			if (!isAttacking && !interact)// ATTACK IN AIR IS HERE
				hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}

	}	

	public void changeHealth(int value) {

		value = (int) (((value * (100 / (float) (100 + defend))* (1 - dmg_down))));
		currHealth += value;
		if (currHealth <= 0) {
			currHealth = 0;
		} else if (currHealth >= maxHealth) {
			currHealth = maxHealth;
		}
	}

	public void changeMana(int value) {
		currMana += value;
		if (currMana <= 0) {
			currMana = 0;
		} else if (currMana >= maxMana) {
			currMana = maxMana;
		}

	}

	private void setAnimation() {
		int startAni = this.state;

		if (interact)
			this.state = IDLE;
		else {
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
	}

	// RENDER
	public void render(Graphics g, int xLvlOffset) {
		animList.get(this.state).draw((int) (hitbox.x - xDrawOffset) - xLvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset), width * flipW, height, g);
		// drawHitbox(g, xLvlOffset);
		g.setColor(new Color(255, 255, nextInt(255)));
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		if (isDoTask) {
			g.drawString(
					descriptionTask + "  " + NightBorne.getDeadCount() + "/"
							+ Game.taskTemplates[ctaskId].counts[ctaskIndex + 1],
					(int) (10 * Game.SCALE), (int) (140 * Game.SCALE));
		}
		if (isDoneTask || isDefaultTask) {
			g.setColor(new Color(255, nextInt(255), 255));
			g.drawString(descriptionTask, (int) (10 * Game.SCALE), (int) (140 * Game.SCALE));
		}
		realHitbox.x = (hitbox.x - hitbox.width / 2);
		realHitbox.y = (hitbox.y - hitbox.height / 2);
		// drawHitbox(g, xLvlOffset);
		// drawAttackBox(g, xLvlOffset);
		renderUI(g);
	}

	private void renderUI(Graphics g) {

		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.YELLOW);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, (int) lostHealthWidth, healthBarHeight);
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		g.setColor(Color.GREEN);
		g.fillRect((manaBarXStart + statusBarX), (int) (manaBarYStart + statusBarY), (int) lostManaWidth,
				manaBarHeight);
		g.setColor(Color.BLUE);
		g.fillRect((manaBarXStart + statusBarX), (int) (manaBarYStart + statusBarY), manaWidth, manaBarHeight);
		g.setColor(Color.PINK);
		g.fillRect(expBarXStart + statusBarX, expBarYStart + statusBarY,expWidth,expBarHeight);
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
		if (!interact)
			this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		if (!interact)
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

	public int getFlipW() {
		return this.flipW;
	}

	public int getAttack() {
		return damage;
	}

	public void setAttack(int damage) {
		this.damage = damage;
	}

	public int getCurrDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public float getDmg_down() {
		return dmg_down;
	}

	public void setDmg_down(float dmg_down) {
		this.dmg_down = dmg_down;
	}

	public int getCurrMaxMana() {
		return this.mana;
	}

	public int getCurrMana() {
		return this.currMana;
	}

	public void applyExp(long exp) {
		this.currEXP += exp;
		updateLevel();
	}

	public boolean isInteract() {
		return interact;
	}

	public void setInteract(boolean interact) {
		this.interact = interact;

	}

	public void setAttackBoxPos(int x, int y) {
		this.attackBox.x = x;
		this.attackBox.y = y;
	}

	public byte getCtaskId() {
		return ctaskId;
	}

	public void setCtaskId(byte ctaskId) {
		this.ctaskId = ctaskId;
	}

	public byte getCtaskIndex() {
		return ctaskIndex;
	}

	public void setCtaskIndex(byte ctaskIndex) {
		this.ctaskIndex = ctaskIndex;
	}

	public short getCtaskCount() {
		return ctaskCount;
	}

	public void setDoneTask(boolean isDoneTask) {
		this.isDoneTask = isDoneTask;
	}

	public boolean isDoneTask() {
		return isDoneTask;
	}

	public void setIsDefaultTask(boolean isDefaultTask) {
		this.isDefaultTask = isDefaultTask;
	}

	public boolean IsDefaultTask() {
		return this.isDefaultTask;
	}

	public String getPlayerName() {
		return this.Name;
	}

	public void setCtaskCount(short ctaskCount) {
		this.ctaskCount = ctaskCount;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlaying(Playing playing) {
		this.playing = playing;
	}

	public void setDoTask(boolean isDoTask) {
		this.isDoTask = isDoTask;
	}

	public boolean isDoTask() {
		return this.isDoTask;
	}

	public void setDescriptionTask(String descriptionTask) {
		this.descriptionTask = descriptionTask;
	}

	public void applyDef(int def) {
		this.def = def;
	}

//
//	public void applyAtk(int attack) {
//		this.ATK = attack;
//	}
	public void applyHeal(int hp) {
		if (currHealth + hp > maxHealth)
			currHealth = maxHealth;
		else
			currHealth += hp;
	}

	public int getDef() {
		return this.def;
	}

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public void updateLevel() {
		if (currEXP < Game.exps[Level])
			return;
		currEXP = currEXP - Game.exps[Level];
		BuffEffect.setIsLevelUp(true);
		this.maxHealth += (int)(this.maxHealth  * 0.2);
		this.maxMana += (int)(this.maxMana * 0.2);
		Level += 1;
	}

	public long getCurrExp() {
		return this.currEXP;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold += gold;
		System.out.println(gold);
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

	public int getTileY() {
		return tileY;
	}

	public static Player getPlayer(User user, int id) {
		final Player player = new Player(user, 0, 0, (int) (Game.TILES_SIZE * 4), (int) (Game.TILES_SIZE * 2));
		try {
			MySQL mySQL = new MySQL(0);
			ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `player` WHERE `player_id`=" + id + " LIMIT 1;");
			if (red.first()) {
				player.playerId = red.getInt("player_id");
				player.gold = red.getInt("gold");
				player.mapId = red.getInt("map_id");

				player.x = red.getInt("coordinates_x");
				player.y = red.getInt("coordinates_y");
				player.hitbox.x = player.x;
				player.hitbox.y = player.y;

			} else {
				return null;
			}
			mySQL.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PhysicalMap.loadMapData();
		PhysicalMap.loadMobData(id);
		PhysicalMap.loadNpcData(id);
		InventoryManager.loadInventoryData(id);
		return player;
	}
	public static void setUpPlayerStatus(Player player) {
		try {
			MySQL mySQL = new MySQL(0);
			ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `player` WHERE `player_id`=" + player.getPlayerId() + " LIMIT 1;");
			if (red.first()) {

				player.currEXP = red.getLong("experience");
				player.damage = red.getInt("attack");
				player.currHealth = red.getInt("health_point");
				player.changeHealth(0);
				player.currMana = red.getInt("mana");
				player.def = red.getInt("defense");
			} else {
				return;
			}
			
			red.close();
			red = mySQL.stat.executeQuery("SELECT * FROM dotask WHERE `player_id` = " + player.getPlayerId() + ";");
			if(red.first()) {
				player.ctaskId = red.getByte("task_id");
				player.ctaskIndex = red.getByte("task_index");
				player.ctaskCount = red.getByte("task_count");
			}else {
				return;
			}
			red.close();
			mySQL.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean savePlayerData(Playing playing) {
		final Player player = playing.getPlayer();
		// maybe bug in player.getDef
			MySQL.savePlayerState((int) player.getCurrExp(), player.getLevel(), player.getCurrDamage(), player.getDef(),
					player.getCurrHealth(),player.getCurrMana(), player.getGold(), (int) player.getHitbox().getX(),
					(int) player.getHitbox().getY(), player.getMapId(), player.getPlayerId());
			MySQL.saveTaskState(player.getPlayerId(), player.getCtaskId(), player.getCtaskIndex(), player.getCtaskCount());
		return true;
	}

}
