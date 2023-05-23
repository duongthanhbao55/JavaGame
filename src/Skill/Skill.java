package Skill;

import static untilz.Constants.Directions.RIGHT;
import static untilz.Constants.PlayerConstants.ATTACK_1;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class Skill {
	protected int x,y,width,height;
	protected Rectangle bounds;
	protected Rectangle2D.Float attackBox;
	protected int dir;
	protected boolean hasDealtDamage;
	protected Skill(int x, int y, int width,int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		initBounds();
	}
	
	protected void update(long currTime) {
		
	}
	protected void render(Graphics g,int xLvlOffset) {
		
	}
	
	private void initBounds() {
		bounds = new Rectangle(x,y,width,height);
		attackBox = new Rectangle2D.Float(0,0,0,0);
	}
	public Rectangle getBounds() {
		return this.bounds;
	}
	public void setPos(int x, int y, int dir) {
		bounds.x = x;
		bounds.y = y;
		this.dir = dir;
	}
	public int flipX() {
		if (dir == RIGHT)
			return 0;
		else
			return (int)this.bounds.getWidth();

	}

	public int flipW() {
		if (dir == RIGHT)
			return 1;
		else
			return -1;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public void setBoundsSize(int width, int height) {
		bounds.width = width;
		bounds.height = height;
	}
	protected void setAttackBox(float x, float y, float width, float height) {
		attackBox = new Rectangle2D.Float(x,y,width,height);
	}
}
