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
import database.MySQL;
import entities.Enemy;
import entities.NPC;
import entities.NPC_Wizard1;
import entities.NightBorne;
import gamestates.Gamestate;
import gamestates.Playing;
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
		loadEnemies();
		loadNpcs();

	}

	public void loadNextLevel() {

		// lvlIndex++;
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
		Rectangle2D.Float playerHitbox = game.getPlaying().getPlayer().getRealHitbox();
		for (int i = 0; i < levels.get(lvlIndex).getAreaSwitch().length; i++) {
			if (levels.get(lvlIndex).getAreaSwitch()[i].intersects(playerHitbox)) {
				levels.get(lvlIndex).setItem(ItemManager.getItems());
				game.getPlaying().getPlayer().setSpawn(levels.get(lvlIndex).getPlayerSpawn()[i]);
				lvlIndex = PhysicalMap.mapTemplate[lvlIndex].WmapID[i];
				game.getPlaying().getPlayer().setMapId(lvlIndex);
				loadNextLevel();
				game.getPlaying().loadNextLevel();
			}
		}
	}

	public void loadEnemies() {
		for (int i = 0; i < EnemyManager.arrMobStatus.length; i++) {
			int index = EnemyManager.arrMobStatus[i].mapID;
			int x = EnemyManager.arrMobStatus[i].mobX;
			int y = EnemyManager.arrMobStatus[i].mobY;
			boolean active = false;
			if (EnemyManager.arrMobStatus[i].mobStatus == 1) {
				active = true;
			}
			int currHealth = EnemyManager.arrMobStatus[i].health_point;
			levels.get(index).getNightBornes().add(new NightBorne(x, y, currHealth, active, game.getPlaying()));
		}
	}

	public void loadNpcs() {
		for (int i = 0; i < NPCManager.arrNpcStatus.length; i++) {
			int index = NPCManager.arrNpcStatus[i].mapID;
			int x = NPCManager.arrNpcStatus[i].npcX;
			int y = NPCManager.arrNpcStatus[i].npcY;
			String name = NPCManager.arrNpcStatus[i].name;
			int npcId = NPCManager.arrNpcStatus[i].npcID;
			levels.get(index).getNpcs().add(new NPC_Wizard1(x, y, 0, name, npcId));
			NPCManager.getAllNpc().addAll(levels.get(index).getNpcs());
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

	public void setLvlIndex(int lvlIndex) {
		this.lvlIndex = lvlIndex;
	}

	public ArrayList<PhysicalMap> getLevels() {
		return levels;
	}

	public static void saveMobStatus(Playing playing) {
		int i = 0;
		int index = 1;
		for (PhysicalMap pm : playing.getLevelManager().getLevels()) {
			for (NightBorne e : pm.getNightBornes()) {
				int active = 0;
				if (e.isActive()) {
					active = 1;
				}

				MySQL.saveMobStatus(index, (int) e.getHitbox().getX(), (int) e.getHitbox().getY(), active,
						e.getCurrHealth(), e.getRefreshTime(), (int) e.getDeadTime(), playing.getPlayer().getPlayerId(),
						e.getEnemyId(), i);
				index++;
			}
			i++;
		}

	}

	public static void saveNpcStatus(Playing playing) {
		int mapIndex = 0;
		for (PhysicalMap pm : playing.getLevelManager().getLevels()) {
			for (NPC n : pm.getNpcs()) {
				MySQL.saveNpcStatus((int) n.getHitbox().getX(), (int) n.getHitbox().getY(), mapIndex,
						playing.getPlayer().getPlayerId(), n.getNpcId());
			}
			mapIndex++;
		}
	}
}
