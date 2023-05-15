package objects;


import gamestates.Playing;
import main.Game;
import untilz.LoadSave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import Template.InventoryTemplate;
import Template.ItemTemplate;
import Template.MapTemplate;
import database.ItemManager;
import database.MySQL;

import static untilz.Constants.UI.Inventory.*;
import static untilz.HelpMethods.nextInt;

public class InventoryManager {
	private Playing playing;
	private int inventoryId = 0;
	private static ArrayList<Item> items = new ArrayList<Item>();
	public static InventoryTemplate[] inventoryTemplate;
	private BufferedImage InventoryImg;
	private Rectangle2D.Float Bag;
	private Slot[] Slots;
	private boolean isOpen = false;

	public InventoryManager(Playing playing) {
		this.playing = playing;
		LoadImg();
		initBag();
	}// 175

	public void initBag() {
		Bag = new Rectangle2D.Float(Game.TILES_SIZE * 35 + (INVENTORY_WIDTH - GRID_WIDTH) / 2 + 1*Game.SCALE,
				1 * Game.TILES_SIZE - 4 * Game.SCALE, GRID_WIDTH, GRID_HEIGHT);
		Slots = new Slot[24];
		float width = GRID_WIDTH / 4;
		float height = GRID_HEIGHT / 6;
		int row = 0;
		for (int i = 0; i < Slots.length; i++) {
			if (i != 0)
				if (i % 4 == 0) {
					++row;
				}
			Slots[i] = new Slot((float) (Bag.getX() + (i % 4 * width)), (float) (Bag.getY() + row * height), width,
					height, true,false, null);
		}	
	}
	public void initDataInventory() {
		float width = GRID_WIDTH / 4;
		float height = GRID_HEIGHT / 6;
		for(int i = 0; i < InventoryManager.inventoryTemplate[inventoryId].itemId.length; i++) {
			int index = InventoryManager.inventoryTemplate[inventoryId].itemIndex[i];
			Slots[index].addItem(new Item((int)0, (int)0, 0,
							ItemManager.arrItemTemplate[inventoryTemplate[inventoryId].itemIndex[index]]));
		}
	}
	public void LoadImg() {
		InventoryImg = LoadSave.GetSpriteAtlas(LoadSave.INVENTORY_BACKGROUND);
	}

	public void add(Item i) {
		items.add(i);
		for (int k = 0; k < Slots.length; k++) {
			if (Slots[k].isEmpty()) {
				Slots[k].addItem(i);
				break;
			} else if (Slots[k].getItems().get(0).getName().equals(i.getName())) {
				Slots[k].addItem(i);
				break;
			}

		}
	}

	public void render(Graphics g) {
		g.drawImage(InventoryImg, Game.TILES_SIZE * 35, 0, INVENTORY_WIDTH, INVENTORY_HEIGHT, null);
		g.setColor(new Color(255, 0, 0));
		g.drawRect((int) Bag.getX(), (int) Bag.getY(), (int) Bag.getWidth(), (int) Bag.getHeight());
		for (Slot r : Slots) {
			g.setColor(new Color(0, 0, 255));
			r.render(g);
		}
	}

	public void update() {
		for (Slot r : Slots) {
			r.update();
		}
	}

	public void mousePressed(MouseEvent e) {
		for (Slot s : Slots)
			if (isIn(e, s))
				s.setMousePressed(true);

	}

	public void mouseReleased(MouseEvent e) {
		for (Slot s : Slots) {
			if (isIn(e, s))
				if (s.isMousePressed()) {
					s.ShowOption(true);
				}
		}

		for (Slot s : Slots)
			s.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		for (Slot s : Slots)
			s.setMouseOver(false);

		for (Slot s : Slots)
			if (isIn(e, s)) {
				s.setMouseOver(true);
				s.setDescriptionPos(e.getX(), e.getY());
			}

	}

	private boolean isIn(MouseEvent e, Slot b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public boolean isOpen() {
		return isOpen;
	}
	public Slot[] getSlots() {
		return Slots;
	}
	public static void loadInventoryData() {
		try {
			InventoryTemplate[] _inventoryTemplates = new InventoryTemplate[0];
			final MySQL mySQL = new MySQL(0);

			ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `inventory`;");
			if (res.last()) {
				_inventoryTemplates = new InventoryTemplate[res.getRow()];
				res.beforeFirst();
			}
			int i = 0;
			while (res.next()) {
				final InventoryTemplate inventoryTemplate = new InventoryTemplate();
				inventoryTemplate.InventoryId = res.getShort("id");
				final JSONArray itemId = (JSONArray) JSONValue.parse(res.getString("itemId"));
				final JSONArray itemIndex = (JSONArray) JSONValue.parse(res.getString("itemIndex"));
				inventoryTemplate.itemId = new short[itemId.size()];
				inventoryTemplate.itemIndex = new short[inventoryTemplate.itemId.length];

			
				for (int j5 = 0; j5 < inventoryTemplate.itemId.length; ++j5) {
					inventoryTemplate.itemId[j5] = Short.parseShort(itemId.get(j5).toString());
					inventoryTemplate.itemIndex[j5] = Short.parseShort(itemIndex.get(j5).toString());
				}
				

				_inventoryTemplates[i] = inventoryTemplate;
				++i;
			}
			res.close();
			InventoryManager.inventoryTemplate = _inventoryTemplates;

		} catch (SQLException | NumberFormatException e) {
			throw new RuntimeException(e);
		}
	}

}
