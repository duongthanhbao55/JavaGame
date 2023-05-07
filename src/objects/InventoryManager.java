package objects;

import Template.ItemTemplate;

import gamestates.Playing;
import main.Game;
import ui.PauseButton;
import untilz.LoadSave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static untilz.Constants.UI.Inventory.*;

public class InventoryManager {
	private Playing playing;
	private static ArrayList<Item> items = new ArrayList<Item>();
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
		Bag = new Rectangle2D.Float(Game.TILES_SIZE * 35 + (INVENTORY_WIDTH - GRID_WIDTH) / 2,
				1 * Game.TILES_SIZE - 5 * Game.SCALE, GRID_WIDTH, GRID_HEIGHT);
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
		System.out.println(width + "" + height);
	}

	public void LoadImg() {
		InventoryImg = LoadSave.GetSpriteAtlas(LoadSave.INVENTORY_BACKGROUND);
	}

	public void add(Item i) {
		items.add(i);
		for (int k = 0; k < Slots.length; k++) {
			if (Slots[k].isEmpty()) {
				Slots[k].setItem(i);
				break;
			} else if (Slots[k].getItem().getName().equals(i.getName())) {
				Slots[k].increaseItem();
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

}
