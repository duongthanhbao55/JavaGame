package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import untilz.LoadSave;
import static untilz.Constants.UI.UrmButton.*;

public class LevelCompleteOverlay {
	private Playing playing;
	private UrmButton menu, next;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;
	
	public LevelCompleteOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	private void initButtons() {
		int menuX = (int)(395 * Game.SCALE);
		int nextX = (int)(510 * Game.SCALE);
		int y = (int)(195 * Game.SCALE);
		next = new UrmButton(nextX, y,URM_SIZE, URM_SIZE,0);
		menu = new UrmButton(menuX, y,URM_SIZE, URM_SIZE,2);
		
	}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
		bgW = (int)(img.getWidth() * Game.SCALE);
		bgH = (int)(img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW/2;
		bgY = (int)(75 * Game.SCALE);
		
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		next.render(g);
		menu.render(g);	
	}
	
	public void update() {
		next.update();
		menu.update();
	}
	
	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
	public void MouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);
		
		if(isIn(menu, e))
			menu.setMouseOver(true);
		else if(isIn(next, e))
			next.setMouseOver(true);	
	}
	public void MouseRelease(MouseEvent e) {
		if(isIn(menu, e)) {
			if( menu.isMousePressed()) {
				playing.resetAll();
				Gamestate.state = Gamestate.MENU;
			}
				
		}
		else if(isIn(next, e))
			if(next.isMousePressed())
				playing.loadNextLevel();
		
		menu.resetBools();
		next.resetBools();
	}
	public void MousePressed(MouseEvent e) {
		if(isIn(menu, e))
			menu.setMousePressed(true);
		else if(isIn(next, e))
			next.setMousePressed(true);
	}
	
	
}
