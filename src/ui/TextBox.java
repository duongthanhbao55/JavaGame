package ui;

import static untilz.Constants.UI.TextBox.TEXTBOX_WIDTH;
import static untilz.Constants.UI.TextBox.TEXTBOX_HEIGHT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import main.Game;
import untilz.LoadSave;

public class TextBox {
	private int x, y;
	private int index;
	private int xDrawOffset, yDrawOffset;
	private BufferedImage textBox;
	private Rectangle bounds;
	private boolean mouseOver, mousePressed;
	
	public TextBox(int x, int y,int index){
		this.x = x;
		this.y = y;
		this.index = index;
		xDrawOffset =(int)( 13 * Game.SCALE );
		yDrawOffset = (int)(7 * Game.SCALE);
		loadImgs();
		initBounds();
	}
	private void loadImgs() {
		
		textBox = LoadSave.GetSpriteAtlas(LoadSave.TEXT_BOX);
		
	}
	private void initBounds() {
		bounds = new Rectangle(x + xDrawOffset, y + yDrawOffset,(int)(TEXTBOX_WIDTH - xDrawOffset * Game.SCALE) - 10,(int)(TEXTBOX_HEIGHT - yDrawOffset * Game.SCALE) - 4);
	}
	
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	
	//RENDER
	public void render(Graphics g) {
		g.drawImage(textBox, x , y, TEXTBOX_WIDTH, TEXTBOX_HEIGHT,null);
		if(index == 1) {
			g.setColor(new Color(0,0,0,80));
			g.fillRect((int)(bounds.x + 4* Game.SCALE),(int)( bounds.y + 2 * Game.SCALE),(int)( bounds.width - 9 * Game.SCALE),(int)( bounds.height - 4 * Game.SCALE));
		}
	}
	
	//UPDATE
	public void update() {
		index = 0;
		if(mouseOver) {
			index = 1;
		}
		if(mousePressed) {
			index = 2;
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
	public Rectangle getBounds() {
		return this.bounds;
	}
}
