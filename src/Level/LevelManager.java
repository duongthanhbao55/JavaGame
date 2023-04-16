package Level;

import static Load.CacheDataLoader.MAP_1;
import static Load.CacheDataLoader.MAP_2;
import static Load.CacheDataLoader.MAP_3;

import java.awt.Graphics;
import java.util.ArrayList;

import Load.CacheDataLoader;
import Map.PhysicalMap;
import gamestates.Gamestate;
import main.Game;

public class LevelManager {
	private Game game;
	private ArrayList<PhysicalMap> levels;
	private int lvlIndex = 0;
	
	public LevelManager(Game game) {
		this.game = game;
		levels = new ArrayList<>();
		buildAllLevels();
	}

	private void buildAllLevels() {
		levels.add(CacheDataLoader.getInstance().getPhyscialMap(MAP_1));
		levels.add(CacheDataLoader.getInstance().getPhyscialMap(MAP_2));
		levels.add(CacheDataLoader.getInstance().getPhyscialMap(MAP_3));
	}
	public void loadNextLevel() {
		lvlIndex++;
		if(lvlIndex >= levels.size()) {
			lvlIndex = 0;
			Gamestate.state = Gamestate.MENU;
		}
		
		PhysicalMap newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnimies(newLevel);
		game.getPlaying().getPlayer().LoadLvlData(newLevel.getMapLayer().get(0).getTileMap());
		game.getPlaying().setMaxLvlOffsetX(newLevel.getLvlOffset());
		game.getPlaying().getObjectManager().loadObject(newLevel);
	}
	public void render(Graphics g,int lvlOffset) {
	
	}
	public void update() {
	
	}
	public PhysicalMap getCurrLevel() {
		return levels.get(lvlIndex);
	}
	public int getAmountOfLevels() {
		return levels.size();
	}
	public int getLvlIndex() {
		return lvlIndex;
	}
}
