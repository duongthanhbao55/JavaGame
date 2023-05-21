package objects;

import entities.Player;
import gamestates.Playing;

import static untilz.Constants.PlayerConstants.*;

public class EquipmentEffect {
    private final Playing playing;
    private int ATK_flat;
    private float ATK_percent;
    private float DMG_UP;
    private float DMG_DOWN;
    private int HP;
    private int DEF_flat;
    private float DEF_percent;

    public EquipmentEffect(Playing playing) {
        this.playing = playing;
        this.ATK_flat = 0;
        this.ATK_percent = 0;
        this.DMG_DOWN = 0;
        this.DMG_UP = 0;
        this.HP = 0;
        this.DEF_flat = 0;
        this.DEF_percent = 0;
    }

    public float getDMG_DOWN() {
        return DMG_DOWN;
    }

    public void applyEffect(Slot[] slots) {
        for (Slot s : slots) {
            if (s != null)
                if (!s.getItems().isEmpty())
                    applyEffect(s.getItems().get(0));
        }
    }

    public void removeEffect(Item item) {
        final Player player = playing.getPlayer();
        // ATK
        ATK_flat -= item.atk;
        ATK_percent -= item.atk_up;
        player.setAttack((int) (DEFAULT_ATTACK * (1 + ATK_percent) + ATK_flat));
        player.applyDef(-item.getDef());
        // DMG
        int currAtk = player.getAttack();
        DMG_UP -= item.dmg_up;
        player.setDamage((int) (currAtk * (1 + DMG_UP)));
        // DMG_DOWN
        DMG_DOWN -= item.dmg_down;
        player.setDmg_down(DMG_DOWN);
        //HP
        HP -= item.hp;
        player.setMaxHealth(DEFAULT_MAXHEALTH + HP);
        // DEF
        if (item.def_up >= 1) DEF_flat -= item.def_up;
        else DEF_percent -= item.def_up;
        DEF_flat -= item.def;
        player.applyDef((int) (DEFAULT_DEF * (1 + DEF_percent) + DEF_flat));

        item.isEquipped = false;
    }

    public void applyEffect(Item item) {
        final Player player = playing.getPlayer();
        
        if(item.slot < 0) {
        	 player.applyHeal(item.heal);
             // MANA
             player.applyMana(item.mana);
             return;
        }
        // ATK
        ATK_flat += item.atk;
        ATK_percent += item.atk_up;
        player.setAttack((int) (DEFAULT_ATTACK * (1 + ATK_percent) + ATK_flat));
        // DMG
        int currAtk = player.getAttack();
        DMG_UP += item.dmg_up;
        player.setDamage((int) (currAtk * (1 + DMG_UP)));
        // DMG_DOWN
        DMG_DOWN += item.dmg_down;
        player.setDmg_down(DMG_DOWN);
        // DEF
        if (item.def_up >= 1) DEF_flat += item.def_up;
        else DEF_percent += item.def_up;
        DEF_flat += item.def;
        player.applyDef((int) (DEFAULT_DEF * (1 + DEF_percent) + DEF_flat));
        // HEALTH
        HP += item.hp;
        player.setMaxHealth(DEFAULT_MAXHEALTH + HP);
        // HEAL
       

        if (item.heal ==0 && item.mana==0) item.isEquipped = true;
    }

}