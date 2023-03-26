package entities;

import static untilz.Constants.EnemyConstants.ATTACK;
import static untilz.Constants.EnemyConstants.HURT;
import static untilz.Constants.EnemyConstants.IDLE;
import static untilz.Constants.EnemyConstants.RUNNING;

import java.util.ArrayList;

import Effect.Animation;
import Load.CacheDataLoader;
import gamestates.Playing;
import main.Game;

public class NPC_Wizard1 extends NPC{

	//VARIBALE
	ArrayList<Animation> Wizard1AnimList = new ArrayList<Animation>();
	
	public NPC_Wizard1(float x, float y, int width, int height, int enemyType, Playing playing) {
		super(x, y, width, height, enemyType, playing);
		initHitbox(30,30);
		LoadAnimNPC();
	}
	
	private void LoadAnimNPC() {
		
		Wizard1AnimList.add(CacheDataLoader.getInstance().getAnimation("WZ1Idle"));
		Wizard1AnimList.add(CacheDataLoader.getInstance().getAnimation("WZ1Run"));
	}
	//UPDATE
	public void update(long currTime,int[][] lvlData,Player player) {
		super.update(lvlData, Wizard1AnimList.get(npcState));
		updateBehavior(lvlData,player);
		//updateAttackBox();
		Wizard1AnimList.get(npcState).Update(currTime);
	}
	private void updateBehavior(int[][] lvlData, Player player) {

		if(firstUpdate)
			firstUpdateCheck(lvlData);
			
		if(inAir) {
			updateInAir(lvlData);
		}else {

			switch(npcState) {
			case IDLE:
				newState(RUNNING,Wizard1AnimList.get(npcState));
				break;
			case RUNNING:			
				move(lvlData);
				break;
			case ATTACK:
//				if(NightborneAnimList.get(enemyState).getCurrentFrame() == 0)
//						attackChecked = false;
//				
//				if(NightborneAnimList.get(enemyState).getCurrentFrame() == 5
//					&& !attackChecked) {
//					checkPlayerHit(attackBox,player);
//				}
					
				break;
			}
		}

	}

	private void updateInAir(int[][] lvlData) {
		// TODO Auto-generated method stub
		
	}

	private void firstUpdateCheck(int[][] lvlData) {
		// TODO Auto-generated method stub
		
	}

}
