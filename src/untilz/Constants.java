package untilz;

import main.Game;

public class Constants {
	public static final float GRAVITY = 0.04f * Game.SCALE;
	
	public static class Objecttiles{
		public static final int CANNON_BALL_DEFAULT_WIDTH = 15 ;
		public static final int CANNON_BALL_DEFAULT_HEIGHT = 15 ;
		public static final int CANNON_BALL_WIDTH = (int) (CANNON_BALL_DEFAULT_WIDTH * Game.SCALE);
		public static final int CANNON_BALL_HEIGHT = (int)(CANNON_BALL_DEFAULT_HEIGHT * Game.SCALE);
		public static final float SPEED = 0.75f * Game.SCALE;
		public static final int CANNON_BALL_DAMAGE = 15;
		
	}
	
	public static class ObjectConstants{
		public static final int HEAL_POTION = 0;
		public static final int MANA_POTION = 1;
		public static final int BARREL = 2;
		public static final int BOX = 3;
		public static final int SPIKE = 4;
		public static final int LEFT_CANNON = 5;
		public static final int RIGHT_CANNON = 6;
		
		
		public static final int HEAL_POTION_VALUE = 15;
		public static final int MANA_POTION_VALUE = 10;
		
		public static final int CONTAINER_WIDTH_DEFAULT = 40;
		public static final int CONTAINER_HEIGHT_DEFAULT = 30;
		public static final int CONTAINER_WIDTH = (int)(Game.SCALE * CONTAINER_WIDTH_DEFAULT );
		public static final int CONTAINER_HEIGHT = (int)(Game.SCALE * CONTAINER_HEIGHT_DEFAULT );
		
		public static final int POTION_WIDTH_DEFAULT = 12;
		public static final int POTION_HEIGHT_DEFAULT = 16;
		public static final int POTION_WIDTH = (int)(Game.SCALE * POTION_WIDTH_DEFAULT);
		public static final int POTION_HEIGHT = (int)(Game.SCALE * POTION_HEIGHT_DEFAULT);
		
		public static final int SPIKE_WIDTH_DEFAULT = 32;
		public static final int SPIKE_HEIGHT_DEFAULT = 32;
		public static final int SPIKE_WIDTH = (int)(Game.SCALE * SPIKE_WIDTH_DEFAULT);
		public static final int SPIKE_HEIGHT = (int)(Game.SCALE * SPIKE_HEIGHT_DEFAULT);
		
		public static final int CANNON_WIDTH_DEFAULT = 40;
		public static final int CANNON_HEIGHT_DEFAULT = 26;
		public static final int CANNON_WIDTH = (int)(Game.SCALE * CANNON_WIDTH_DEFAULT);
		public static final int CANNON_HEIGHT = (int)(Game.SCALE * CANNON_HEIGHT_DEFAULT);
		public static int GetObjectDmg(int enemy_type) {
			switch(enemy_type) {
			case SPIKE:
				return 15;
			case LEFT_CANNON,RIGHT_CANNON:
				return 20;
			default:
				return 0;
			}
		}
		
	}
	
	public static class EnemyConstants{
		public static final int NIGHTBORNE = 0;
		
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HURT = 3;
		public static final int DEAD = 4;
		
		public static final int NIGHTBORNE_SIZE_DEFAULT = 80;
		public static final int NIGHTBORNE_SIZE = (int)(NIGHTBORNE_SIZE_DEFAULT * Game.SCALE);
		
		public static final int NIGHTBORNE_DRAWOFFSET_X = (int)(0 * Game.SCALE);
		public static final int NIGHTBORNE_DRAWOFFSET_Y = (int)(7 * Game.SCALE);
		

		
		public static int GetMaxHealth(int enemy_type) {
			switch(enemy_type) {
			case NIGHTBORNE:
				return 15;
			default:
				return 1;
			}
		}
		
		public static int GetEnemyDmg(int enemy_type) {
			switch(enemy_type) {
			case NIGHTBORNE:
				return 15;
			default:
				return 0;
			}
		}
		
	}
	public static class NPC_Wizard1{
		public static final int WIZARD1 = 0;
		
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		
		public static final int WIZARD1_SIZE_DEFAULT = 80;
		public static final int WIZARD1_SIZE = (int)(WIZARD1_SIZE_DEFAULT * Game.SCALE);
		
		public static final int WIZARD1_DRAWOFFSET_X = (int)(2 * Game.SCALE);
		public static final int WIZARD1_DRAWOFFSET_Y = (int)(7 * Game.SCALE);

	}
	public static class Enviroment{
		public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
		public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
		public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
		public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
		
		public static final int BIG_CLOUD_WIDTH = (int)(BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int BIG_CLOUD_HEIGHT = (int)(BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_WIDTH = (int)(SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_HEIGHT = (int)(SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int SKY_WIDTH_DEFAULT = 320;
		public static final int SKY_HEIGHT_DEFAULT = 240;
		public static final int FAR_CLOUD_WIDTH_DEFAULT = 128;
		public static final int FAR_CLOUD_HEIGHT_DEFAULT = 240;
		public static final int NEAR_CLOUD_WIDTH_DEFAULT = 160;
		public static final int NEAR_CLOUD_HEIGHT_DEFAULT = 240;
		public static final int FAR_MOUNTAIN_WIDTH_DEFAULT = 160;
		public static final int FAR_MOUNTAIN_HEIGHT_DEFAULT = 240;
		public static final int MOUNTAIN_WIDTH_DEFAULT = 320;
		public static final int MOUNTAIN_HEIGHT_DEFAULT = 240;
		public static final int TREE_SIZE_DEFAULT= 240;
		
		
		public static final int SKY_WIDTH =(int)(SKY_WIDTH_DEFAULT * Game.SCALE);
		public static final int SKY_HEIGHT = (int)(SKY_HEIGHT_DEFAULT * Game.SCALE);
		public static final int FAR_CLOUD_WIDTH = (int)(FAR_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int FAR_CLOUD_HEIGHT = (int)(FAR_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
		public static final int NEAR_CLOUD_WIDTH = (int)(NEAR_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int NEAR_CLOUD_HEIGHT = (int)(NEAR_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
		public static final int FAR_MOUNTAIN_WIDTH = (int)(FAR_MOUNTAIN_WIDTH_DEFAULT * Game.SCALE);
		public static final int FAR_MOUNTAIN_HEIGHT = (int)(FAR_MOUNTAIN_HEIGHT_DEFAULT * Game.SCALE);
		public static final int MOUNTAIN_WIDTH = (int)(MOUNTAIN_WIDTH_DEFAULT * Game.SCALE);
		public static final int MOUNTAIN_HEIGHT = (int)(MOUNTAIN_HEIGHT_DEFAULT * Game.SCALE);
		public static final int TREE_SIZE = (int)(TREE_SIZE_DEFAULT * Game.SCALE);
		
	}
	
	public static class UI{
		public static class Button{
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
			
		}
		public static class PauseButton{
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
			
		}
		public static class UrmButton{
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
			
		}
		public static class VolumeButtons{
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;
			
			
			
			
			public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);
		
		}
		public static class TextBox{
			public static final int TEXTBOX_WIDTH_DEFAULT = 450;
			public static final int TEXTBOX_HEIGHT_DEFAULT = 150;
			
			public static final int TEXTBOX_WIDTH = (int)(TEXTBOX_WIDTH_DEFAULT * Game.SCALE / 2);
			public static final int TEXTBOX_HEIGHT = (int)(TEXTBOX_HEIGHT_DEFAULT * Game.SCALE / 4);
			
		}
	}
	
	
	public static class Directions{
		public static final int UP = 0;
		public static final int DOWN = 1;
		public static final int LEFT = 2;
		public static final int RIGHT = 3;
	}
	
	public static class PlayerConstants{
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK_1 = 2;
		public static final int JUMP = 3;
		public static final int FALLING = 4;
		public static final int DEAD = 5;
		public static final int GROUND = 6;		
		public static final int ATTACK_JUMP = 7;
		
		public static final int DEFAULT_DAMAGE = 5;
	}
	public static class VFX{
		public static final int HIT_EFFECT_HEIGHT_DEFAULT = 48;
		public static final int HIT_EFFECT_WIDTH_DEFAULT = 48;
		
		public static final int HIT_EFFECT_HEIGHT = (int)(HIT_EFFECT_HEIGHT_DEFAULT * Game.SCALE);
		public static final int HIT_EFFECT_WIDTH = (int)(HIT_EFFECT_WIDTH_DEFAULT * Game.SCALE);
	}
}
