package database;

import Level.EnemyManager;
import Template.EnemyTemplate;
import Template.ItemTemplate;
import objects.Item;
import entities.Player;
import gamestates.Playing;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemManager {
    //VARIABLES
    private Playing playing;
    public static ItemTemplate[] arrItemTemplate;
    private static ArrayList<Item> items = new ArrayList<Item>();
    static boolean isInteracting = false;

    public ItemManager(Playing playing) {
        this.playing = playing;

        items.add(new Item(500, 400, 0, arrItemTemplate[9]));


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

                    itemTemplate.name = read.getString("name");
                    itemTemplate.atk = read.getInt("atk");
                    itemTemplate.hp = read.getInt("hp");
                    itemTemplate.def = read.getInt("def");
                    itemTemplate.slot = read.getInt("slot");
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
        Item[] itemDrops = new Item[items.size()];


        for (int i = 0;i<items.size();i++) {
            itemDrops[i] = null;
            if (player.getHitbox().intersects(items.get(i).getHitbox())) {
                items.get(i).isContact = true;
                items.get(i).setQuantity(items.get(i).getQuantity() + 1);

                playing.getInventoryManager().add(items.get(i));
                items.get(i).isContact = false;
                itemDrops[i] = items.get(i);
                //items.remove(i);
            }

        }
        for(Item i : itemDrops){
            if(i != null){
                items.remove(i);
            }
        }

    }

    public void render(Graphics g, int xLvlOffset) {
        for (Item i : items) {
            i.render(g, xLvlOffset);
        }
    }

    public void update() {
        for (Item i : items) {
            i.update();
        }
    }

    public void add(Item i) {
        items.add(i);
    }
}
