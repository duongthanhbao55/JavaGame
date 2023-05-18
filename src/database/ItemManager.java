package database;

import Level.EnemyManager;
import Map.PhysicalMap;
import Template.EnemyTemplate;
import Template.ItemTemplate;
import objects.Item;
import ui.Confirm;
import entities.Player;
import gamestates.Playing;
import untilz.LoadSave;

import java.awt.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

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
            ItemTemplate[] _arrItemTemplate = new ItemTemplate[0];
            final MySQL mySQL = new MySQL(0);

            ResultSet read = mySQL.stat.executeQuery("SELECT * FROM Item;");

            if (read.last()) {
                _arrItemTemplate = new ItemTemplate[read.getRow()];
                read.beforeFirst();
            }

            int i = 0;

            while (read.next()) {
                final ItemTemplate itemTemplate = new ItemTemplate();
                // Parse effect
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
                // OTHER VARIABLES
                itemTemplate.id = (byte) read.getInt("itemID");
                itemTemplate.name = read.getString("name");
                itemTemplate.slot = (byte) read.getInt("slot");
                itemTemplate.ability = read.getString("ability");
                itemTemplate.description = read.getString("itemdesc");
                itemTemplate.filename = read.getString("filename");

                _arrItemTemplate[i] = itemTemplate;
                ++i;
            }
            read.close();

            arrItemTemplate = _arrItemTemplate;
        } catch (SQLException | NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadItemDataFromCSV() {
        final String filePath = "csv/assets-1805-2.csv";
        InputStream inputStream = LoadSave.class.getResourceAsStream('/' + filePath);
        assert inputStream != null;
        BufferedReader bufferedReader;
        ItemTemplate[] _tempArrItemTemplate;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            int lineCount = (int) bufferedReader.lines().count();
            int i =0;
            _tempArrItemTemplate = new ItemTemplate[lineCount];
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null&& i <lineCount) {
                assert false;
                String[] datas = line.split(";");
                ItemTemplate IT = null;
                IT.name = datas[0];
                IT.slot = Byte.parseByte(datas[1]);
                IT.gold = Integer.parseInt(datas[2]);
                IT.filename = datas[3];
                IT.ability = datas[4];
                IT.description = datas[5];
                String[] effects = datas[6].split(",");
                IT.atk = Integer.parseInt(effects[0]);
                IT.hp = Integer.parseInt(effects[1]);
                IT.def = Integer.parseInt(effects[2]);
                IT.atk_up = Float.parseFloat(effects[3]);
                IT.hp_up = Float.parseFloat(effects[4]);
                IT.def_up = Float.parseFloat(effects[5]);
                IT.speed_up = Float.parseFloat(effects[6]);
                IT.dmg_up = Float.parseFloat(effects[7]);
                IT.dmg_down = Float.parseFloat(effects[8]);
                IT.heal = Integer.parseInt(effects[9]);
                IT.mana = Integer.parseInt(effects[10]);
                _tempArrItemTemplate[i] = IT;
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        arrItemTemplate = _tempArrItemTemplate;
        for (ItemTemplate itemTemplate : arrItemTemplate) {
            System.out.println(itemTemplate.toString());
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
        ItemManager.items.add(i);
    }
    public void loadItem(PhysicalMap physicalMap) {
    	ItemManager.items = physicalMap.getItems();
    }
    public static ArrayList<Item> getItems() {
    	return ItemManager.items;
    }
}
