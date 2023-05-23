package Effect;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class VFX {
	protected int x, y, width, height; 
	protected Rectangle bounds;
	
	protected VFX(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	protected void update(long currTime) {
		
	}
	
	protected void render(Graphics g,int xLvlOffset) {
		
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public void initBounds() {
		this.bounds = new Rectangle(x,y,width,height);
	}
	
}
