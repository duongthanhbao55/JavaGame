package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	public static int MENU_1 = 0;
	public static int LEVEL_1 = 1;
	public static int LEVEL_2 = 2;
	
	public static int DIE = 0;
	public static int JUMP = 1;
	public static int GAMEOVER = 2;
	public static int ATTACK_ONE = 3;
	public static int ATTACK_TWO = 4;
	public static int ATTACK_THREE = 5;
	
	private Clip[] songs, effects;
	private int currSongId;
	private float volume = 0.5f;
	private boolean songMute, effectMute;
	private Random rand = new Random();
	
	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU_1);
	}
	
	private void loadSongs() {
		String[] names = {"menu","Caves","Caves"};
		songs = new Clip[names.length];
		for(int i = 0; i < songs.length; i++) {
			songs[i] = getClip(names[i]);
		}
		updateSongVolume();
	}
	private void loadEffects() {
		String[] names = {"damaged3","jump1","gameover","attack1","attack2","attack3"};
		effects = new Clip[names.length];
		for(int i = 0; i < effects.length; i++) {
			effects[i] = getClip(names[i]);
		}
		
		updateEffectVolume();
	}
	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream audio;

		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();	

			c.open(audio);
			
			return c;
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
		
	}
	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectVolume();
	}
	public void stopSong() {
		if(songs[currSongId].isActive()) {
			songs[currSongId].stop();
		}
	}
	public void setLevelSong(int lvlIndex) {
		if(lvlIndex % 2 == 0) {
			playSong(LEVEL_1);
		}
		else
			playSong(LEVEL_2);
	}
	public void lvlCompleted() {
		stopSong();
		//playEffect(LVL_COMPLETED);
	}
	public void playAttackSound() {
		int start = 3;//potision of first string attack sound in variable names
		start += rand.nextInt(3);
		playEffect(start);
	}
	public void playEffect(int effect) {
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();
	}
	public void playSong(int song) {
		songs[currSongId].stop();
		
		currSongId = song;
		updateSongVolume();
		
		songs[currSongId].setMicrosecondPosition(0);// reset position of current sound
		songs[currSongId].loop(Clip.LOOP_CONTINUOUSLY);//loop background song until gameover
	}
	public void toggleSongMute() {
		this.songMute = !songMute;
		for(Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}
	public void toggleEffectMute() {
		this.effectMute = !effectMute;
		for(Clip c : effects) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(effectMute);
		}
	}
	private void updateSongVolume() {
		FloatControl gainControl = (FloatControl) songs[currSongId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();	
		float gain = (range * volume) + gainControl.getMinimum();
		System.out.println(gain);
		gainControl.setValue(gain);
	}
	private void updateEffectVolume() {
		for(Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}
}
