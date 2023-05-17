-- USE gamedb;

/*!40101 SET NAMES utf8mb4 */;

 drop table item;
-- SET SESSION AUTO_INCREMENT_OFFSET  = 3000;
CREATE TABLE IF NOT EXISTS Item  (
    itemID TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slot TINYINT NOT NULL DEFAULT 0,
    filename VARCHAR(34) NOT NULL,
    ability TEXT(500) DEFAULT NULL,
    itemDesc TEXT(500) DEFAULT NULL,
    effect VARCHAR(60) DEFAULT NULL-- SET SESSION AUTO_INCREMENT_OFFSET  = 3000;
);
CREATE TABLE IF NOT EXISTS Item  (
    itemID TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slot TINYINT NOT NULL DEFAULT 0,
    filename VARCHAR(34) NOT NULL,
    ability TEXT(500) DEFAULT NULL,
    itemDesc TEXT(500) DEFAULT NULL,
    effect VARCHAR(60) DEFAULT NULL
);
-- set first id value
-- ALTER TABLE Item AUTO_INCREMENT=2001;

-- drop table item;
-- TRUNCATE item;
-- drop trigger new_item_insert;


-- DEFAULT = NONE for ability & DESC
DELIMITER //
CREATE TRIGGER new_item_insert BEFORE INSERT ON item 
FOR EACH ROW 
	BEGIN
		SET 
        NEW.ability = if (NEW.ability IS NULL,'None',NEW.ability),
		NEW.itemdesc = if ( NEW.itemdesc IS NULL , 'None' , NEW.itemdesc);
		-- NEW.effect = if (NEW.effect IS NULL , '[]',NEW.effect);
	END//
DELIMITER ;



-- INSERT DATA
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Anvil','0,0,0,0,0,0,0,0,0,0,0',-2,'None','anvil.png','a metalworking tool to forge your armors and weapons');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Basic Arm Guard','0,50,100,0,0,0,0,0,0.2,0,0',1,'Resist incoming damages by 20%','arm_guard.png','a guard to block a little of damages');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Basic Arrow','0,0,0,0,0,0,0,0,0,0,0',-3,'None','arrow.png','good ol'' arrow');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Apprentice Magic Book','50,50,50,0,0,0,0,0.2,0.2,0,0',10,'Increase damages dealt by 20% and decrease incoming damages taken by 20%','book.png','spells written by an apprentice wizard, didn''t have any specialities yet');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Basic Boots','0,10,10,0,0,0,0.2,0,0,0,0',5,'Increases move speed by 20%','boots_01.png','basic boots you could ever find');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('New Boots','0,30,30,0,0,0,0.3,0,0,0,0',5,'Increases move speed by 30%','boots_02.png','fresh boots from your local shoesmiths');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Basic Bow','80,30,10,0,0,0,0.2,0,0,0,0',0,'Increases move speed by 20%','bow_01.png','basic wooden bow from your great grandparents, at least it''s lightweight!');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fancy Bow','200,100,100,0,0,0,0,0,0,0,0',0,'For every enemies defeated, ATK increased by 5%','bow_02.png','your granparents didn''t tell you about this fancy bow with golden string and gemstones. This is much heavier but at least it fills you with superpower!');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Golden Circlet','50,100,100,0,0,0,0,0,0,0,0',1,'For every enemies defeated, DEF increased by 15%','circlet.png','an arm ornament left by a mistress');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Three-Leafed Clover','0,0,0,0,0,0,0,0,0,0,0',1,'One (1) random stat increased by 200','clover_leaf.png','would this serve as a lucky charm?');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Compass','0,0,0,0,0,0,0,0,0,0,0',-2,'Return to a previous checkpoint or teleport to boss''s lair','compass.png','a traveler''s best friend');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Cookies','0,0,0,0,0,0,0,0,0,50,0',-1,'Restores 50 HP points','cookies.png','cookies, well, cookies');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Copper Ingot','0,0,0,0,0,0,0,0,0,0,0',-3,'None','copper_ingot.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Copper Ore','0,0,0,0,0,0,0,0,0,0,0',-3,'None','copper_ore.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fablic Clothe','150,150,100,0,0,0,0,0.5,0.3,0,0',1,'Increase damages dealt by 50% and decrease incoming damages taken by 30%','fablic_clothe.png','feels like a legendary hero');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fabric','0,0,0,0,0,0,0,0,0,0,0',-3,'None','fabric.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Feather','0,0,0,0,0,0,0,0,0,0,0',-3,'None','feather_a.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fish','0,0,0,0,0,0,0,0,0,50,0',-1,'Restores 50 HP points','fish_a.png','raw fish, best to grill in a campfire');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fur','0,0,0,0,0,0,0,0,0,0,0',-3,'None','fur_a.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Glasses','0,0,0,0,0,0,0.2,0,0,0,0',-3,'None','glasses.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('A Traveler''s Gloves','0,50,50,0,0,0,0.3,0.4,0,0,0',1,'Increases move speed by 20%','glove_01.png','basic gloves for adventures');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Magical Gloves','200,100,50,0,0,0,0,0,0,0,0',1,'Increases move speed by 30% and damages dealt by 40%','glove_02.png','its 3 magical stones are doing something');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Glowing Dust','0,0,0,0,0,0,0,0,0,0,0',-3,'None','glowing_dust.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Gold Ingot','0,0,0,0,0,0,0,0,0,0,0',-3,'None','gold_ingot.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Gold Ore','0,0,0,0,0,0,0,0,0,0,0',-3,'None','gold_ore.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Green Mushroom','0,0,0,0,0,0,0,0,0,50,0',-1,'Restores 50 HP points','green_mushroom.png','is it edible?');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Basic Headgear','30,100,100,0,0,0,0,0,0.2,0,0',2,'Decreases incoming damages taken by 20%','headgear_01.png','basic headwear for the battlefield');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Hero Headgear','100,200,200,0,0,0,0,0,0.5,0,0',2,'Decreases incoming damages taken by 50%','headgear_02.png','a hero''s endeavor for their testaments');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Herb','0,0,0,0,0,0,0,0,0,150,0',-1,'Restores 150 HP points','herb.png','the natural medicine');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fine Pickaxe','100,50,0,0,0,0,0,0.3,0,0,0',0,'Increases damages dealt by 30%','hi_quality_pickaxe.png','well-made by the best hands in town');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fine Scethe','100,50,0,0,0,0,0,0.3,0,0,0',0,'Increases damages dealt by 30%','hi_quality_scethe.png','well-made by the best hands in town');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fine Hoe','100,50,0,0,0,0,0,0.3,0,0,0',0,'Increases damages dealt by 30%','hoe.png','well-made by the best hands in town');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('HP Potion','0,0,0,0,0,0,0,0,0,300,0',-1,'Restores 300 HP Points','hp_potion.png','bought from a local wizard store if you recall correctly');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Iron Armor','20,50,50,0,0,0,0,0,0.3,0,0',3,'Decreases incoming damages taken by 30%','iron_armor.png','basic unisex wear for the battlefield');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Iron Ingot','0,0,0,0,0,0,0,0,0,0,0',-3,'None','iron_ingot.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Iron Ore','0,0,0,0,0,0,0,0,0,0,0',-3,'None','iron_ore.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Leather Armor','0,80,50,0,0,0,0,0,0.2,0,0',3,'Decreases incoming damages taken by 20%','leather_armor.png','lightweight and waterproof');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Leather Arm Guard','0,80,50,0,0,0,0,0,0.2,0,0',8,'Decreases incoming damages taken by 20%','leather_arm_guard.png','looks cool');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Leather Boots','30,80,30,0,0,0,0.3,0,0,0,0',5,'Increases move speed by 30%','leather_boots_01.png','basic leather boots you could ever ask');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Worn Leather Boots','0,30,20,0,0,0,0.1,0,0,0,0',5,'Increases move speed by 10%','leather_boots_02.png','have trodden ten thousands miles');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Leather Glove','0,50,50,0,0,0,0.3,0,0,0,0',0,'Increases move speed by 30%','leather_glove.png','also looks cool');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Log','0,0,0,0,0,0,0,0,0,0,0',-3,'None','log.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Mana Potion','0,0,0,0,0,0,0,0,0,300,0',-1,'Restores 300 Mana Points','mana_potion.png','bought from a local wizard store if you recall correctly');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Matua','50,100,150,0,0,0,0.3,0,0.3,0,0',3,'Increases move speed by 30% and decreases incoming damages taken by 30%','mantua.png','mantua for a fabled hero');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Meat','0,0,0,0,0,0,0,0,0,50,0',-1,'Restores 50 HP points','meat.png','raw meat, best to stew it first');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Basic Necklace','0,50,50,0,0,100,0,0,0,0,0',7,'Increases DEF by 100 points','necklace_01.png','feminine necklace belonged to some mistress');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Fine Necklace','0,80,80,0,0,200,0,0,0,0,0',7,'Increases DEF 200 points','necklace_02.png','feminine necklace belonged to some mistress, it seems having some power');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Onigiri','0,0,0,0,0,0,0,0,150,0,0',-1,'Restores 150 HP points','onigiri.png','rice is life');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Pouch','0,0,0,0,0,0,0,0,0,0,0',-1,'Increase your item box capacity by 100','pouch.png','damn, it''s empty');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Basic Quiver','0,0,0,0,0,0,0,0,0,0,0',-2,'Organize your arrows in one (1) place: your arrows quiver will count as 1 item in your item box','quiver.png','helps you organize your arrows bunch');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Bronze Ring','0,50,50,0,0,0,0,0,0,0,0',1,'None','ring_01.png','engagement ring left by someone');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Ruby and Gold Ring','50,200,100,0,0,0,0,0,0.4,0,0',1,'Decreases incoming damages taken by 40%','ring_02.png','looks like a ring with magical power');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Sandwich','0,0,0,0,0,0,0,0,0,150,0',-1,'Restores 150 HP points','sandwich.png','bread, egg, bacon, tomatoes, lettuces');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Scroll Leather','0,0,0,0.1,0,0,0,0,0,0,0',-1,'Increases ATK by 10%','scroll_leather.png','an ancient wizard''s special spell');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Metal Shield','0,100,100,0,0,0,0,0,0.45,0,0',8,'Decreases incoming damages taken by 45%','shield_01.png','None');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Imperial Shield','80,150,200,0,0,0,0,0,0.45,0,0',8,'Decreases incoming damages taken by 45% and reflects 30% of absorbed damages','shield_02.png','made by the Imperial''s proudest blacksmiths');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Blessed Spear','250,100,100,0.3,0,0,0,0,0,0,0',0,'Increases ATK by 30% and for every enemies defeated, increases ATK by 5%','spear_01.png','is blessed by the fire goddess');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Imperial Spear','200,100,100,0,0,0,0,0.2,0,0,0',0,'Increases damages dealt by 20%','spear_02.png','made by the Imperial''s proudest blacksmiths');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Stone Sword','250,80,80,0.3,0,0,0,0.45,0,0,0',0,'Increases damages dealt by 45% and ATK by 30%','stone sword.png','is definitely heavy as hell');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Stone','0,0,0,0,0,0,0,0,0,0,0',-3,'None','stone.png','Material');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Basic Sword','150,80,80,0,0,0,0,0.2,0,0,0',0,'Increases damages dealt by 20%','sword_01.png','made by the Imperial''s proudest blacksmiths');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Blessed Sword','250,100,100,0.3,0,0,0,0,0,0,0',0,'Increases ATK by 30% and for every enemies defeated, increases ATK by 5%','sword_02.png','is blessed by the fire goddess');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Cat Tail','150,150,150,0,0,0,0,0.15,0,0,0',9,'Increases damages dealt by 15%; decreases incoming damages taken by 15%; increases move speed by 20%','tail.png','artificial cat tail ~ nyan');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Veggie','0,0,0,0,0,0,0,0,0,100,0',-1,'Restores 100 HP points','veggie.png','what kind of salad shall you make?');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Apprentice Wand','100,200,80,0.3,0,0.3,0,0,0,0,0',0,'Increases ATK and DEF by 30%','wand_01.png','once belongs to a apprentice magus');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Young Magus Wand','200,300,150,0.45,0,0.45,0,0,0,0,0',0,'Increases ATK and DEF by 45% and 2% for every enemies defeated','wand_02.png','once belongs to a young magus');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Watering Can','0,0,0,0,0,0,0,0,0,0,0',-2,'None','watering_can.png','you got to water to veggies');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Watermelon','0,0,0,0,0,0,0,0,0,100,0',-1,'Restores 100 HP points','watermelon.png','fresh and juicy, not sure if it could fill your stomach');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Wooden Box','0,0,0,0,0,0,0,0,0,0,0',-1,'Obtain one (1) random item','wooden_box.png','what is inside?');
INSERT INTO item(Name,Effect,Slot,Ability,Filename,itemdesc) VALUES ('Wooden Shield','20,100,100,0,0,0,0,0,0.2,0,0',8,'Decreases incoming damages taken by 20%','wooden_shield.png','a dream of every young lad');