package objects;

import java.awt.Graphics;
import java.util.ArrayList;

import Map.PhysicalMap;

import static untilz.Constants.ObjectConstants.*;
import gamestates.Playing;

public class ObjectManager {
	
	private Playing playing;
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	
	
	public ObjectManager(Playing playing) {
		 this.playing = playing;		 
	}
	public void update(long currTime) {
		for(Potion p : potions) {
			if(p.isActive())
				p.update(currTime);
		}
		for(GameContainer gc : containers) {
			if(gc.active)
				gc.update(currTime);
		}
	}
	public void render(Graphics g, int xLvlOffset) {
		System.out.println(this.containers.size());
		drawPotions(g,xLvlOffset);
		drawContainers(g,xLvlOffset);
	}
	private void drawContainers(Graphics g, int xLvlOffset) {
		for(GameContainer gc : containers) {
			if(gc.active)
				gc.render(g,xLvlOffset);
		}
	}
	private void drawPotions(Graphics g, int xLvlOffset) {
		for(Potion p : potions) {
			if(p.active) {
				p.render(g,xLvlOffset);
			}
				
		}
		
	}
	public void loadObject(PhysicalMap physicalMap) {
		loadPotion(physicalMap);
		loadContainer(physicalMap);
	}
	public void loadPotion(PhysicalMap physicalMap) {
		this.potions = physicalMap.getPotions();
	}
	public void loadContainer(PhysicalMap physicalMap) {
		this.containers = physicalMap.getContainers();
	}
	
}
