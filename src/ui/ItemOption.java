package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.Game;
import untilz.LoadSave;
import static untilz.HelpMethods.nextInt;

public class ItemOption {
	private BufferedImage optionBackground;
	private BufferedImage Pointer;

	private int xPos, yPos, width, height;
	private int pXPos, pYPos, pWidth, pHeight;
	private int index = 0;
	private int countOption = 0;
	private int indexPos;
	private String[] text;
	private int xTextOffset;
	private int yTextOffset;
	
	private boolean isShow = false;
	private boolean isEquip = false;
	private boolean isDrop = false;
	private boolean isSell = false;
	private boolean isUse = false;
	private boolean isUnequip = false;

	public ItemOption(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.pXPos = (int) (xPos + 2 * Game.SCALE);
		this.pYPos = (int) (yPos + (width / 3 - 14) / 2);
		this.pWidth = (int) (14 * Game.SCALE);
		this.pHeight = (int) (12 * Game.SCALE);
		this.indexPos = this.pYPos;
		this.xTextOffset = (int) (3 * Game.SCALE);
		this.yTextOffset = (int) (2 * Game.SCALE);
		loadImgs();
	}

	private void loadImgs() {
		optionBackground = LoadSave.GetSpriteAtlas(LoadSave.OPTION_ITEM_BG);
		Pointer = LoadSave.GetSpriteAtlas(LoadSave.POINTER);
	}

	public void update() {

	}

	public void render(Graphics g) {
		g.drawImage(optionBackground, xPos, yPos, (int) (width * Game.SCALE + 20 * Game.SCALE), (int) (height * Game.SCALE), null);
		g.drawImage(Pointer, pXPos, indexPos, pWidth, pHeight, null);
	
		for (int i = 0; i < text.length; i++) {
			if (index == i) {
				g.setColor(new Color(0, 155, 165, 200));
			}else {
				g.setColor(new Color(0, 0, 0, 200));
			}
			g.drawString(text[i], pXPos + pWidth + xTextOffset, pYPos + (i + 1) * width / 3 - yTextOffset);
		}
	}

	public void keyPressed(KeyEvent e) {
		resetBoolean();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			index--;
			if (index == -1) {
				index = countOption - 1;
			}
			indexPos = pYPos + index * width / 3;
			break;
		case KeyEvent.VK_DOWN:
			index++;
			if (index == countOption) {
				index = 0;
			}
			indexPos = pYPos + index * width / 3;
			break;
		case KeyEvent.VK_ENTER:
			if(index == 0 && text[index].equals("equip")) {
				this.isEquip = true;
			}
			if(index == 0 && text[index].equals("unequip")){
				this.isUnequip = true;
			}
			if(index == 0 && text[index].equals("use")) {
				this.isUse = true;
			}
			if(index == 2 && text[index].equals("sell")) {
				this.isSell = true;
			}
			if(index == 1 && text[index].equals("drop")) {
				this.isDrop = true;
			}
			break;
		}
	}
	public void setText(String[] text) {
		this.countOption = text.length;
		this.text = text;
	}
	public int getIndex() {
		return this.index;
	}
	public String getText() {
		return this.text[index];
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public boolean isEquip() {
		return isEquip;
	}

	public void setEquip(boolean isEquip) {
		this.isEquip = isEquip;
	}

	public boolean isDrop() {
		return isDrop;
	}

	public void setDrop(boolean isDrop) {
		this.isDrop = isDrop;
	}

	public boolean isSell() {
		return isSell;
	}

	public void setSell(boolean isSell) {
		this.isSell = isSell;
	}

	public boolean isUse() {
		return isUse;
	}

	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}
	public boolean isUnequip() {
		return this.isUnequip;
	}
	public void setCountOption(int countOption) {
		this.countOption = countOption;
	}
	public void resetBoolean() {
		isDrop = false;
		isEquip = false;
		isSell = false;
		isShow = false;
		isUnequip = false;
		isUse = false;
	}
}
