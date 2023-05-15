package Level;

import static Load.CacheDataLoader.MAP_1;
import static Load.CacheDataLoader.MAP_2;
import static Load.CacheDataLoader.MAP_3;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Load.CacheDataLoader;
import Map.PhysicalMap;
import database.ItemManager;
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
		levels.get(0).loadAll(game.getPlaying(), 0);
		levels.get(1).loadAll(game.getPlaying(), 1);
		levels.get(2).loadAll(game.getPlaying(), 2);
	}

	public void loadNextLevel() {
		levels.get(lvlIndex).setItem(ItemManager.getItems());
		//lvlIndex++;
		if (lvlIndex >= levels.size()) {
			lvlIndex = 0;
			Gamestate.state = Gamestate.MENU;
		}
		loadNextMapData();
	}

	private void loadNextMapData() {
		PhysicalMap newLevel = levels.get(lvlIndex);
		game.getPlaying().getPlayer().LoadLvlData(newLevel.getMapLayer().get(0).getTileMap());
		game.getPlaying().getEnemyManager().loadEnimies(newLevel);
		game.getPlaying().setMaxLvlOffsetX(newLevel.getLvlOffset());
		game.getPlaying().getObjectManager().loadObject(newLevel);
		game.getPlaying().getItemManager().loadItem(newLevel);
		game.getPlaying().getNpcManager().loadNpcs(newLevel);
		game.getPlaying().initTask();
	}

	public void render(Graphics g, int lvlOffset) {

	}

	public void update() {
		Rectangle2D.Float playerHitbox = game.getPlaying().getPlayer().getHitbox();
		for (int i = 0; i < levels.get(lvlIndex).getAreaSwitch().length; i++) {
			if (levels.get(lvlIndex).getAreaSwitch()[i].intersects(playerHitbox)) {
					lvlIndex = PhysicalMap.mapTemplate[lvlIndex].WmapID[i];	
					loadNextLevel();
					game.getPlaying().loadNextLevel();
			}
		}
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
