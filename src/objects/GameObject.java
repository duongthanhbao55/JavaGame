package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import static untilz.Constants.ObjectConstants.*;

import main.Game;

public class GameObject {

	protected int x, y, objType;
	protected Rectangle2D.Float hitbox;
	protected boolean doAnimation, active = true;
	protected int xDrawOffset, yDrawOffset;

	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}
	
	
	
	public void reset() {
		active = true;
		
		if(objType == BARREL || objType == BOX || objType == RIGHT_CANNON || objType == LEFT_CANNON) {
			doAnimation = false;
		} else
		doAnimation = true;
	}
	
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float((int)x,(int)y,width * Game.SCALE,height * Game.SCALE);
	}
	public void drawHitbox(Graphics g, int xLvlOffset) {
		//DEBUGGING
		g.setColor(Color.pink);
		g.drawRect((int)(hitbox.x - hitbox.width/2) - xLvlOffset,(int) (hitbox.y  - hitbox.height/2),(int) hitbox.width,(int) hitbox.height);
	}

	public int getObjType() {
		return objType;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	public void setAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}
}
