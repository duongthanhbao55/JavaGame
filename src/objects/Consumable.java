package objects;

import Template.ItemTemplate;
import entities.Player;

public class Consumable extends Item{
    public Consumable(int x, int y, int objType, ItemTemplate itemTemplate) {
        super(x, y, objType, itemTemplate);
    }

    public void use(Player player){
        player.changeHealth(heal);
        player.changeMana(mana);
        player.setAttack((int) (player.getAttack()*(1+atk_up)));
        quantity--;
    }
}
