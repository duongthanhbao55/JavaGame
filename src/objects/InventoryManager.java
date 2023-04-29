package objects;

import Template.ItemTemplate;
import gamestates.Playing;

import java.util.ArrayList;

public class InventoryManager {
    private Playing playing;
    private static ArrayList<Item> items = new ArrayList<Item>();

    public InventoryManager(Playing playing) {
        this.playing = playing;
    }
    public void add(Item i){
        items.add(i);
        System.out.println(i);
    }
}
