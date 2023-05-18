 USE gamedb;

/*!40101 SET NAMES utf8mb4 */;

 -- drop table item;
-- SET SESSION AUTO_INCREMENT_OFFSET  = 3000;
CREATE TABLE IF NOT EXISTS Item  (
    itemID TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slot TINYINT NOT NULL DEFAULT -1,
    filename VARCHAR(34) NOT NULL,
    ability TEXT(500) DEFAULT NULL,
    description TEXT(500) DEFAULT NULL,
    effect VARCHAR(60) DEFAULT NULL,
    gold SMALLINT DEFAULT 0
);

-- set first id value
-- ALTER TABLE Item AUTO_INCREMENT=2001;

-- drop table item;
-- TRUNCATE item;
-- drop trigger new_item_insert;


-- DEFAULT = NONE for ability & DESC
--DELIMITER //
--CREATE TRIGGER new_item_insert BEFORE INSERT ON item
--FOR EACH ROW
--	BEGIN
--		SET
--        NEW.ability = if (NEW.ability IS NULL,'None',NEW.ability),
--		NEW.description = if ( NEW.itemdesc IS NULL , 'None' , NEW.itemdesc);
--		-- NEW.effect = if (NEW.effect IS NULL , '[]',NEW.effect);
--	END//
--DELIMITER ;