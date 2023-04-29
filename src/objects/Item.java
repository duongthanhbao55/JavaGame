package objects;

import Template.ItemTemplate;
import database.MySQL;
import main.Game;
import objects.GameObject;
import untilz.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;


public class Item extends GameObject {

    private String name;
    private int atk;
    private int def;
    private int hp;
    private int slot;
    private String ability;
    private String description;
    private int quantity = 0;
    public boolean isContact = false;
    private int maxHoverOffset, hoverDir = 1;
    private float hoverOffset;
    private BufferedImage img;

    public Item(int x, int y, int objType, ItemTemplate itemTemplate) {
        super(x, y, objType);
        initHitbox((int) ( 16*Game.SCALE), (int) ( 16*Game.SCALE));
        img = loadImg(itemTemplate.filename);
        name = itemTemplate.name;
        atk = itemTemplate.atk;
        hp = itemTemplate.hp;
        def = itemTemplate.def;
        slot = itemTemplate.slot;
        ability = itemTemplate.ability;
        description = itemTemplate.description;
        maxHoverOffset = (int)(5 * Game.SCALE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private BufferedImage loadImg(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/assets/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public void render(Graphics g, int xLvlOffset) {
        g.drawImage(img, (int) hitbox.getX() - xLvlOffset, (int) hitbox.getY(), (int) (16 * Game.SCALE) , (int) (16 * Game.SCALE), null);
    }

    public void update() {
        updateHover();
    }
    private void updateHover() {
        hoverOffset += (0.075f * Game.SCALE * hoverDir);
        if(hoverOffset >= maxHoverOffset) {
            hoverDir = -1;
        }else if(hoverOffset < 0) {
            hoverDir = 1;
        }
        hitbox.y = y + hoverOffset;
    }
}


