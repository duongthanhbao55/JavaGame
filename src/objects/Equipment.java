package objects;

import static untilz.Constants.UI.EquipmentUI.*;
import static untilz.Constants.UI.Inventory.GRID_HEIGHT;
import static untilz.Constants.UI.Inventory.GRID_WIDTH;
import static untilz.Constants.UI.Inventory.INVENTORY_WIDTH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import Template.EquipmentTemplate;
import Template.InventoryTemplate;
import database.ItemManager;
import database.MySQL;
import gamestates.Playing;
import main.Game;
import ui.Selector;
import untilz.LoadSave;
import untilz.Text;

import static untilz.Constants.EquipmentConstants.*;

public class Equipment {
	private Playing playing;
	private int equipmentId = 0;
	public static EquipmentTemplate[] equipmentTemplate;
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
	public void initEquipment() {
		for(int i = 0; i < equipmentTemplate[equipmentId].itemId.length; i++) {
			int index = ItemManager.arrItemTemplate[equipmentTemplate[equipmentId].itemId[i]].slot;
			Slots[index].addItem(new Item((int)0, (int)0, 0,
							ItemManager.arrItemTemplate[equipmentTemplate[equipmentId].itemId[i]]));
		
			Slots[index].getItemOption().setText(new String[] { Text.get(0, 3), Text.get(0, 4), Text.get(0, 6) });
		}
		Selector.getInstance().getEquipmentEffect().applyEffect(Slots);
	}
	public void update() {
		
	}
	public void render(Graphics g) {
		g.drawImage(equipmentUI, Game.TILES_SIZE * 26, 0, EQUIPMENT_UI_WIDTH, EQUIPMENT_UI_HEIGHT, null);
		g.drawRect((int) Bag.getX(), (int) Bag.getY(), (int) Bag.getWidth(), (int) Bag.getHeight());
		for(Slot s : Slots) {
			s.render(g);
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
	public Slot[] getSlots() {
		return Slots;
	}
	public static void loadEquipment() {
		try {
			EquipmentTemplate[] _equipmentTemplates = new EquipmentTemplate[0];
			final MySQL mySQL = new MySQL(0);

			ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `equipment`;");
			if (res.last()) {
				_equipmentTemplates = new EquipmentTemplate[res.getRow()];
				res.beforeFirst();
			}
			int i = 0;
			while (res.next()) {
				final EquipmentTemplate equipmentTemplates = new EquipmentTemplate();
				equipmentTemplates.EquipmentId = res.getShort("id");
				final JSONArray itemId = (JSONArray) JSONValue.parse(res.getString("itemId"));
				equipmentTemplates.itemId = new short[itemId.size()];
			
				for (int j5 = 0; j5 < equipmentTemplates.itemId.length; ++j5) {
					equipmentTemplates.itemId[j5] = Short.parseShort(itemId.get(j5).toString());
				}
				

				_equipmentTemplates[i] = equipmentTemplates;
				++i;
			}
			res.close();
			equipmentTemplate = _equipmentTemplates;

		} catch (SQLException | NumberFormatException e) {
			throw new RuntimeException(e);
		}
	}
}
