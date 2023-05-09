package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;
import ui.Confirm;
import ui.NormalButton;

public class Slot {
	private Rectangle2D.Float bounds;
	private boolean isEmpty;
	private boolean showOption;
	private boolean isEquipment;
	private boolean isSelected;
	private int x, y;
	private Item item;
	private int xOffset, yOffset;
	private int xPosButton, yPosButton;
	private boolean mouseOver, mousePressed;
	private NormalButton[] optionButton;

	public Slot(float x, float y, float width, float height, boolean isEmpty, boolean isEquipment, Item item) {
		bounds = new Rectangle2D.Float(x, y, width, height);
		xOffset = (int) (3 * Game.SCALE);
		yOffset = (int) (3 * Game.SCALE);
		this.isEmpty = isEmpty;
		this.item = item;
		initButton();
	}

	private void initButton() {
		optionButton = new NormalButton[3];
		optionButton[0] = new NormalButton(xPosButton, xPosButton, 0, 1f, (byte) 0, "use");
		optionButton[1] = new NormalButton(xPosButton, xPosButton, 0, 1f, (byte) 1, "drop");
		optionButton[2] = new NormalButton(xPosButton, xPosButton, 0, 1f, (byte) 2, "sell");
	}

	public void update() {
		if (mousePressed)
			for (NormalButton nb : optionButton) {
				nb.update();
			}
	}

	public void render(Graphics g) {
		if (!isEmpty) {
			item.renderIcon(g, (int) bounds.getX() + xOffset, (int) bounds.getY() + yOffset, 2);
			if (item.getQuantity() > 1) {
				g.setColor(new Color(255, 255, 255));
				g.drawString("" + item.getQuantity(), (int) (bounds.getX() + Game.TILES_SIZE * 2 - xOffset * 2),
						(int) (bounds.getY() + Game.TILES_SIZE * 2));
			}
			if (mouseOver) {
				g.setColor(new Color(0, 0, 0, 140));
				g.fillRect(x, y, 200, 300);
				g.setColor(new Color(255, 255, 255, 160));
				int x1 = x + xOffset;
				int y1 = y + yOffset + Game.TILES_SIZE;
				String text = item.getDescription();
				for (String line : text.split("\n")) {
					g.drawString(line, x1, y1);
					y1 += 20;
				}
			}
			if (mousePressed) {
				for (NormalButton nb : optionButton) {
					nb.render(g);
				}
			}
		}
		if (isSelected) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
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

	public void setPosButton(int xPosButton, int yPosButton) {
		this.xPosButton = xPosButton;
		this.yPosButton = yPosButton;
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

}
