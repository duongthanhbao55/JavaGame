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

    protected String name;
    protected int atk;
    protected int def;
    protected int hp;
    protected float atk_up;
    protected float hp_up;
    protected float def_up;
    protected float speed_up;
    protected float dmg_up;
    protected float dmg_down;
    protected int heal;
    protected int mana;
    protected byte slot;
    protected String ability;
    protected String description;
    protected int quantity = 0;
    public boolean isContact = false;
    protected int maxHoverOffset, hoverDir = 1;
    protected float hoverOffset;
    protected BufferedImage img;

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
    public int getSlot() {
    	return slot;
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
    public void renderIcon(Graphics g,int x, int y, float scale) {
    	g.drawImage(img, x, y, (int) (16 * Game.SCALE * scale) , (int) (16 * Game.SCALE * scale), null);
    }

    public void update() {
        updateHover();
    }
    protected void updateHover() {
        hoverOffset += (0.075f * Game.SCALE * hoverDir);
        if(hoverOffset >= maxHoverOffset) {
            hoverDir = -1;
        }else if(hoverOffset < 0) {
            hoverDir = 1;
        }
        hitbox.y = y + hoverOffset;
    }

}


