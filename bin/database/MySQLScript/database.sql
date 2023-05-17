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
    `password` VARCHAR(100) NOT NULL,
    nickname VARCHAR(12) UNIQUE,
    playerID VARCHAR(100) DEFAULT '[]',
    create_at DATETIME
);
ALTER TABLE accounts
ALTER playerID SET DEFAULT '[321]';
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
  id tinyint(4) NOT NULL, -- mã nhiệm vụ
  tasks varchar(500) NOT NULL DEFAULT '[]', -- tên nhiệm vụ
  mapTasks varchar(500)NOT NULL DEFAULT '[]' -- map thực hiện nhiệm vụ
);

CREATE TABLE `tasktemplate` (
  `taskId` smallint(6) NOT NULL, -- mã nhiệm vụ
  `name` varchar(100)NOT NULL,
  `detail` varchar(500)NOT NULL,
  `subNames` varchar(5000) NOT NULL DEFAULT '[]',
  `counts` varchar(5000) NOT NULL DEFAULT '[]'
);

CREATE TABLE `player` (
  `playerId` int(11) NOT NULL,
  `ctaskId` tinyint(4) NOT NULL DEFAULT '0',
  `ctaskIndex` tinyint(4) NOT NULL DEFAULT '-1',
  `ctaskCount` smallint(6) NOT NULL DEFAULT '0',
  `cspeed` tinyint(1) NOT NULL DEFAULT '4',
  `cName` varchar(20) NOT NULL,
  `cEXP` bigint(20) NOT NULL DEFAULT '0',
  `cExpDown` bigint(20) NOT NULL DEFAULT '0',
  `cLevel` int(10) NOT NULL DEFAULT '1',
  `xu` int(11) NOT NULL DEFAULT '0',
  `InfoMap` varchar(50) NOT NULL DEFAULT '[22,400,500]',
  `vip` tinyint(4) DEFAULT '0'
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




