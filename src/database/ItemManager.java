package database;

import Level.EnemyManager;
import Template.EnemyTemplate;
import Template.ItemTemplate;
import objects.Item;
import entities.Player;
import gamestates.Playing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemManager {
    //VARIABLES
    private Playing playing;
    private static ItemManager instance;
    public static ItemTemplate[] arrItemTemplate;
    private static ArrayList<Item> items = new ArrayList<Item>();
    static boolean isInteracting = false;
public ItemManager(Playing playing){
    this.playing = playing;
    loadItemData();
}
public static void loadItemData(){

    try {
        final MySQL mySQL = new MySQL(0);

        try{
            ResultSet read = mySQL.stat.executeQuery("SELECT * FROM Item;");

            if (read.last()) {
                EnemyManager.arrEnemyTemplate = new EnemyTemplate[read.getRow()];
                read.beforeFirst();
            }

            int i = 0;

            while (read.next()){
                final ItemTemplate itemTemplate = new ItemTemplate();
                itemTemplate.name = read.getString("name");
                itemTemplate.atk = read.getInt("atk");
                itemTemplate.hp = read.getInt("hp");
                itemTemplate.def = read.getInt("def");
                itemTemplate.slot = read.getInt("slot");
                itemTemplate.ability = read.getString("ability");
                itemTemplate.description = read.getString("itemdesc");

                ItemManager.arrItemTemplate[i] = itemTemplate;

                ++i;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

public static void checkItemContact (Player player){

    for (Item i :items) {
        if (player.getHitbox().intersects(i.getHitbox())){
            i.isContact = true;
        }
    }
}





}
