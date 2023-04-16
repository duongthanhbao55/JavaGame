package ui;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import untilz.LoadSave;
import static untilz.Constants.UI.PauseButton.*;
import static untilz.Constants.UI.UrmButton.*;
import static untilz.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

	// VARIABLE
	private Playing playing;
	private BufferedImage background;
	private int bgX, bgY, bgWidth, bgHeight;
	private AudioOptions audioOptions;
	private UrmButton menuB, replayB, unpauseB;



	// CONSRUCTOR
	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadBackground();
		audioOptions = playing.getGame().getAudioOptions();
		initUrmButton();
		
	}


	private void initUrmButton() {
		int menuX = (int) (378 * Game.SCALE);
		int replayX = (int) (452 * Game.SCALE);
		int unpauseX = (int) (527 * Game.SCALE);
		int bY = (int) (350 * Game.SCALE);

		menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
		replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
		unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);

	}



	private void loadBackground() {
		background = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgWidth = (int) (background.getWidth() * Game.SCALE);
		bgHeight = (int) (background.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
		bgY = (int) (50 * Game.SCALE);

	}

	// UPDATE
	public void update() {

		

		// URM BUTTONS
		menuB.update();
		replayB.update();
		unpauseB.update();
		audioOptions.update();
		
	
	}

	// RENDER
	public void render(Graphics g) {
		// BACKGROUND
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(background, bgX, bgY, bgWidth, bgHeight, null);


		// URM BUTTONS
		menuB.render(g);
		replayB.render(g);
		unpauseB.render(g);
		audioOptions.render(g);
		
	}

	// FUNCTIOn
	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {

		if (isIn(e, menuB))
			menuB.setMousePressed(true);
		else if (isIn(e, replayB))
			replayB.setMousePressed(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		else 
			audioOptions.mousePressed(e);

	}

	public void mouseReleased(MouseEvent e) {
	if (isIn(e, menuB)) {
			if (menuB.isMousePressed())
				Gamestate.state = Gamestate.MENU;
				playing.unpauseGame();
		}else if (isIn(e, replayB)) {
			if (replayB.isMousePressed()) {
				playing.resetAll();
				playing.unpauseGame();
			}
		}else if (isIn(e, unpauseB)) {
			if (unpauseB.isMousePressed())
				playing.unpauseGame();
		}else
			audioOptions.mouseReleased(e);

		menuB.resetBools();
		replayB.resetBools();
		unpauseB.resetBools();

	}

	public void mouseMoved(MouseEvent e) {

		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);
			
		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else if (isIn(e, replayB))
			replayB.setMouseOver(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMouseOver(true);
		else 
			audioOptions.mouseMoved(e);
		
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}

}
