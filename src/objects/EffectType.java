package objects;

public enum EffectType {

    // Raw stats buffs
    ATK_UP, DEF_UP, HP_UP, SPEED_UP,

    // Combat dmg calculations
    DMG_UP, // Increase damage dealt by wielder
    DMG_DOWN, // Decrease damage wielder take

    //Utility
    HEAL, // Restore HP points
    MANA, // Restore Mana points
    OTHER; // Others
}
