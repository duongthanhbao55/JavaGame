package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;
import ui.Confirm;

public class Slot {
	private Rectangle2D.Float bounds;
	private boolean isEmpty;
	private boolean showOption;
	private boolean isEquipment;
	private int x,y;
	private Item item;
	private int xOffset, yOffset;
	private boolean mouseOver, mousePressed;

	public Slot(float x, float y, float width, float height, boolean isEmpty,boolean isEquipment, Item item) {
		bounds = new Rectangle2D.Float(x, y, width, height);
		xOffset = (int) (3 * Game.SCALE);
		yOffset = (int) (3 * Game.SCALE);
		this.isEmpty = isEmpty;
		this.item = item;
	}

	public void update() {

	}

	public void render(Graphics g) {
				g.drawRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
		if (!isEmpty) {
			item.renderIcon(g, (int) bounds.getX() + xOffset, (int) bounds.getY() + yOffset, 2);
			if (item.getQuantity() > 1) {
				g.setColor(new Color(255, 255, 255));
				g.drawString("" + item.getQuantity(), (int) (bounds.getX() + Game.TILES_SIZE * 2 - xOffset * 2),
						(int) (bounds.getY() + Game.TILES_SIZE * 2));
			}
			if (mouseOver) {
				System.out.println(item.getSlot());
				g.setColor(new Color(0,0,0,140));
				g.fillRect(x, y, 200, 300);
				g.setColor(new Color(255,255,255,160));
				int x1 = x + xOffset;
				int y1 = y + yOffset + Game.TILES_SIZE;
				String text = item.getDescription();
				for (String line : text.split("\n")) {
					g.drawString(line, x1, y1);
					y1 += 20;
				}
			}
		}

	}

	public void setItem(Item item) {
		this.item = item;
		isEmpty = false;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public Item getItem() {
		return item;
	}

	public Rectangle2D.Float getBounds() {
		return bounds;
	}

	public void increaseItem() {
		item.setQuantity(item.getQuantity() + 1);
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
}
