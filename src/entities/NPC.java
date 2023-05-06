package entities;

import static untilz.Constants.GRAVITY;
import static untilz.Constants.Directions.*;
import static untilz.Constants.EnemyConstants.ATTACK;
import static untilz.Constants.EnemyConstants.DEAD;
import static untilz.Constants.EnemyConstants.GetEnemyDmg;
import static untilz.Constants.EnemyConstants.HURT;
import static untilz.Constants.EnemyConstants.IDLE;
import static untilz.HelpMethods.CanMoveHere;
import static untilz.HelpMethods.GetEntityYPosUnderRoofOfAboveFloor;
import static untilz.HelpMethods.IsEntityOnFloor;
import static untilz.HelpMethods.IsFloor;
import static untilz.HelpMethods.IsSightClear;

import java.awt.geom.Rectangle2D;

import Effect.Animation;
import Task.Task;
import gamestates.Playing;
import main.Game;

public abstract class NPC extends Entity{
	protected int npcState,npcType;
	protected boolean firstUpdate = true;
	protected boolean inAir;
	protected float fallSpeed;
	protected float gravity = 0.04f * Game.SCALE;
	protected int tileY;
	protected float contactDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean haveTask = false;
	protected boolean finishTask = false;
	protected int npcId;
	protected static int currNpcId;
	
	public NPC(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.npcType = enemyType;
		state = IDLE;
		initHitbox(width, height);
	}
	
public void update(int[][] lvlData,Animation anim) {
		
		if(anim.isLastFrame()) 
			switch(this.state) {
			case ATTACK-> this.state = IDLE;
			case HURT-> this.state = IDLE;
			case DEAD -> active = false;
			}			
	}
	protected void firstUpdateCheck(int[][] lvlData) {
		
			if(!IsEntityOnFloor(hitbox,lvlData))
				inAir = true;			
			firstUpdate = false;
	}
	protected void updateInAir(int[][] lvlData) {
		if(CanMoveHere(hitbox.x - hitbox.width/2, (hitbox.y - hitbox.height/2) + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		}else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOfAboveFloor(hitbox, airSpeed);
			tileY = (int)((hitbox.y) / Game.TILES_SIZE);
		}
		
	}

	protected void newState(int enemyState, Animation anim) {
		this.state = enemyState;
		anim.reset();	
	}
	protected boolean canSeePlayer(int[][] lvlData,Player player) {
		int playerTileY = (int) ((player.getHitbox().y) / Game.TILES_SIZE);
		if(playerTileY == tileY) {
//			if(isPlayerInRanger(player)) {
//				if(IsSightClear(lvlData,hitbox,player.hitbox,tileY))
//					return true;
//			}
		}
		return false;
	}
	
	protected boolean isPlayerCloseForContact(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= contactDistance;
	}
	public void setHaveTask(boolean haveTask,Player player) {
		this.haveTask = haveTask;
	}
	public void setFinishTask(boolean finishTask) {
		this.finishTask = finishTask;
	}
	public boolean getFinishTask() {
		return this.finishTask;
	}
	public boolean getHaveTask() {
		return this.haveTask;
	}
	public boolean isActive() {
		return active;
	}
	public static void setCurrNpcId(int currNpcId) {
		NPC.currNpcId = currNpcId;
	}
	public static int getCurrNpcId() {
		return NPC.currNpcId; 
	}
	public void resetNPC() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currHealth = maxHealth;
		//newState(IDLE);
		active = true;
		airSpeed = 0;
	}
}
