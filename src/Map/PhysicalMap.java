package Map;


import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import entities.NightBorne;
import main.Game;


public class PhysicalMap 
{
	//VARIABLE
	Game game;
	
	public ArrayList<TileLayer> mapLayer = new ArrayList<TileLayer>();
	private ArrayList<NightBorne> nightBornes = new ArrayList<>();
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
	
	public void addCollisionIndex(int index) {
		collisionIndex.add(index);
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
}
