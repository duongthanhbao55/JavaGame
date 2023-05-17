package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {
	
	//VARIABLE
	protected float x,y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	protected int state;
	protected float airSpeed;
	protected int maxHealth;
	protected int currHealth = maxHealth;

	protected Rectangle2D.Float attackBox;
	protected Rectangle2D.Float realHitbox;
	protected float walkSpeed = 1.0f * Game.SCALE;
	protected int tileY;
	
	//CONSTRUCTOR
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		//initHitbox();
	}



	//FUNCTION
	protected void drawAttackBox(Graphics g,int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int)(attackBox.x - hitbox.width/2 - xLvlOffset),(int) (attackBox.y - hitbox.height/2), (int)attackBox.width,(int) attackBox.height);
	}
	protected void initHitbox(int width, int height) {
		
		hitbox = new Rectangle2D.Float((int)x,(int)y,width * Game.SCALE,height * Game.SCALE);
		realHitbox = new Rectangle2D.Float((int)(hitbox.x - hitbox.width/2),(int) (hitbox.y  - hitbox.height/2),(int) hitbox.width,(int) hitbox.height);
	}
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		//DEBUGGING
		g.setColor(Color.pink);
		g.drawRect((int)(hitbox.x - hitbox.width/2) - xLvlOffset,(int) (hitbox.y  - hitbox.height/2),(int) hitbox.width,(int) hitbox.height);

	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	public Rectangle2D.Float getRealHitbox(){
		return realHitbox;
	}
	
	public int getState() {
		return this.state;
	}
	public void setMaxHealth(int hp) {
		int heal = hp - this.maxHealth;
		this.maxHealth = hp;
		if (currHealth + heal > maxHealth) currHealth = maxHealth;
		else
			this.currHealth += heal;
	}
	public int getMaxHealth() {
		return this.maxHealth;
	}
	public int getCurrHealth() {
		return this.currHealth;
	}
}
