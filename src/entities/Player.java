package entities;

import Effect.Animation;
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
	private static int playerId;
	private String Name;
	private int Level;
    private long currEXPCap = Exp.BASE_EXP_CAP;
    private long currEXP;
	private long ExpDown;
	private int vip;
	private int mapId;
	private int gold;
	private User user;

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
	private int attack = DEFAULT_ATTACK;
	private int defend = DEFAULT_DEF;
	private int damage = attack;
	final private int maxMana = DEFAULT_MANA;
	private int mana = maxMana;
	private float dmg_down;

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
		this.user = user;
		tileY = (int) (x / Game.TILES_SIZE);
		this.state = IDLE;
		this.maxHealth = DEFAULT_MAXHEALTH;
		this.currHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnim();

        this.currEXP = 0;
		this.ExpDown = 0;
		this.Level = 1;
		this.mapId = 0;
		this.playerId = -9999;

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
		updateHealthBar();
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

		value = (int) (((value * (100 / (float) (100 + defend))))*(1 - dmg_down));
		currHealth += value;
		if (currHealth <= 0) {
			currHealth = 0;
		} else if (currHealth >= maxHealth) {
			currHealth = maxHealth;
		}
	}

	public void hurt(int damage) {
		int value = (int) ((damage * (100/(float)(100 + defend))));;
		currHealth -= value;
		if (currHealth < 0) currHealth = 0;

	}

	public void changeMana(int value) {
		System.out.println("+" + value + " mana!");

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
		if (isDoneTask) {
			g.setColor(new Color(255, nextInt(255), 255));
			g.drawString(descriptionTask, (int) (10 * Game.SCALE), (int) (140 * Game.SCALE));
		}
		realHitbox.x = (hitbox.x - hitbox.width/2);
		realHitbox.y = (hitbox.y  - hitbox.height/2);
		
		

		// drawAttackBox(g, xLvlOffset);
		renderUI(g);
	}

	private void renderUI(Graphics g) {

		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.YELLOW);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, (int) lostHealthWidth, healthBarHeight);
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

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) { this.attack = attack; }

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void applyMana(int mana) {
		if (this.mana + mana >= maxMana){
			this.mana = maxMana;
		}
		else {
            this.mana += mana;
        }
	}

	public float getDmg_down() {
		return dmg_down;
	}

	public void setDmg_down(float dmg_down) {
		this.dmg_down = dmg_down;
	}

	public int getMana() {
		return this.mana;
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

	public String getPlayerName() {
		return this.Name;
	}

	public void setCtaskCount(short ctaskCount) {
		this.ctaskCount = ctaskCount;
	}

	public int getPlayerId() {
		return this.playerId;
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

	public void applyDef(int defend) {
		this.defend = defend;
	}
//
//	public void applyAtk(int attack) {
//		this.ATK = attack;
//	}
    public void applyHeal(int hp) {
        if (currHealth + hp > maxHealth) currHealth = maxHealth;
        else currHealth += hp;
    }

	public int getDef() {
		return this.defend;
	}

	public int getAtk() {
		return this.damage;
	}

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

    public void updateLevel() {
        if (currEXP < currEXPCap) return;
        currEXP = currEXP - currEXPCap;
        switch (Level / 10) {
            case 0 -> currEXPCap = (long) (currEXPCap * Exp.EXP_INCREASE_LV10);
            case 1 -> currEXPCap = (long) (currEXPCap * Exp.EXP_INCREASE_LV20);
        }
        Level += 1;
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
			ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `player` WHERE `playerId`=" + id + " LIMIT 1;");
			if (red.first()) {
				player.playerId = red.getInt("playerId");
				player.ctaskId = red.getByte("ctaskId");
				player.ctaskIndex = red.getByte("ctaskIndex");
				player.ctaskCount = red.getShort("ctaskCount");
				// player.cspeed = red.getByte("cspeed");
				player.Name = red.getString("cName");
                player.currEXP = red.getLong("cEXP");
				player.ExpDown = red.getLong("cExpDown");
				player.gold = red.getInt("xu");
				System.out.println("gold" + player.gold);
				player.Level = red.getInt("cLevel");
				player.vip = red.getInt("vip");
				player.mapId = red.getInt("idMap");
				
				JSONArray jarr2 = (JSONArray) JSONValue.parseWithException(red.getString("InfoMap"));
				player.x = Short.parseShort(jarr2.get(0).toString());
				player.y = Short.parseShort(jarr2.get(1).toString());
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
		InventoryManager.loadInventoryData();
		Equipment.loadEquipment();
		return player;
	}

	public static boolean savePlayerData(PreparedStatement statement,Playing playing) {
	
		try {
			final MySQL mySQL = new MySQL(0);
			try {
				Connection conn = MySQL.getConnection(0);
				String updateQuery = "UPDATE player SET ctaskId = ?, ctaskIndex = ?, ctaskCount = ?, cspeed = ?, cName = ?, cEXP = ?, cExpDown = ?, cLevel = ?, xu = ?, idMap = ?, InfoMap = ?, vip = ? WHERE playerId = ?";
				PreparedStatement pstmt = conn.prepareStatement(updateQuery);
				
				statement.setInt(1, playing.getPlayer().getCtaskId());
				statement.setInt(2, playing.getPlayer().getCtaskIndex());
				statement.setInt(3, playing.getPlayer().getCtaskCount());
				statement.setInt(4, 2);
				statement.setLong(6, 0);
				statement.setLong(7, 0);
				statement.setInt(8, playing.getPlayer().getLevel());
				statement.setInt(9, playing.getPlayer().getGold());
				statement.setInt(10, playing.getPlayer().getMapId());
				String xPos = Double.toString(playing.getPlayer().getHitbox().getX());
				String yPos = Double.toString(playing.getPlayer().getHitbox().getY());
				statement.setString(11, "[" + xPos + "," + yPos + "]");
				
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("New user has been inserted successfully!");
					return true;
				} else {
					System.out.println("User not found with id = " + playerId);
				}
			} finally {
				mySQL.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
