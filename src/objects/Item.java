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

    final protected String name;
    final protected byte id;
    final protected int atk;
    final protected int def;
    final protected int hp;
    final protected float atk_up;
    final protected float hp_up;
    final protected float def_up;
    final protected float speed_up;
    final protected float dmg_up;
    final protected float dmg_down;
    final protected int heal;
    final protected int mana;
    final protected byte slot;
    final protected String ability;
    final protected String description;
    final protected int gold;
    protected int quantity = 0;
    public boolean isContact = false;
    protected int maxHoverOffset, hoverDir = 1;
    protected float hoverOffset;
    protected BufferedImage img;
    protected boolean isEquipped = false;
    protected String[] options; 
    
    public Item(int x, int y, int objType, ItemTemplate itemTemplate) {
        super(x, y, objType);
        initHitbox((int) ( 16*Game.SCALE), (int) ( 16*Game.SCALE));
        id = itemTemplate.id;
        img = loadImg(itemTemplate.filename);
        name = itemTemplate.name;
        atk = itemTemplate.atk;
        hp = itemTemplate.hp;
        def = itemTemplate.def;
        atk_up = itemTemplate.atk_up;
        hp_up = itemTemplate.hp_up;
        def_up = itemTemplate.def_up;
        speed_up = itemTemplate.speed_up;
        dmg_up = itemTemplate.dmg_up;
        dmg_down = itemTemplate.dmg_down;
        heal = itemTemplate.heal;
        mana = itemTemplate.mana;
        gold = itemTemplate.gold;
        slot = itemTemplate.slot;
        ability = itemTemplate.ability;
        description = itemTemplate.description;
        maxHoverOffset = (int)(5 * Game.SCALE);
    }

    public String getName() {
        return name;
    }

    public int getAtk() {
        return atk;
    }


    public int getDef() {
        return def;
    }


    public int getHp() {
        return hp;
    }


    public String getAbility() {
        return ability;
    }


    public String getDescription() {
        return description;
    }

    public float getAtk_up() {
        return atk_up;
    }

    public float getHp_up() {
        return hp_up;
    }

    public float getDef_up() {
        return def_up;
    }

    public float getSpeed_up() {
        return speed_up;
    }

    public float getDmg_up() {
        return dmg_up;
    }

    public float getDmg_down() {
        return dmg_down;
    }

    public int getHeal() {
        return heal;
    }

    public int getMana() {
        return mana;
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
    
    public byte getId() {
		return id;
	}

    public int getGold() {
        return gold;
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


