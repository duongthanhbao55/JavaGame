package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import untilz.LoadSave;

import static untilz.Constants.UI.Message.MESSAGE_WIDTH;
import static untilz.Constants.UI.Message.MESSAGE_HEIGHT;

public class MessageIcon {
	private BufferedImage[] messageIcon;

	private int x,y;
	private int type;
	public MessageIcon(int x, int y, int type){
		this.x = x;
		this.y = y;
		this.type = type;
		loadImg();
	}
	private void loadImg(){
		messageIcon = new BufferedImage[2];
		messageIcon[0] = LoadSave.GetSpriteAtlas(LoadSave.MESSAGE_EXCLAMATION);
		messageIcon[1] = LoadSave.GetSpriteAtlas(LoadSave.MESSAGE_QUESTION);
	}
	public void update() {
		
	}
	public void render(Graphics g,int xLvlOffset) {
		g.drawImage(messageIcon[type], x - xLvlOffset, y, MESSAGE_WIDTH, MESSAGE_HEIGHT, null);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
