package ui;

import static untilz.Constants.UI.Button.B_HEIGHT;
import static untilz.Constants.UI.Button.B_HEIGHT_DEFAULT;
import static untilz.Constants.UI.Button.B_WIDTH;
import static untilz.Constants.UI.Button.B_WIDTH_DEFAULT;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import untilz.LoadSave;

public class LoginButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = B_WIDTH / 2;
	private Gamestate state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;
	
	
	

	//CONSTRUCTOR
	public LoginButton(int xPos, int yPos, int rowIndex,Gamestate state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImgs();
		initBounds();
	}

	

	//FUNCTION
	private void loadImgs() {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.LOGIN_BUTTON);
		for(int i = 0; i < imgs.length; i++) {
			imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT,rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
		}
		
	}
	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos,B_WIDTH,B_HEIGHT);
	}
	
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	
	//RENDER
	public void render(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT,null);
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
	public void applyGamestate() {
		Gamestate.state = state;
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	public Gamestate getState() {
		return state;
	}
}
