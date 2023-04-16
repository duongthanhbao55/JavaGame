package ui;

import static untilz.Constants.UI.PauseButton.SOUND_SIZE;
import static untilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static untilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import main.Game;

public class AudioOptions {

	private SoundButton musicButton, sfxButton;
	private VolumeButton volumeButton;
	private Game game;

	public AudioOptions(Game game) {
		this.game = game;
		initSoundButton();
		initVolumeButton();
	}

	private void initVolumeButton() {
		int vX = (int) (372 * Game.SCALE);
		int vY = (int) (303 * Game.SCALE);
		volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
	}

	private void initSoundButton() {
		int soundX = (int) (520 * Game.SCALE);
		int musicY = (int) (165 * Game.SCALE);
		int sfxY = (int) (211 * Game.SCALE);
		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
	}

	public void update() {
		// SOUND BUTTONS
		musicButton.update();
		sfxButton.update();

		// VOLUME BUTTONS
		volumeButton.update();
	}

	public void render(Graphics g) {
		// SOUND BUTTONS
		musicButton.render(g);
		sfxButton.render(g);

		// VOLUME BUTTONS
		volumeButton.render(g);
	}

	// FUNCTION

	public void mouseDragged(MouseEvent e) {
		if (volumeButton.isMousePressed()) {
			float valueBefore = volumeButton.getFloatValue();
			volumeButton.changeX(e.getX());
			float valueAfter = volumeButton.getFloatValue();
			if(valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMousePressed(true);

	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, musicButton)) {
			if (musicButton.isMousePressed()) {
				musicButton.setMute(!musicButton.isMute());// if mute on this function set its off and opposite
				game.getAudioPlayer().toggleSongMute();
			}
				
		} else if (isIn(e, sfxButton)) {
			if (sfxButton.isMousePressed()) {
				sfxButton.setMute(!sfxButton.isMute());
				game.getAudioPlayer().toggleEffectMute();
			}
				
		}

		musicButton.resetBools();
		sfxButton.resetBools();
		volumeButton.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		volumeButton.setMouseOver(false);

		if (isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMouseOver(true);

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}
}
