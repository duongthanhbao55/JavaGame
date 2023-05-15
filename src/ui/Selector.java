package ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import Effect.Animation;
import Load.CacheDataLoader;
import objects.EquipmentEffect;
import objects.Item;
import objects.Slot;

public class Selector {

	private Animation selector;
	private Rectangle2D.Float bounds;
	private static Selector instance;
	private int index = 0;
	private boolean isInInventory = true;
	private boolean isInEquipment = false;
	private int first = 0;
	private Slot[] slotInventory;
	private Slot[] slotEquipment;
	private EquipmentEffect equipmentEffect;

	private ItemOption itemOption;

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
		if (isInInventory && !slotInventory[index].isEmpty())
			slotInventory[index].renderItemOption(g);
		if (isInEquipment && !slotEquipment[index].isEmpty()) {
			slotEquipment[index].renderItemOption(g);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (isInInventory) {
			if (!slotInventory[index].isEmpty())
				if (slotInventory[index].isSelected()) {
					slotInventory[index].keyPressed(e);
					if (slotInventory[index].getItemOption().isEquip()) {
						Equip();
					}
				}

		} else if (isInEquipment) {
			if (!slotEquipment[index].isEmpty())
				if (slotEquipment[index].isSelected()) {
					slotEquipment[index].keyPressed(e);
					if (slotEquipment[index].getItemOption().isUnequip()) {
						Unequip();
					}
				}
		}
		switch (e.getKeyCode()) {

		case KeyEvent.VK_UP:
			if (isInInventory && !slotInventory[index].isSelected()) {
				index -= 4;
				if (index < 0) {
					index = index + 24;
				}
				bounds = slotInventory[index].getBounds();
			} else if (isInEquipment && !slotEquipment[index].isSelected()) {
				if (index == 0) {
					index = 1;
				} else if (index == 2) {
					index = 6;
				} else if (index == 7) {
					index = 9;
				} else {
					index--;
				}
				bounds = slotEquipment[index].getBounds();
			}
			break;
		case KeyEvent.VK_DOWN:
			if (isInInventory && !slotInventory[index].isSelected()) {
				index += 4;
				if (index / 4 >= 6) {
					index = index % 6;
				}
				bounds = slotInventory[index].getBounds();
			} else if (isInEquipment && !slotEquipment[index].isSelected()) {
				index++;
				if (index == 2) {
					index = 0;
				} else if (index == 7) {
					index = 2;
				} else if (index == 10) {
					index = 7;
				}
				bounds = slotEquipment[index].getBounds();
			}

			break;
		case KeyEvent.VK_LEFT:
			if (isInInventory && !slotInventory[index].isSelected()) {
				index--;
				if (index == -1) {
					index = 3;
				} else if (index % 4 == 3) {
					index = index + 4;
				}
				bounds = slotInventory[index].getBounds();
			} else if (isInEquipment && !slotEquipment[index].isSelected()) {
				if (index == 0) {
					index = 8;
				} else if (index == 1) {
					index = 9;
				} else if (index == 6) {
					index = 1;
				} else if (index == 9) {
					index = 6;
				} else if (index == 8) {
					index = 3;
				} else if (index == 7) {
					index = 2;
				} else if (index > 1 && index < 6) {
					index = 0;
				}
				bounds = slotEquipment[index].getBounds();
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (isInInventory && !slotInventory[index].isSelected()) {
				if (slotInventory[index].isSelected()) {
					slotInventory[index].setSelect(false);
				}
				index++;
				if (index % 4 == 0) {
					index = index - 4;
				}
				bounds = slotInventory[index].getBounds();
			} else if (isInEquipment && !slotEquipment[index].isSelected()) {
				if (slotEquipment[index].isSelected()) {
					slotEquipment[index].setSelect(false);
				}
				if (index == 0 || index == 6) {
					index += 3;
				} else if (index == 1) {
					index += 5;
				} else if (index == 9) {
					index = 1;
				} else if (index == 3) {
					index = 8;
				} else if (index == 7 || index == 8) {
					index = 0;
				} else if (index == 2) {
					index = 7;
				} else if (index == 5 || index == 4) {
					index = 8;
				}
				bounds = slotEquipment[index].getBounds();
			}
			break;
		case KeyEvent.VK_ENTER:
			if (isInInventory) {
				slotInventory[index].setSelect(!slotInventory[index].isSelected());
			} else if (isInEquipment) {
				slotEquipment[index].setSelect(!slotEquipment[index].isSelected());
			}
			break;
		case KeyEvent.VK_BACK_QUOTE:
			if (isInInventory) {
				if (slotInventory[index].isSelected()) {
					slotInventory[index].setSelect(false);
				}
				index = 0;
				bounds = slotEquipment[index].getBounds();
			} else if (isInEquipment) {
				if (slotEquipment[index].isSelected()) {
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

	private void Equip() {
		int index1 = slotInventory[index].getItems().get(0).getSlot();
		if (index1 >= 0 && index1 <= 9) {
			if (!slotEquipment[index1].isEmpty()) {
				Item t = slotEquipment[index1].getItems().get(0);
				if (!t.getName().equals(slotInventory[index].getItems().get(0).getName())) {
		
					slotEquipment[index1].getItems().remove(0);
					equipmentEffect.removeEffect(t);
					
					slotEquipment[index1].addItem(slotInventory[index].getItems().get(0));
					equipmentEffect.applyEffect(slotInventory[index].getItems().get(0));

					slotInventory[index].getItems().remove(0);

					if (slotInventory[index].getItems().size() <= 0) {
						slotInventory[index].setEmpty(true);
					}
					for (Slot s : slotInventory) {
						if (!s.isEmpty()) {
							if (t.getName().equals(s.getItems().get(0).getName())) {
								s.addItem(t);
								slotEquipment[index1].getItemOption()
										.setText(new String[] { "unequip", "drop", "sell" });
								return;
							}
						}
					}
					for (Slot s : slotInventory) {
						if (s.isEmpty()) {
							s.addItem(t);
							slotEquipment[index1].getItemOption().setText(new String[] { "unequip", "drop", "sell" });
							break;
						}
					}
				}

			} else {
				slotEquipment[index1].addItem(slotInventory[index].getItems().get(0));
				equipmentEffect.applyEffect(slotEquipment[index1].getItems().get(0));
				slotInventory[index].getItems().remove(slotEquipment[index1].getItems().get(0));
				if (slotInventory[index].getItems().size() <= 0) {
					slotInventory[index].setEmpty(true);
				}
				slotEquipment[index1].getItemOption().setText(new String[] { "unequip", "drop", "sell" });
			}
		}
	}

	private void Unequip() {

		for (Slot s : slotInventory) {
			if (!s.isEmpty()) {
				if (s.getItems().get(0).getName().equals(slotEquipment[index].getItems().get(0).getName())) {
					s.addItem(slotEquipment[index].getItems().get(0));
					equipmentEffect.removeEffect(slotEquipment[index].getItems().get(0));
					slotEquipment[index].getItems().remove(0);
					slotEquipment[index].setEmpty(true);
					return;
				}
			}
		}
		for (Slot s : slotInventory) {
			if (s.isEmpty()) {
				s.addItem(slotEquipment[index].getItems().get(0));
				equipmentEffect.removeEffect(slotEquipment[index].getItems().get(0));
				slotEquipment[index].getItems().remove(0);
				slotEquipment[index].setEmpty(true);
				return;
			}
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

	public void setItemOption(ItemOption itemOption) {
		this.itemOption = itemOption;
	}

	public void setEquipmentEffect(EquipmentEffect equipmentEffect) {
		this.equipmentEffect = equipmentEffect;
	}
}
