package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.Game;
import ui.ItemOption;
import ui.NormalButton;

public class Slot {
	private Rectangle2D.Float bounds;
	private boolean isEmpty;
	private boolean showOption;
	private boolean isSelected;
	private int x, y;
	private Item item;
	private ArrayList<Item> items = new ArrayList<>();
	private ItemOption itemOption;
	private int xOffset, yOffset;
	private boolean mouseOver, mousePressed;

	public Slot(float x, float y, float width, float height, boolean isEmpty, boolean isEquipment, Item item) {
		bounds = new Rectangle2D.Float(x, y, width, height);
		xOffset = (int) (3 * Game.SCALE);
		yOffset = (int) (3 * Game.SCALE);
		this.isEmpty = isEmpty;
		this.item = item;
		itemOption = new ItemOption((int) (x + width), (int) y, 60, 47);
		initButton();
	}

	private void initButton() {

	}

	public void update() {

	}

	public void render(Graphics g) {
		if (!isEmpty) {
			items.get(0).renderIcon(g, (int) bounds.getX() + xOffset, (int) bounds.getY() + yOffset, 2);
			if (items.size() > 1) {
				g.setColor(new Color(255, 255, 255));
				g.drawString("" + items.size(), (int) (bounds.getX() + Game.TILES_SIZE * 2 - xOffset * 2),
						(int) (bounds.getY() + Game.TILES_SIZE * 2));
			}
			if (mouseOver) {
				g.setColor(new Color(0, 0, 0, 140));
				g.fillRect(x, y, 200, 300);
				g.setColor(new Color(255, 255, 255, 160));
				int x1 = x + xOffset;
				int y1 = y + yOffset + Game.TILES_SIZE;
				String text = items.get(0).getDescription();
				for (String line : text.split("\n")) {
					g.drawString(line, x1, y1);
					y1 += 20;
				}
			}			
		}
		if (isSelected) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
		}

	}

	public void renderItemOption(Graphics g) {
		if (!isEmpty)
			if (isSelected) {
				itemOption.render(g);
			}
	}

	public void keyPressed(KeyEvent e) {
		if (!isEmpty)
			itemOption.keyPressed(e);
	}

	public void addItem(Item item) {
		items.add(item);
		if (item.slot >= 0 && item.slot <= 9) {
			itemOption.setText(new String[] { "equip", "drop", "sell" });
		}
		else if(item.slot == -1) {
			itemOption.setText(new String[] { "use", "drop", "sell" });
		}	
		else if(item.slot == -2) {
			itemOption.setText(new String[] {"use","drop"});
		}			
		else if(item.slot == -3) {
			itemOption.setText(new String[] { "drop","sell"});
		}		
		else if(item.slot == 10) {
			itemOption.setText(new String[] {"learn","drop"});
		}
		isEmpty = false;
	}
	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public Item getItem() {
		return item;
	}

	public Rectangle2D.Float getBounds() {
		return bounds;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void ShowOption(boolean showOption) {
		this.showOption = showOption;
	}

	public void setDescriptionPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public void setSelect(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ItemOption getItemOption() {
		return this.itemOption;
	}
	public ArrayList<Item> getItems(){
		return this.items;
	}

}
