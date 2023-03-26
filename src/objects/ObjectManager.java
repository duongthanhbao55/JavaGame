package objects;

import java.util.ArrayList;

import Effect.Animation;
import Load.CacheDataLoader;
import gamestates.Playing;

public class ObjectManager {
	
	private Playing playing;
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	
	
	public ObjectManager(Playing playing) {
		 this.playing = playing;
	}
	
	
}
