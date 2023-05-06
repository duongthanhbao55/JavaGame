package database;

import Level.EnemyManager;
import Template.EnemyTemplate;
import Template.ItemTemplate;
import objects.Item;
import ui.Confirm;
import entities.Player;
import gamestates.Playing;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemManager {
	// VARIABLES
	private Playing playing;
	public static ItemTemplate[] arrItemTemplate;
	private static ArrayList<Item> items = new ArrayList<Item>();
	static boolean isInteracting = false;

	public ItemManager(Playing playing) {
		this.playing = playing;
	}

	public static void loadItemData() {

		try {
			final MySQL mySQL = new MySQL(0);

			try {
				ResultSet read = mySQL.stat.executeQuery("SELECT * FROM Item;");

				if (read.last()) {
					ItemManager.arrItemTemplate = new ItemTemplate[read.getRow()];
					read.beforeFirst();
				}

				int i = 0;

				while (read.next()) {
					final ItemTemplate itemTemplate = new ItemTemplate();


					// Parse effect
					try {
						final String[] effect = read.getString("effect").trim().split(",");
						itemTemplate.atk = Integer.parseInt(effect[0].trim());
						itemTemplate.hp = Integer.parseInt(effect[1].trim());
						itemTemplate.def = Integer.parseInt(effect[2].trim());
						itemTemplate.atk_up = Float.parseFloat(effect[3].trim());
						itemTemplate.hp_up = Float.parseFloat(effect[4].trim());
						itemTemplate.def_up = Float.parseFloat(effect[5].trim());
						itemTemplate.speed_up = Float.parseFloat(effect[6].trim());
						itemTemplate.dmg_up = Float.parseFloat(effect[7].trim());
						itemTemplate.dmg_down = Float.parseFloat(effect[8].trim());
						itemTemplate.heal = Integer.parseInt(effect[9].trim());
						itemTemplate.mana = Integer.parseInt(effect[10].trim());
					} catch (SQLException | NumberFormatException e) {
						System.out.println("Parse Error");
						throw new RuntimeException(e);
					}

					// OTHER VARIABLES
					itemTemplate.id = (byte) read.getInt("itemID");
					itemTemplate.name = read.getString("name");
					itemTemplate.slot = (byte) read.getInt("slot");
					itemTemplate.ability = read.getString("ability");
					itemTemplate.description = read.getString("itemdesc");
					itemTemplate.filename = read.getString("filename");


					ItemManager.arrItemTemplate[i] = itemTemplate;

					++i;

				}

				read.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public void checkItemContact(Player player) {
		Item[] itemDrops = new Item[ItemManager.items.size()];

		for (int i = 0; i < ItemManager.items.size(); i++) {
			itemDrops[i] = null;
			if (player.getHitbox().intersects(ItemManager.items.get(i).getHitbox()) && !Confirm.isShow()) {
				ItemManager.items.get(i).isContact = true;
				ItemManager.items.get(i).setQuantity(ItemManager.items.get(i).getQuantity() + 1);

				playing.getInventoryManager().add(ItemManager.items.get(i));
				ItemManager.items.get(i).isContact = false;
				itemDrops[i] = ItemManager.items.get(i);
				// items.remove(i);
				break;
			}

		}
		for (Item i : itemDrops) {
			if (i != null) {
				ItemManager.items.remove(i);
				break;
			}
		}

	}

	public void render(Graphics g, int xLvlOffset) {
		if (ItemManager.items.size() > 0)
			for (Item i : ItemManager.items) {
				i.render(g, xLvlOffset);
			}
	}

	public void update() {
		for (Item i : ItemManager.items) {
			i.update();
		}
	}

	public void add(Item i) {
		ItemManager.items.add(i);
	}

	public static void addItem(Item i) {
		System.out.println(true);
		ItemManager.items.add(i);
	}
}
