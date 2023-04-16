package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import Effect.Animation;
import Load.CacheDataLoader;
import main.Game;

import static untilz.Constants.Objecttiles.*;

public class Projectiles {
	private Rectangle2D.Float hitbox;
	private int dir;
	private boolean active = true;
	private Animation cannonBall;

	
	Projectiles(int x, int y, int dir){
		int xOffset = (int)(-2 * Game.SCALE);
		int yOffset = (int)(0 * Game.SCALE);
		if(dir == 1) {
			xOffset = (int)(15 * Game.SCALE);
		}
		hitbox = new Rectangle2D.Float(x + xOffset,y + yOffset,CANNON_BALL_WIDTH,CANNON_BALL_HEIGHT);
		this.dir = dir;
		loadAnim();
	}
	
	private void loadAnim() {
		cannonBall = CacheDataLoader.getInstance().getAnimation("Ball");
	}
	 void updatePos() {
		hitbox.x += dir * SPEED;
	}
	public void render(Graphics g, int xLvlOffset) {
		cannonBall.draw((int)(hitbox.getX() - xLvlOffset), (int)hitbox.getY(), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, g);
	}
	public void setPos(int x, int y) {
		hitbox.x = x;
		hitbox.y = y;	
	}
	
	public Rectangle2D.Float getHitbox(){	
		return this.hitbox;
	}
	
	public void setActive(boolean active) {	
		this.active = active;
	}
	
	public boolean isActive() {
		return this.active;
	}
}
