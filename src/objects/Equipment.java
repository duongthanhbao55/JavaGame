package objects;

import static untilz.Constants.UI.EquipmentUI.*;
import static untilz.Constants.UI.Inventory.GRID_HEIGHT;
import static untilz.Constants.UI.Inventory.GRID_WIDTH;
import static untilz.Constants.UI.Inventory.INVENTORY_WIDTH;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import untilz.LoadSave;

import static untilz.Constants.EquipmentConstants.*;

public class Equipment {
	private Playing playing;
	private BufferedImage equipmentUI;
	private Slot[] Slots = new Slot[10];
	private Rectangle2D.Float Bag;
	public Equipment(Playing playing) {
		this.playing = playing;
		LoadImg();
		initBag();
	}
	
	private void LoadImg() {
		equipmentUI = LoadSave.GetSpriteAtlas(LoadSave.EQUIPMENT_BACKGROUND);
		
	}
	public void initBag() {
		Bag = new Rectangle2D.Float( Game.TILES_SIZE * 26, 0, EQUIPMENT_UI_WIDTH, EQUIPMENT_UI_HEIGHT);
		float width = GRID_WIDTH / 4;
		float height = GRID_HEIGHT / 6;
		Slots[HELMET] = new Slot((float) (Bag.getX() + HELMET_POSX), (float) (Bag.getY() + HELMET_POSY), width,
				height, true,true, null);
		
		Slots[ARMOR] = new Slot((float) (Bag.getX() + ARMOR_POSX), (float) (Bag.getY() + ARMOR_POSY), width,
				height, true,true, null);
		
		Slots[BELT] = new Slot((float) (Bag.getX() + BELT_POSX), (float) (Bag.getY() + BELT_POSY), width,
				height, true,true, null);
		
		Slots[BOOTS] = new Slot((float) (Bag.getX() + BOOTS_POSX), (float) (Bag.getY() + BOOTS_POSY), width,
				height, true,true, null);
		
		Slots[SHIELD] = new Slot((float) (Bag.getX() + SHIELD_POSX), (float) (Bag.getY() + SHIELD_POSY), width,
				height, true,true, null);
		
		Slots[NACKLACE] = new Slot((float) (Bag.getX() + NACKLACE_POSX), (float) (Bag.getY() + NACKLACE_POSY), width,
				height, true,true, null);
		
		Slots[SWORD] = new Slot((float) (Bag.getX() + SWORD_POSX), (float) (Bag.getY() + SWORD_POSY), width,
				height, true,true, null);
		
		Slots[RING1] = new Slot((float) (Bag.getX() + RING1_POSX), (float) (Bag.getY() + RING1_POSY), width,
				height, true,true, null);
		
		Slots[RING2] = new Slot((float) (Bag.getX() + RING2_POSX), (float) (Bag.getY() + RING2_POSY), width,
				height, true,true, null);
		
		Slots[RING3] = new Slot((float) (Bag.getX() + RING3_POSX), (float) (Bag.getY() + RING3_POSY), width,
				height, true,true, null);
		
		
	}
	public void update() {
		
	}
	public void render(Graphics g) {
		g.drawImage(equipmentUI, Game.TILES_SIZE * 26, 0, EQUIPMENT_UI_WIDTH, EQUIPMENT_UI_HEIGHT, null);
		for(Slot s : Slots) {
			s.render(g);
		}
	}
}
