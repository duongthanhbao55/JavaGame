package objects;

import Template.ItemTemplate;

import gamestates.Playing;
import main.Game;
import untilz.LoadSave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static untilz.Constants.UI.Inventory.*;

public class InventoryManager {
    private Playing playing;
    private static ArrayList<Item> items = new ArrayList<Item>();
    private BufferedImage InventoryImg;
    private Rectangle2D.Float Bag;
    private boolean isOpen = false;

    public InventoryManager(Playing playing) {
        this.playing = playing;
        LoadImg();
        initBag();
    }//175
    public void initBag() {
    	Bag = new Rectangle2D.Float(Game.TILES_SIZE * 47 + (INVENTORY_WIDTH - GRID_WIDTH)/2,1*Game.TILES_SIZE - 5*Game.SCALE,
    								GRID_WIDTH,GRID_HEIGHT);
    }
    public void LoadImg() {
    	InventoryImg = LoadSave.GetSpriteAtlas(LoadSave.INVENTORY_BACKGROUND);
    }
    public void add(Item i){
        items.add(i);
    }
    public void render(Graphics g) {
    	g.drawImage(InventoryImg,Game.TILES_SIZE * 47 , 0,INVENTORY_WIDTH,INVENTORY_HEIGHT, null);
    	g.setColor(new Color(255,0,0));
    	g.drawRect((int)Bag.getX(), (int)Bag.getY(), (int)Bag.getWidth(), (int)Bag.getHeight());
    	for(Item i : items) {
    		i.renderIcon(g, Game.TILES_SIZE * 48, Game.TILES_SIZE * 1, 2);
    	}
    }
    public void update() {
    	
    }
    public void setOpen(boolean isOpen) {
    	this.isOpen = isOpen;
    }
    public boolean isOpen() {
    	return isOpen;
    }
}
