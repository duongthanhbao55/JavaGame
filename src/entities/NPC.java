package entities;

import static untilz.Constants.Directions.*;
import static untilz.Constants.EnemyConstants.IDLE;

import Effect.Animation;
import gamestates.Playing;
import main.Game;

public abstract class NPC extends Entity{
	protected int npcState,npcType;
	protected boolean firstUpdate = true;
	protected boolean inAir;
	protected float fallSpeed;
	protected float gravity = 0.04f * Game.SCALE;
	protected float walkSpeed = 0.5f * Game.SCALE;
	protected int walkDir = LEFT;
	protected int tileY;
	protected int currHealth;
	protected boolean active = true;
	
	public NPC(float x, float y, int width, int height, int enemyType,Playing playing) {
		super(x, y, width, height, playing);
		this.npcType = enemyType;
		npcState = IDLE;
		initHitbox(width, height);
	}
	
	//UPDATE
	protected void update(int [][] lvlData, Animation anim) {
		
	}

	
	protected void updateState() {
		
	}
	
	
	//FUNCTION
	protected void move(int[][] lvlData) {
		
	}
	protected void newState(int enemyState, Animation anim) {
		this.npcState = enemyState;
		anim.reset();	
	}
}
