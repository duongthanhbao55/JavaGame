package objects;

import database.MySQL;
import objects.GameObject;

import java.sql.SQLException;



public class Item extends GameObject {

    public String name;

    public int atk;
    public int def;
    public int hp;
    public String type;
    public String ability;
    public String description;

    public boolean isContact = false;


    public Item(int x, int y, int objType) {
        super(x, y, objType);
    }
}

