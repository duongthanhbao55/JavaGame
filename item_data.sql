USE TEST1;

/*!40101 SET NAMES utf8mb4 */;

SET SESSION AUTO_INCREMENT_OFFSET  = 3000;
CREATE TABLE IF NOT EXISTS Item  (
    itemID INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    atk INT DEFAULT 0,
    def INT DEFAULT 0,
    hp INT DEFAULT 0,
    slot TINYINT NOT NULL DEFAULT 0,
    filename VARCHAR(30) NOT NULL,
    ability TEXT DEFAULT NULL,
    itemDesc TEXT DEFAULT NULL
);
-- set first id value
ALTER TABLE Item AUTO_INCREMENT=2001;

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
	END//
DELIMITER ;



-- INSERT DATA
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Anvil',0,0,0,0,'anvil.png','None','a metalworking tool to forge your 1s and 2s');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Basic Arm Guard',0,50,100,1,'arm_guard.png','Resist incoming damages by 20%','a guard to block a little of damages');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Basic Arrow',0,0,0,0,'arrow.png','None','good ol'' arrow');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Apprentice Magic Book',50,50,50,1,'book.png','Increase damages dealt by 20% and decrease incoming damages taken by 20%','spells written by an apprentice wizard, didn''t have any specialities yet');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Basic Boots',0,10,10,1,'boots_01.png','Increases move speed by 20%','basic boots you could ever find');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('New Boots',0,30,30,1,'boots_02.png','Increases move speed by 30%','fresh boots from your local shoesmiths');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Basic Bow',80,30,10,2,'bow_01.png','Increases move speed by 20%','basic wooden bow from your great grandparents, at least it''s lightweight!');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fancy Bow',200,100,100,2,'bow_02.png','For every enemies defeated, ATK increased by 5%','your granparents didn''t tell you about this fancy bow with golden string and gemstones. This is much heavier but at least it fills you with superpower!');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Golden Circlet',50,100,100,1,'circlet.png','For every enemies defeated, DEF increased by 15%','an arm ornament left by a mistress');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Three-Leafed Clover',0,0,0,1,'clover_leaf.png','One (1) random stat increased by 200','would this serve as a lucky charm?');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Compass',0,0,0,0,'compass.png','Return to a previous checkpoint or teleport to boss''s lair','a traveler''s best friend');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Cookies',0,0,0,0,'cookies.png','Restores 50 HP points','cookies, well, cookies');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Copper Ingot',0,0,0,0,'copper_ingot.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Copper Ore',0,0,0,0,'copper_ore.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fablic Clothe',150,150,100,1,'fablic_clothe.png','Increase damages dealt by 50% and decrease incoming damages taken by 30%','feels like a legendary hero');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fabric',0,0,0,0,'fabric.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Feather',0,0,0,0,'feather_a.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fish',0,0,0,0,'fish_a.png','Restores 50 HP points','raw fish, best to grill in a campfire');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fur',0,0,0,0,'fur_a.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Glasses',0,0,0,0,'glasses.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('A Traveler''s Gloves',0,50,50,1,'glove_01.png','Increases move speed by 20%','basic gloves for adventures');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Magical Gloves',200,100,50,1,'glove_02.png','Increases move speed by 30% and damages dealt by 40%','its 3 magical stones are doing something');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Glowing Dust',0,0,0,0,'glowing_dust.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Gold Ingot',0,0,0,0,'gold_ingot.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Gold Ore',0,0,0,0,'gold_ore.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Green Mushroom',0,0,0,0,'green_mushroom.png','Restores 50 HP points','is it edible?');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Basic Headgear',30,100,100,1,'headgear_01.png','Decreases incoming damages taken by 20%','basic headwear for the battlefield');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Hero Headgear',100,200,200,1,'headgear_02.png','Decreases incoming damages taken by 50%','a hero''s endeavor for their testaments');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Herb',0,0,0,0,'herb.png','Restores 150 HP points','the natural medicine');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fine Pickaxe',100,50,0,2,'hi_quality_pickaxe.png','Increases damages dealt by 30%','well-made by the best hands in town');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fine Scethe',100,50,0,2,'hi_quality_scethe.png','Increases damages dealt by 30%','well-made by the best hands in town');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fine Hoe',100,50,0,2,'hoe.png','Increases damages dealt by 30%','well-made by the best hands in town');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('HP Potion',0,0,0,0,'hp_potion.png','Restores 300 HP Points','bought from a local wizard store if you recall correctly');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Iron 1',20,50,50,1,'iron_1.png','Decreases incoming damages taken by 30%','basic unisex wear for the battlefield');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Iron Ingot',0,0,0,0,'iron_ingot.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Iron Ore',0,0,0,0,'iron_ore.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Leather 1',0,80,50,1,'leather_1.png','Decreases incoming damages taken by 20%','lightweight and waterproof');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Leather Arm Guard',0,80,50,1,'leather_arm_guard.png','Decreases incoming damages taken by 20%','looks cool');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Leather Boots',30,80,30,1,'leather_boots_01.png','Increases move speed by 30%','basic leather boots you could ever ask');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Worn Leather Boots',0,30,20,1,'leather_boots_02.png','Increases move speed by 10%','have trodden ten thousands miles');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Leather Glove',0,50,50,1,'leather_glove.png','Increases move speed by 30%','also looks cool');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Log',0,0,0,0,'log.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Mana Potion',0,0,0,0,'mana_potion.png','Restores 300 Mana Points','bought from a local wizard store if you recall correctly');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Matua',50,100,150,1,'mantua.png','Increases move speed by 30% and decreases incoming damages taken by 30%','mantua for a fabled hero');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Meat',0,0,0,0,'meat.png','Restores 50 HP points','raw meat, best to stew it first');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Basic Necklace',0,50,50,1,'necklace_01.png','Increases DEF by 100 points','feminine necklace belonged to some mistress');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Fine Necklace',0,80,80,1,'necklace_02.png','Increases DEF 200 points','feminine necklace belonged to some mistress, it seems having some power');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Onigiri',0,0,0,0,'onigiri.png','Restores 150 HP points','rice is life');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Pouch',0,0,0,0,'pouch.png','Increase your 0 box capacity by 100','damn, it''s empty');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Basic Quiver',0,0,0,0,'quiver.png','Organize your arrows in one (1) place: your arrows  quiver will count as 1 0 in your 0 box','helps you organize your arrows bunch');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Bronze Ring',0,50,50,1,'ring_01.png','None','engagement ring left by someone');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Ruby and Gold Ring',50,200,100,1,'ring_02.png','Decreases incoming damages taken by 40%','looks like a ring with magical power');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Sandwich',0,0,0,0,'sandwich.png','Restores 150 HP points','bread, egg, bacon, tomatoes, lettuces');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Scroll Leather',0,0,0,0,'scroll_leather.png','Increases ATK by 10%','an ancient wizard''s special spell');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Metal Shield',0,100,100,1,'shield_01.png','Decreases incoming damages taken by 45%',NULL);
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Imperial Shield',80,150,200,1,'shield_02.png','Decreases incoming damages taken by 45% and reflects 30% of absorbed damages','made by the Imperial''s proudest blacksmiths');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Blessed Spear',250,100,100,2,'spear_01.png','Increases ATK by 30% and for every enemies defeated, increases ATK by 5%','is blessed by the fire goddess');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Imperial Spear',200,100,100,2,'spear_02.png','Increases damages dealt by 20%','made by the Imperial''s proudest blacksmiths');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Stone Sword',250,80,80,2,'stone sword.png','Increases damages dealt by 45% and ATK by 30%','is definitely heavy as hell');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Stone',0,0,0,0,'stone.png','None','Material');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Basic Sword',150,80,80,2,'sword_01.png','Increases damages dealt by 20%','made by the Imperial''s proudest blacksmiths');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Blessed Sword',250,100,100,2,'sword_02.png','Increases ATK by 30% and for every enemies defeated, increases ATK by 5%','is blessed by the fire goddess');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Cat Tail',150,150,150,1,'tail.png','Increases damages dealt by 15%; decreases incoming damages taken by 15%; increases move speed by 20%','artificial cat tail ~ nyan');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Veggie',0,0,0,0,'veggie.png','Restores 100 HP points','what kind of salad shall you make?');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Apprentice Wand',100,200,80,2,'wand_01.png','Increases ATK and DEF by 30%','once belongs to a apprentice magus');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Young Magus Wand',200,300,150,2,'wand_02.png','Increases ATK and DEF by 45% and 2% for every enemies defeated','once belongs to a young magus');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Watering Can',0,0,0,0,'watering_can.png','None','you got to water to veggies');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Watermelon',0,0,0,0,'watermelon.png','Restores 100 HP points','fresh and juicy, not sure if it could fill your stomach');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Wooden Box',0,0,0,0,'wooden_box.png','Obtain one (1) random 0','what is inside?');
INSERT INTO item(Name,ATK,HP,DEF,Slot,Filename,Ability,itemdesc) VALUES ('Wooden Shield',20,100,100,2,'wooden_shield.png','Decreases incoming damages taken by 20%','a dream of every young lad');
