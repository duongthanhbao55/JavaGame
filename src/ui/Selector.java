package ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import Effect.Animation;
import Load.CacheDataLoader;
import objects.Slot;

public class Selector {

	private Animation selector;
	private Rectangle2D.Float bounds;
	private static Selector instance;
	private int index = 0;
	private boolean isInInventory = true;
	private boolean isInEquipment = false;
	private Slot[] slotInventory;
	private Slot[] slotEquipment;

	public Selector() {
		loadAnim();
	}

	public Selector(float xPos, float yPos, float width, float height) {
		bounds = new Rectangle2D.Float(xPos, yPos, width, height);
		loadAnim();
	}

	public Selector(Rectangle2D.Float bounds) {
		this.bounds = bounds;
		loadAnim();
	}

	public static Selector getInstance() {
		if (instance == null) {
			instance = new Selector();
		}
		return instance;
	}

	private void loadAnim() {
		selector = CacheDataLoader.getInstance().getAnimation("Selector");
	}

	public void update(long currTime) {
		selector.Update(currTime);
	}

	public void render(Graphics g) {
		selector.drawNormal((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight(),
				g);
	}

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (isInInventory) {
				if(slotInventory[index].isSelected()) {
					slotInventory[index].setSelect(false);
				}
				index -= 4;
				if (index < 0) {
					index = index + 24;
				}
				bounds = slotInventory[index].getBounds();
			} else if (isInEquipment) {
				if(slotEquipment[index].isSelected()) {
					slotEquipment[index].setSelect(false);
				}
				if(index == 0) {
					index = 1;
				}else if(index == 2) {
					index = 6;
				}else if(index == 7) {
					index = 9;
				}else {
					index--;
				}
				bounds = slotEquipment[index].getBounds();
			}
			break;
		case KeyEvent.VK_DOWN:
			if (isInInventory) {
				if(slotInventory[index].isSelected()) {
					slotInventory[index].setSelect(false);
				}
				index += 4;
				if (index / 4 >= 6) {
					index = index % 6;
				}
				bounds = slotInventory[index].getBounds();
			} else if (isInEquipment) {
				if(slotEquipment[index].isSelected()) {
					slotEquipment[index].setSelect(false);
				}
				index++;	
				if(index == 2) {
					index = 0;
				}else if(index == 7) {
					index = 2;
				}else if(index == 10) {
					index = 7;
				}
				bounds = slotEquipment[index].getBounds();
			}

			break;
		case KeyEvent.VK_LEFT:
			if (isInInventory) {
				if(slotInventory[index].isSelected()) {
					slotInventory[index].setSelect(false);
				}
				index--;
				if (index == -1) {
					index = 3;
				} else if (index % 4 == 3) {
					index = index + 4;
				}
				bounds = slotInventory[index].getBounds();
			} else if (isInEquipment) {
				if(slotEquipment[index].isSelected()) {
					slotEquipment[index].setSelect(false);
				}
				if(index == 0) {
					index = 8;
				}else if(index == 1) {
					index = 9;
				}else if(index == 6) {
					index = 1;
				}else if(index == 9) {
					index = 6;
				}else if(index == 8) {
					index = 3;
				}else if(index == 7	) {
					index = 2;
				}
				else if(index > 1 && index < 6) {
					index = 0;
				}
				bounds = slotEquipment[index].getBounds();
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (isInInventory) {
				if(slotInventory[index].isSelected()) {
					slotInventory[index].setSelect(false);
				}
				index++;
				if (index % 4 == 0) {
					index = index - 4;
				}
				bounds = slotInventory[index].getBounds();
			} else if (isInEquipment) {
				if(slotEquipment[index].isSelected()) {
					slotEquipment[index].setSelect(false);
				}
				if(index == 0 || index == 6) {
					index += 3;
				}else if(index == 1) {
					index += 5;
				}else if(index == 9){
					index = 1;
				}else if(index == 3) {
					index = 8;
				}else if(index == 7 || index == 8) {
					index = 0;
				}else if(index == 2) {
					index = 7;
				}else if(index == 5 || index == 4) {
					index = 8;
				}
				bounds = slotEquipment[index].getBounds();
			}
			break;
		case KeyEvent.VK_ENTER:
			if(isInInventory) {
				slotInventory[index].setSelect(!slotInventory[index].isSelected());
			}else if(isInEquipment) {
				slotEquipment[index].setSelect(!slotEquipment[index].isSelected());
			}
			break;
		case KeyEvent.VK_BACK_QUOTE:
			if (isInInventory) {
				if(slotInventory[index].isSelected()) {
					slotInventory[index].setSelect(false);
				}
				index = 0;
				bounds = slotEquipment[index].getBounds();
			} else if (isInEquipment) {
				if(slotEquipment[index].isSelected()) {
					slotEquipment[index].setSelect(false);
				}
				index = 0;
				bounds = slotInventory[index].getBounds();
			}
			isInInventory = !isInInventory;
			isInEquipment = !isInEquipment;
			break;
		}

	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			break;
		case KeyEvent.VK_DOWN:
			break;
		case KeyEvent.VK_LEFT:
			break;
		case KeyEvent.VK_RIGHT:
			break;
		case KeyEvent.VK_ENTER:
			break;
		}
	}

	public void setBounds(int xPos, int yPos, int width, int height) {
		bounds.x = xPos;
		bounds.y = yPos;
		bounds.width = width;
		bounds.height = height;
	}

	public void setBounds(Rectangle2D.Float bounds) {
		this.bounds = bounds;
	}

	public void setSlotInventory(Slot[] slotInventory) {
		this.slotInventory = slotInventory;
	}

	public void setSlotEquipment(Slot[] slotEquipment) {
		this.slotEquipment = slotEquipment;
	}
}
