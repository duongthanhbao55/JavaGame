package Map;


import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import entities.NightBorne;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;


public class PhysicalMap 
{
	//VARIABLE
	Game game;
	
	public ArrayList<TileLayer> mapLayer = new ArrayList<TileLayer>();
	private ArrayList<NightBorne> nightBornes = new ArrayList<>();
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<Spike> spikes = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<Cannon> cannons = new ArrayList<>();
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;
	
	private ArrayList<Integer> collisionIndex = new ArrayList<>();
	
	
	//CONSTRUCTOR
	public PhysicalMap()
	{
		mapLayer = new ArrayList<TileLayer>();
		
	}
	public PhysicalMap(Game game)
	{
		this.game = game;		
		mapLayer = new ArrayList<TileLayer>();
	}
	
	
	//COPY CONSTRUCTOR
	public PhysicalMap(PhysicalMap physmap)
	{
		for(TileLayer layer : physmap.mapLayer)
		{
			this.mapLayer.add(new TileLayer(layer));
			
		}
		for(NightBorne nightBorne : physmap.getNightBornes())
		{
			this.nightBornes.add(nightBorne);
		}
		for(Potion p : physmap.getPotions())
		{
			this.potions.add(p);
		}
		for(GameContainer c : physmap.getContainers())
		{
			this.containers.add(c);
		}
		for(Spike s : physmap.getSpikes()) {
			this.spikes.add(s);
		}
		for(Cannon c : physmap.getCannons()) {
			this.cannons.add(c);
		}
		physmap.calcLvlOffsets();
		this.maxLvlOffsetX = physmap.getLvlOffset();
		this.lvlTilesWide = physmap.getLvlTilesWide();
		this.playerSpawn = physmap.getPlayerSpawn();
		
	}
	
	

	public void setNightBornes(ArrayList<NightBorne> nightBornes) {
		this.nightBornes = nightBornes;
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
	public void loadEnenmies(ArrayList<NightBorne> nightBorneList) {
		this.nightBornes = nightBorneList;
	}
	//UPDATE
	
	public void Update()
	{
		for(TileLayer layer : mapLayer)
		{
			layer.Update();
		}
	}
	
	
	//RENDER
	
	public void Render(Graphics g, int xLvlOffset)
	{    
		for(TileLayer layer : mapLayer)
		{
			layer.Render(g, xLvlOffset);
		}
	}
	
	//GETTER VS SETTER
	public ArrayList<TileLayer> getMapLayer() 
	{
		return mapLayer;
	}
	public void setMapLayer(ArrayList<TileLayer> mapLayer) {
		this.mapLayer = mapLayer;
	}
	
	public int getLvlOffset() {
		return maxLvlOffsetX;
	}
	public ArrayList<NightBorne> getNightBornes(){
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
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	public void setPlayerSpawn(Point playerSpawn) {
		this.playerSpawn = playerSpawn;
	}
	public void addPlayerSpawn(Point playerSpawn) {
		this.playerSpawn = playerSpawn;
	}
	public ArrayList<Cannon> getCannons() {
		return cannons;
	}
	public void setCannons(ArrayList<Cannon> cannons) {
		this.cannons = cannons;
	}

}
