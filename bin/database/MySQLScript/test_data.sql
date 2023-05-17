-- STEP 1: Create database
CREATE DATABASE IF NOT EXISTS gamedb;
use gamedb;
-- STEP 2: Create table
DROP TABLE accounts;
-- DROP TABLE accounts_inf;
CREATE TABLE accounts(
	user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL UNIQUE,
    nickname VARCHAR(12) UNIQUE,
    playerID VARCHAR(100) DEFAULT '[]',
    create_at DATETIME
);
-- CREATE TABLE accounts_inf(
-- 	user_id INT PRIMARY KEY AUTO_INCREMENT,
--     email VARCHAR(100),
--     first_name VARCHAR(20),
--     last_name VARCHAR(20)
-- );

-- CREATE TABLE accounts(
-- 	user_id INT,
--     user_name VARCHAR(50),
--     pass_word VARCHAR(100),
--     nick_name VARCHAR(12)
-- );

CREATE TABLE task (
  id tinyint(4) NOT NULL,
  tasks varchar(500) NOT NULL DEFAULT '[]',
  mapTasks varchar(500)NOT NULL DEFAULT '[]'
);

CREATE TABLE `tasktemplate` (
  `taskId` smallint(6) NOT NULL PRIMARY KEY,
  `name` varchar(100)NOT NULL,
  `detail` varchar(500)NOT NULL,
  `subNames` varchar(5000) NOT NULL DEFAULT '[]',
  `counts` varchar(5000) NOT NULL DEFAULT '[]'
);

DROP TABLE `player`;

CREATE TABLE `player` (
  `userId` int,
  `playerId` int(11) NOT NULL PRIMARY KEY,
  `inventoryId` int(11) NOT NULL,
  `ctaskId` tinyint(4) NOT NULL DEFAULT '0',
  `ctaskIndex` tinyint(4) NOT NULL DEFAULT '-1',
  `ctaskCount` smallint(6) NOT NULL DEFAULT '0',
  `cspeed` tinyint(1) NOT NULL DEFAULT '4',
  `cName` varchar(20) NOT NULL,
  `cEXP` bigint(20) NOT NULL DEFAULT '0',
  `cExpDown` bigint(20) NOT NULL DEFAULT '0',
  `cLevel` int(10) NOT NULL DEFAULT '1',
  `xu` int(11) NOT NULL DEFAULT '0',
  `idMap` smallint NOT NULL DEFAULT '0',
  `InfoMap` varchar(50) NOT NULL DEFAULT '[400,500]',
  `vip` tinyint(4) DEFAULT '0',
   FOREIGN KEY (`userId`) REFERENCES accounts(user_id),
   FOREIGN KEY (`ctaskId`) REFERENCES `tasktemplate`(`taskId`),
   FOREIGN KEY (`idMap`) REFERENCES `maptemplate`(`id`),
   FOREIGN KEY(`inventoryId`) REFERENCES `inventory`(`id`)
);

CREATE TABLE `mobtemplate` (
  `mobTemplateId` smallint(6) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `name` varchar(100)NOT NULL,
  `hp` int(11) NOT NULL,
  `isBoss` tinyint(1) NOT NULL,
  `rangeMove` tinyint(4) NOT NULL,
  `speed` tinyint(4) NOT NULL,
  `isAttack` tinyint(1) NOT NULL DEFAULT '1'
);

CREATE TABLE `npctemplate` (
  `id` tinyint(4) NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  `menu` varchar(2000) NOT NULL
);
--
-- Cấu trúc bảng cho bảng `maptemplate`
--
DROP TABLE `maptemplate`;

CREATE TABLE `maptemplate` (
  `id` smallint(6) NOT NULL PRIMARY KEY,
  `mapName` varchar(100) COLLATE utf8_bin NOT NULL,
  `mapDescription` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '',
  `WgoX` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `WgoY` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `WmapID` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `npcX` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `npcY` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `npcID` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `mobID` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `mobLevel` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `mobX` varchar(1000) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `mobY` varchar(1000) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `mobStatus` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `moblevelBoss` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `mobRefreshTime` varchar(1000) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `bgID` tinyint(4) NOT NULL
);

CREATE TABLE `inventory`(
	`id` smallint(6) NOT NULL PRIMARY KEY,
	`itemId` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '[]',
    `itemIndex` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '[]'
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
-- ADD FOREING KEY
-- ALTER TABLE accounts
-- ADD CONSTRAINT fk_userId
-- FOREIGN KEY (user_id) REFERENCES accounts_inf(user_id);

-- STEP 3: ADD DATA SET

-- STEP 4: Create indexs
CREATE INDEX username_idx
ON accounts(username);

CREATE INDEX nickname_idx
ON accounts(nickname);

CREATE INDEX email_idx
ON accounts(email);

CREATE INDEX password_idx
ON accounts(`password`);
-- STEP 5: Create functions
-- DROP FUNCTION findEmail;
-- DROP FUNCTION findNickname;
-- DROP FUNCTION findPassword;
-- DROP FUNCTION findUsername;
-- DROP FUNCTION haveNickname;
SET GLOBAL log_bin_trust_function_creators = 1;
DELIMITER $$
CREATE FUNCTION findUsername(f_username VARCHAR(100))
RETURNS VARCHAR(100)
NOT DETERMINISTIC
BEGIN
	DECLARE result VARCHAR(100);
    SET result = (SELECT username FROM accounts WHERE username = f_username);
    RETURN result;
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION findNickname(f_nickname VARCHAR(12))
RETURNS VARCHAR(12)
NOT DETERMINISTIC
BEGIN
	DECLARE result VARCHAR(12);
    SET result = (SELECT nickname FROM accounts WHERE nickname = f_nickname);
    RETURN result;
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION findEmail(f_email VARCHAR(100))
RETURNS VARCHAR(100)
NOT DETERMINISTIC
BEGIN
	DECLARE result VARCHAR(100);
    SET result = (SELECT email FROM accounts WHERE email = f_email);
    RETURN result;
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION findPassword(f_username VARCHAR(100))
RETURNS VARCHAR(100)
NOT DETERMINISTIC
BEGIN
	DECLARE result VARCHAR(100);
    SET result = (SELECT `password` FROM accounts WHERE username = f_username);
    RETURN result;
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION haveNickname(f_username VARCHAR(100))
RETURNS VARCHAR(12)
NOT DETERMINISTIC
BEGIN
	DECLARE result VARCHAR(12);
    SET result = (SELECT nickname FROM accounts WHERE username = f_username);
    RETURN result;
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION findPlayerID(f_username VARCHAR(100))
RETURNS VARCHAR(100)
NOT DETERMINISTIC
BEGIN
	DECLARE result VARCHAR(100);
    SET result = (SELECT playerID FROM accounts WHERE username = f_username);
    RETURN result;
END $$
DELIMITER ;

-- STEP 6: Create stored procedure

-- DROP PROCEDURE find_nickname;
-- DROP PROCEDURE find_username;
-- DROP PROCEDURE getAccounts;
-- DROP PROCEDURE getAccountsInf;
-- DROP PROCEDURE getClientInf;
-- DROP PROCEDURE setNickname;

DELIMITER $$
CREATE PROCEDURE setNickname(IN f_username VARCHAR(100),IN n_nickname VARCHAR(12))
BEGIN
	UPDATE accounts
    SET nickname = n_nickname
    WHERE username = f_username;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE setPlayerID(IN n_playerID VARCHAR(100), IN f_username VARCHAR(100))
BEGIN
	UPDATE accounts
    SET playerID = n_playerID
    WHERE username = f_username;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE getAccounts()
BEGIN
	SELECT * FROM accounts;
END $$
DELIMITER ;

-- STEP 7: Create trigger
-- CREATE TRIGGER before_insert_account
-- BEFORE INSERT ON accounts
-- FOR EACH ROW
-- SET NEW.user_id = (SELECT accounts_inf.user_id FROM accounts_inf ORDER BY accounts_inf.user_id DESC LIMIT 1);

CREATE TRIGGER before_insert_accounts
BEFORE INSERT ON accounts
FOR EACH ROW
SET NEW.create_at = NOW();

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


INSERT INTO `maptemplate` (`id`, `mapName`, `mapDescription`, `WgoX`, `WgoY`, `WmapID`, `npcX`, `npcY`, `npcID`, `mobID`, `mobLevel`, `mobX`, `mobY`, `mobStatus`, `moblevelBoss`, `mobRefreshTime`,`bgID`) VALUES
('0', 'Castle', '', '[0,3839]', '[400,550]', '[3,2]','[828]', '[312]', '[0]', '[0,0,0,0]', '[1,1,1,1]', '[1620,1572,1524,1500]', '[360,408,408,408]', '[0,0,0,0]','[0,0,0,0]', '[45000,45000,45000,45000]', '0'),
('1', 'Castle1', '', '[0,4319]', '[400,550]', '[0,2]','[828]', '[312]', '[0]', '[0,0,0,0]', '[1,1,1,1]', '[1620,1572,1524,1500]', '[360,408,408,408]', '[0,0,0,0]','[0,0,0,0]', '[45000,45000,45000,45000]', '0'),
('2', 'Castle2', '', '[0,4319]', '[400,700]', '[1,0]','[400]', '[312]', '[0]', '[0,0,0,0]', '[1,1,1,1]', '[1620,1572,1524,1500]', '[360,408,408,408]', '[0,0,0,0]','[0,0,0,0]', '[45000,45000,45000,45000]', '0');
INSERT INTO accounts(email, username, password, nickname, playerID) VALUES
("test1@gmail.com","admin","admin","BuonNguQua","[321]");
CALL getAccounts();

INSERT INTO `task` (`id`, `tasks`, `mapTasks`) VALUES
(0, '[0,-2,0]', '[0,-2,0]'),
(1, '[0,-2,0]', '[0,-2,0]'),
(2, '[0,-2,0]', '[0,-2,0]');
INSERT INTO `inventory` (`id`, `itemId`, `itemIndex`) VALUES
(0,'[0,1,2]','[0,1,2]');

INSERT INTO `tasktemplate` (`taskId`, `name`, `detail`, `subNames`, `counts`) VALUES
(0, 'Mission kill NightBorne', 'ốc sên (Map1)', '[\"talk to the blue wizard\",\"kill NightBorne 0\",\"talk to the blue wizard\"]', '[-1,2,-1]'),
(1, 'Mission kill NightBorne', 'ốc sên (Map1)', '[\"talk to the blue wizard\",\"kill NightBorne\",\"talk to the blue wizard\"]', '[-1,2,-1]'),
(2, 'Mission kill NightBorne', 'NightBorne', '[\"talk to the blue wizard\",\"kill NightBorne 2\",\"talk to the blue wizard\"]', '[-1,2,-1]');
INSERT INTO `player` (`playerId`, `ctaskId`, `ctaskIndex`, `ctaskCount`, `cspeed`, `cName`, `cEXP`, `cExpDown`, `cLevel`, `xu`,`idMap`, `InfoMap`, `vip`) VALUES
('321', '0', '-1', '1', '2', 'admin', '0', '0', '1', '0', '0', '[700,500]', '0');
INSERT INTO `mobtemplate` (`mobTemplateId`, `type`, `name`, `hp`, `isBoss`, `rangeMove`, `speed`, `isAttack`) VALUES
(0, 0, 'NightBorne', 15, 0, 0, 1, 0);
INSERT INTO `npctemplate` (`id`, `name`, `menu`) VALUES
(0, 'NPC_Wizard1', '[[\"Nói chuyện\"]]');

