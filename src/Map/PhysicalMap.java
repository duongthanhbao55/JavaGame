package Map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import Template.MapTemplate;
import database.MySQL;
import entities.NPC;
import entities.NPC_Wizard1;
import entities.NightBorne;
import gamestates.Playing;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Item;
import objects.Potion;
import objects.Spike;

public class PhysicalMap {
	// VARIABLE
	Game game;

	public static MapTemplate[] mapTemplate;
	public ArrayList<TileLayer> mapLayer = new ArrayList<TileLayer>();
	private ArrayList<NightBorne> nightBornes = new ArrayList<>();
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<Spike> spikes = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<Cannon> cannons = new ArrayList<>();
	private ArrayList<Item> items = new ArrayList<>();
	private ArrayList<NPC_Wizard1> npcs = new ArrayList<>();
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Rectangle2D.Float[] areaSwitchMap;
	private Point[] playerSpawn;

	private ArrayList<Integer> collisionIndex = new ArrayList<>();
	private int idMap;

	// CONSTRUCTOR
	public PhysicalMap() {
		mapLayer = new ArrayList<TileLayer>();

	}

	public PhysicalMap(Game game) {
		this.game = game;
		mapLayer = new ArrayList<TileLayer>();
	}

	// COPY CONSTRUCTOR
	public PhysicalMap(PhysicalMap physmap) {
		for (TileLayer layer : physmap.mapLayer) {
			this.mapLayer.add(new TileLayer(layer));
		}
		for (NightBorne nightBorne : physmap.getNightBornes()) {
			this.nightBornes.add(nightBorne);
		}
		for (Potion p : physmap.getPotions()) {
			this.potions.add(p);
		}
		for (GameContainer c : physmap.getContainers()) {
			this.containers.add(c);
		}
		for (Spike s : physmap.getSpikes()) {
			this.spikes.add(s);
		}
		for (Cannon c : physmap.getCannons()) {
			this.cannons.add(c);
		}
		physmap.calcLvlOffsets();
		this.maxLvlOffsetX = physmap.getLvlOffset();
		this.lvlTilesWide = physmap.getLvlTilesWide();
		this.playerSpawn = physmap.getPlayerSpawn();

	}

	public void loadAll(Playing playing, int idMap) {
		loadEnenmies(playing, idMap);
		loadPlayerSpawn();
		loadAreaSwitch();
		loadNpcs();
	}

	public void loadEnenmies(Playing playing, int idMap) {
		this.idMap = idMap;
		for (int i = 0; i < PhysicalMap.mapTemplate[idMap].mobID.length; i++) {
			nightBornes.add(new NightBorne(PhysicalMap.mapTemplate[idMap].mobX[i],
					PhysicalMap.mapTemplate[idMap].mobY[i], playing));
		}

	}

	public void loadAreaSwitch() {
		areaSwitchMap = new Rectangle2D.Float[PhysicalMap.mapTemplate[this.idMap].WgoX.length];
		for (int i = 0; i < areaSwitchMap.length; i++) {
			float xPos = PhysicalMap.mapTemplate[this.idMap].WgoX[i];
			float yPos = PhysicalMap.mapTemplate[this.idMap].WgoY[i];
			areaSwitchMap[i] = new Rectangle2D.Float(xPos, yPos, 4, 100);
		}
	}

	public void loadNpcs() {
		for (int i = 0; i < PhysicalMap.mapTemplate[idMap].npcID.length; i++) {
			float xPos = PhysicalMap.mapTemplate[this.idMap].npcX[i];
			float yPos = PhysicalMap.mapTemplate[this.idMap].npcY[i];
			int id = PhysicalMap.mapTemplate[this.idMap].npcID[i];
			npcs.add(new NPC_Wizard1(xPos, yPos, id));
		}
	}

	public void loadPlayerSpawn() {
		playerSpawn = new Point[PhysicalMap.mapTemplate[idMap].cSpawnX.length];
		for (int i = 0; i < PhysicalMap.mapTemplate[idMap].cSpawnX.length; i++) {
			float xPos = PhysicalMap.mapTemplate[this.idMap].cSpawnX[i];
			float yPos = PhysicalMap.mapTemplate[this.idMap].cSpawnY[i];
			playerSpawn[i] = new Point((int)xPos,(int)yPos);
		}
	}

	public void calcLvlOffsets() {
		lvlTilesWide = mapLayer.get(0).getTileMap()[0].length;
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
	}

	public int getLvlTilesWide() {
		return lvlTilesWide;
	}

	public void setLvlTilesWide(int lvlTilesWide) {
		this.lvlTilesWide = lvlTilesWide;
	}

	public void addEnenmies(ArrayList<NightBorne> nightBorneList) {
		this.nightBornes = nightBorneList;
	}
	// UPDATE

	public void Update() {
		for (TileLayer layer : mapLayer) {
			layer.Update();
		}

	}

	// RENDER

	public void Render(Graphics g, int xLvlOffset) {
		for (TileLayer layer : mapLayer) {
			layer.Render(g, xLvlOffset);
		}
		g.setColor(new Color(255, 0, 0));

		g.drawRect((int) areaSwitchMap[0].getBounds().getX() - xLvlOffset, (int) areaSwitchMap[0].getBounds().getY(),
				(int) areaSwitchMap[0].getBounds().getWidth(), (int) areaSwitchMap[0].getBounds().getHeight());
		g.drawRect((int) areaSwitchMap[1].getBounds().getX() - xLvlOffset, (int) areaSwitchMap[1].getBounds().getY(),
				(int) areaSwitchMap[1].getBounds().getWidth(), (int) areaSwitchMap[1].getBounds().getHeight());
	}

	// GETTER VS SETTER
	public ArrayList<TileLayer> getMapLayer() {
		return mapLayer;
	}

	public void setMapLayer(ArrayList<TileLayer> mapLayer) {
		this.mapLayer = mapLayer;
	}

	public int getLvlOffset() {
		return maxLvlOffsetX;
	}

	public ArrayList<NightBorne> getNightBornes() {
		return nightBornes;
	}

	public ArrayList<Potion> getPotions() {
		return potions;
	}

	public void setPotions(ArrayList<Potion> potions) {
		this.potions = potions;
	}

	public ArrayList<GameContainer> getContainers() {
		return containers;
	}

	public void setContainers(ArrayList<GameContainer> containers) {
		this.containers = containers;
	}

	public void setSpikes(ArrayList<Spike> spikes) {
		this.spikes = spikes;
	}

	public ArrayList<Spike> getSpikes() {
		return spikes;
	}

	public ArrayList<Integer> getCollisionIndex() {
		return collisionIndex;
	}

	public Point[] getPlayerSpawn() {
		return playerSpawn;
	}

	public void setPlayerSpawn(Point[] playerSpawn) {
		this.playerSpawn = playerSpawn;
	}


	public ArrayList<Cannon> getCannons() {
		return cannons;
	}

	public void setItem(ArrayList<Item> items) {
		this.items = items;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public ArrayList<NPC_Wizard1> getNpcs() {
		return npcs;
	}

	public void setCannons(ArrayList<Cannon> cannons) {
		this.cannons = cannons;
	}

	public Rectangle2D.Float[] getAreaSwitch() {
		return this.areaSwitchMap;
	}

	public static void loadMapData() {
		try {
			MapTemplate[] _mapTemplates = new MapTemplate[0];
			final MySQL mySQL = new MySQL(0);

			ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `maptemplate`;");
			if (res.last()) {
				_mapTemplates = new MapTemplate[res.getRow()];
				res.beforeFirst();
			}
			int i = 0;
			while (res.next()) {
				final MapTemplate mapTemplate = new MapTemplate();
				mapTemplate.mapID = res.getShort("id");
				mapTemplate.mapName = res.getString("mapName");
				mapTemplate.mapDescription = res.getString("mapDescription");
				final JSONArray WgoX = (JSONArray) JSONValue.parse(res.getString("WgoX"));
				final JSONArray WgoY = (JSONArray) JSONValue.parse(res.getString("WgoY"));
				final JSONArray cSpawnX = (JSONArray) JSONValue.parse(res.getString("cSpawnX"));
				final JSONArray cSpawnY = (JSONArray) JSONValue.parse(res.getString("cSpawnY"));
				final JSONArray WmapID = (JSONArray) JSONValue.parse(res.getString("WmapID"));
				mapTemplate.WgoX = new short[WgoX.size()];
				mapTemplate.WgoY = new short[mapTemplate.WgoX.length];
				mapTemplate.cSpawnX = new short[mapTemplate.WgoX.length];
				mapTemplate.cSpawnY = new short[mapTemplate.WgoX.length];
				mapTemplate.WmapID = new short[mapTemplate.WgoX.length];

				for (int j5 = 0; j5 < mapTemplate.WgoX.length; ++j5) {
					mapTemplate.WgoX[j5] = Short.parseShort(WgoX.get(j5).toString());
					mapTemplate.WgoY[j5] = Short.parseShort(WgoY.get(j5).toString());
					mapTemplate.cSpawnX[j5] = Short.parseShort(cSpawnX.get(j5).toString());
					mapTemplate.cSpawnY[j5] = Short.parseShort(cSpawnY.get(j5).toString());
					mapTemplate.WmapID[j5] = Short.parseShort(WmapID.get(j5).toString());
				}
				final JSONArray jarrX = (JSONArray) JSONValue.parse(res.getString("npcX"));
				final JSONArray jarrY = (JSONArray) JSONValue.parse(res.getString("npcY"));
				final JSONArray jarrID = (JSONArray) JSONValue.parse(res.getString("npcID"));
				mapTemplate.npcX = new short[jarrX.size()];
				mapTemplate.npcY = new short[mapTemplate.npcX.length];
				mapTemplate.npcID = new byte[mapTemplate.npcX.length];

				for (int j6 = 0; j6 < mapTemplate.npcX.length; ++j6) {
					mapTemplate.npcX[j6] = Short.parseShort(jarrX.get(j6).toString());
					mapTemplate.npcY[j6] = Short.parseShort(jarrY.get(j6).toString());
					mapTemplate.npcID[j6] = Byte.parseByte(jarrID.get(j6).toString());
				}
				final JSONArray mobID = (JSONArray) JSONValue.parse(res.getString("mobID"));
				final JSONArray mobLevel = (JSONArray) JSONValue.parse(res.getString("mobLevel"));
				final JSONArray mobX = (JSONArray) JSONValue.parse(res.getString("mobX"));
				final JSONArray mobY = (JSONArray) JSONValue.parse(res.getString("mobY"));
				final JSONArray mobStatus = (JSONArray) JSONValue.parse(res.getString("mobStatus"));
				final JSONArray mobRefreshTime = (JSONArray) JSONValue.parse(res.getString("mobRefreshTime"));
				mapTemplate.mobID = new int[mobID.size()];
				mapTemplate.mobLevel = new short[mapTemplate.mobID.length];
				mapTemplate.mobX = new short[mapTemplate.mobID.length];
				mapTemplate.mobY = new short[mapTemplate.mobID.length];
				mapTemplate.mobStatus = new byte[mapTemplate.mobID.length];
				mapTemplate.mobRefreshTime = new int[mapTemplate.mobID.length];
				for (int j7 = 0; j7 < mapTemplate.mobID.length; ++j7) {
					mapTemplate.mobID[j7] = Integer.parseInt(mobID.get(j7).toString());
					mapTemplate.mobLevel[j7] = Short.parseShort(mobLevel.get(j7).toString());
					mapTemplate.mobX[j7] = Short.parseShort(mobX.get(j7).toString());
					mapTemplate.mobY[j7] = Short.parseShort(mobY.get(j7).toString());
					mapTemplate.mobStatus[j7] = Byte.parseByte(mobStatus.get(j7).toString());
					mapTemplate.mobRefreshTime[j7] = Integer.parseInt(mobRefreshTime.get(j7).toString());
				}

				mapTemplate.bgID = res.getByte("bgID");
				_mapTemplates[i] = mapTemplate;
				++i;
			}
			res.close();
			mapTemplate = _mapTemplates;

		} catch (SQLException | NumberFormatException e) {
			throw new RuntimeException(e);
		}
	}

}
