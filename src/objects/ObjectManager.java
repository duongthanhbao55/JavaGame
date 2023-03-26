package objects;

import java.awt.Graphics;
import java.util.ArrayList;

import static untilz.Constants.ObjectConstants.*;
import gamestates.Playing;

public class ObjectManager {
	
	private Playing playing;
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	
	
	public ObjectManager(Playing playing) {
		 this.playing = playing;
		 
		 potions.add(new Potion(300,200,HEAL_POTION));
		 potions.add(new Potion(400,200,MANA_POTION));
		 
		 containers.add(new GameContainer(500,200,BARREL));
		 containers.add(new GameContainer(600,200,BOX));
		 
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
	
}
