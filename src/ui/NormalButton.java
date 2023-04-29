package ui;

import static untilz.Constants.UI.PauseButton.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import untilz.LoadSave;

public class NormalButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = SOUND_SIZE / 2;
	private byte state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;
	private float scale = 0f;
	
	

	//CONSTRUCTOR
	public NormalButton(int xPos, int yPos, int rowIndex,float scale, byte state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		this.scale = scale;
		loadImgs();
		initBounds();
	}

	

	//FUNCTION
	private void loadImgs() {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.DEFAULT_BUTTON);
		for(int i = 0; i < imgs.length; i++) {
			imgs[i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT,rowIndex * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
		}
		
	}
	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos,(int)(SOUND_SIZE * scale),(int)(SOUND_SIZE * scale));
	}
	
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	
	//RENDER
	public void render(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos,(int)(SOUND_SIZE * scale),(int)(SOUND_SIZE * scale),null);
	}
	
	//UPDATE
	public void update() {
		index = 0;
		if(mouseOver) {
			index = 1;
		}
		if(mousePressed) {
			index =2;
		}
			
	}
	
	
	//GETTER VS SETTER

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	public void applyTask() {
		//do something
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	public byte getState() {
		return state;
	}
}

