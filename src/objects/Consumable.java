package objects;

import Template.ItemTemplate;
import entities.Player;

public class Consumable extends Item{
    public Consumable(int x, int y, int objType, ItemTemplate itemTemplate) {
        super(x, y, objType, itemTemplate);
        heal = itemTemplate.heal;
        mana = itemTemplate.mana;
        atk_up = itemTemplate.atk_up;
        hp_up = itemTemplate.hp_up;
        def_up = itemTemplate.def_up;
        speed_up = itemTemplate.speed_up;
    }

    public void use(Player player){
        player.changeHealth(heal);
        player.changeMana(mana);
        player.setATK((int) (player.getATK()*(1+atk_up)));
        quantity--;
    }
}
