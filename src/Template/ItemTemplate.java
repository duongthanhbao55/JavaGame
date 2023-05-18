package Template;

public class ItemTemplate {
    public byte id;

    public String name;
    public int atk;
    public int def;
    public int hp;
    public float atk_up;
    public float hp_up;
    public float def_up;
    public float speed_up;
    public float dmg_up;
    public float dmg_down;
    public int heal;
    public int mana;
    public byte slot;
    public String ability;
    public String description;
    public String filename;
    public int gold;

    @Override
    public String toString() {
        return "ItemTemplate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", atk=" + atk +
                ", def=" + def +
                ", hp=" + hp +
                ", atk_up=" + atk_up +
                ", hp_up=" + hp_up +
                ", def_up=" + def_up +
                ", speed_up=" + speed_up +
                ", dmg_up=" + dmg_up +
                ", dmg_down=" + dmg_down +
                ", heal=" + heal +
                ", mana=" + mana +
                ", slot=" + slot +
                ", ability='" + ability + '\'' +
                ", description='" + description + '\'' +
                ", filename='" + filename + '\'' +
                ", gold=" + gold +
                '}';
    }
}
