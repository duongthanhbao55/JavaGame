					-- TABLE ACCOUNT
-- CREATE INDEXS

CREATE INDEX username_idx
ON accounts(username);

CREATE INDEX nickname_idx
ON accounts(nickname);

CREATE INDEX email_idx
ON accounts(email);

CREATE INDEX password_idx
ON accounts(`password`);

-- CREATE STORED PROCEDURE

DELIMITER $$
CREATE PROCEDURE setNickname(IN f_username VARCHAR(100),IN n_nickname VARCHAR(12))
BEGIN
	UPDATE accounts
    SET nickname = n_nickname
    WHERE username = f_username;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE getAccounts()
BEGIN
	SELECT * FROM accounts;
END $$
DELIMITER ;

-- CREATE FUNCTION

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

-- CREATE TRIGGER

CREATE TRIGGER before_insert_accounts
BEFORE INSERT ON accounts
FOR EACH ROW
SET NEW.create_at = NOW();

					-- TABLE ITEM

-- CREATE TRIGGER

DELIMITER //
CREATE TRIGGER new_item_insert BEFORE INSERT ON item 
FOR EACH ROW 
	BEGIN
		SET 
        NEW.ability = if (NEW.ability IS NULL,'None',NEW.ability),
		NEW.`description` = if ( NEW.`description` IS NULL , 'None' , NEW.`description`);
		-- NEW.effect = if (NEW.effect IS NULL , '[]',NEW.effect);
	END//
DELIMITER ;
