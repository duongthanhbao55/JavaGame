
package ui;

import static untilz.Constants.UI.UrmButton.URM_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import untilz.LoadSave;

public class GameOverOverlay {
	private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	private UrmButton menu, play;
	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		createImage();
		createButtons();
	}
	
	private void createButtons() {
		int menuX = (int)(400 * Game.SCALE);
		int playX = (int)(505 * Game.SCALE);
		int y = (int)(195 * Game.SCALE);
		play = new UrmButton(playX, y,URM_SIZE, URM_SIZE,0);
		menu = new UrmButton(menuX, y,URM_SIZE, URM_SIZE,2);	
	}

	private void createImage() {
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgW = (int)(img.getWidth() * Game.SCALE);
		imgH = (int)(img.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int)(100 * Game.SCALE);
		
	}
	public void update() {
		play.update();
		menu.update();
	}
	public void Draw(Graphics g) {
		g.setColor(new Color(0,0,0,200));
		g.fillRect(0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT);
		
		g.drawImage(img, imgX, imgY, imgW, imgH, null);
		
		menu.render(g);
		play.render(g);
//		g.setColor(Color.white);
//		g.drawString("GameOver", Game.GAME_WIDTH, Game.GAME_HEIGHT);
//		g.drawString("Press ESC to enter main Menu!",Game.GAME_WIDTH/2,300);
	}
	public void keyPressed(KeyEvent e) {

	}
	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
	public void MouseMoved(MouseEvent e) {
		play.setMouseOver(false);
		menu.setMouseOver(false);
		
		if(isIn(menu, e))
			menu.setMouseOver(true);
		else if(isIn(play, e))
			play.setMouseOver(true);	
	}
	public void MouseRelease(MouseEvent e) {
		if(isIn(menu, e)) {
			if( menu.isMousePressed()) {
				playing.resetAll();
				playing.setGameState(Gamestate.MENU);
			}
				
		}
		else if(isIn(play, e))
			if(play.isMousePressed()) {
				playing.resetAll();
				playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
			}
				
		
		menu.resetBools();
		play.resetBools();
	}
	public void MousePressed(MouseEvent e) {
		if(isIn(menu, e))
			menu.setMousePressed(true);
		else if(isIn(play, e))
			play.setMousePressed(true);
	}
}
