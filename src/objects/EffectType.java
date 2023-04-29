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
/*
index	effect
0	ATK_UP
1	HP_UP
2	DEF_UP
3	SPEED_UP
4	DMG_UP
5	DMG_DOWN
6	HEAL
7	MANA
*/
