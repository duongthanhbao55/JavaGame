package ui;

import static untilz.Constants.UI.PauseButton.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import main.Game;
import untilz.LoadSave;
import static untilz.Constants.UI.ConfirmButton.*;

public class NormalButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = CONFIRM_BUTTON_SIZE / 2;
	private byte state;
	private String text;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;
	private float scale = 0f;

	// CONSTRUCTOR
	public NormalButton(int xPos, int yPos, int rowIndex, float scale, byte state, String text) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		this.scale = scale;
		this.text = text;
		loadImgs();
		initBounds();
	}

	// FUNCTION
	private void loadImgs() {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CONFIRM_BUTTON);
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = temp.getSubimage(i * CONFIRM_BUTTON_SIZE_DEFAULT, rowIndex * CONFIRM_BUTTON_SIZE_DEFAULT,
					CONFIRM_BUTTON_SIZE_DEFAULT, CONFIRM_BUTTON_SIZE_DEFAULT);
		}

	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, (int) (CONFIRM_BUTTON_SIZE * scale),
				(int) (CONFIRM_BUTTON_SIZE * scale));
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	// RENDER
	public void render(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, (int) (CONFIRM_BUTTON_SIZE * scale),
				(int) (CONFIRM_BUTTON_SIZE * scale), null);
		g.setColor(new Color(0, 0, 0));
		int y = yPos + bounds.height / 2;
		int x = xPos + (int)(bounds.getWidth() - text.length()*13)/2;
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		if (index == 1)
			g.drawString(text, x, y + 5);
		else
			g.drawString(text, x, y);
	}

	// UPDATE
	public void update() {	
		index = 0;
		if (mouseOver) {
			index = 1;
		}
		if (mousePressed) {
			index = 2;
		}

	}

	// GETTER VS SETTER

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
		// do something
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public byte getState() {
		return state;
	}
}
