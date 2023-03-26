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
	private SoundButton musicButton, sfxButton;
	private UrmButton menuB, replayB, unpauseB;
	private VolumeButton volumeButton;


	// CONSRUCTOR
	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadBackground();
		initSoundButton();
		initUrmButton();
		initVolumeButton();
	}

	private void initVolumeButton() {
		int vX = (int)(372 * Game.SCALE);
		int vY = (int)(303 * Game.SCALE);
		volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH,VOLUME_HEIGHT);
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

	private void initSoundButton() {
		int soundX = (int) (520 * Game.SCALE);
		int musicY = (int) (165 * Game.SCALE);
		int sfxY = (int) (211 * Game.SCALE);
		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
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
		// SOUND BUTTONS
		musicButton.update();
		sfxButton.update();

		// URM BUTTONS
		menuB.update();
		replayB.update();
		unpauseB.update();
		
		// VOLUME BUTTONS
		volumeButton.update();
	}

	// RENDER
	public void render(Graphics g) {
		// BACKGROUND
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(background, bgX, bgY, bgWidth, bgHeight, null);

		// SOUND BUTTONS
		musicButton.render(g);
		sfxButton.render(g);
		// URM BUTTONS
		menuB.render(g);
		replayB.render(g);
		unpauseB.render(g);
		
		//VOLUME BUTTONS
		volumeButton.render(g);
	}

	// FUNCTIOn
	public void mouseMoved() {

	}

	public void mouseDragged(MouseEvent e) {
		if(volumeButton.isMousePressed()) {
			volumeButton.changeX(e.getX());
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if (isIn(e, menuB))
			menuB.setMousePressed(true);
		else if (isIn(e, replayB))
			replayB.setMousePressed(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMousePressed(true);

	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, musicButton)) {
			if (musicButton.isMousePressed())
				musicButton.setMute(!musicButton.isMute());// if mute on this function set its off and opposite
		} else if (isIn(e, sfxButton)) {
			if (sfxButton.isMousePressed())
				sfxButton.setMute(!sfxButton.isMute());
		} else if (isIn(e, menuB)) {
			if (menuB.isMousePressed())
				Gamestate.state = Gamestate.MENU;
				playing.unpauseGame();
		}else if (isIn(e, replayB)) {
			if (replayB.isMousePressed())
				System.out.println("Replay Level");
		}else if (isIn(e, unpauseB)) {
			if (unpauseB.isMousePressed())
				playing.unpauseGame();
		}

		musicButton.resetBools();
		sfxButton.resetBools();
		menuB.resetBools();
		replayB.resetBools();
		unpauseB.resetBools();
		volumeButton.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);
		volumeButton.setMouseOver(false);
		
		
		
		if (isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else if (isIn(e, replayB))
			replayB.setMouseOver(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMouseOver(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMouseOver(true);

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}

}
