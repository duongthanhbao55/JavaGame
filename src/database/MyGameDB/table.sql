CREATE DATABASE mygame;
USE mygame;


CREATE TABLE accounts(
	user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    nickname VARCHAR(12) UNIQUE,
    create_at DATETIME
);

CREATE TABLE player(
	player_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    experience INT NOT NULL,
    attack INT NOT NULL DEFAULT 100,
    defense INT NOT NULL DEFAULT 100,
    health_potion INT NOT NULL DEFAULT 100,
    gold INT NOT NULL DEFAULT 0,
    coordinates_x INT NOT NULL,
    coordinates_y INT NOT NULL,
    
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES accounts(user_id)
);
CREATE TABLE item (
	item_id INT AUTO_INCREMENT PRIMARY KEY,
    `item_name` VARCHAR(100) NOT NULL,
	slot TINYINT NOT NULL DEFAULT 0,
    filename VARCHAR(100) NOT NULL,
    ability VARCHAR(200) DEFAULT NULL,
	`description` VARCHAR(200) DEFAULT NULL,
    effect VARCHAR(100) DEFAULT NULL
);

CREATE TABLE task(
	task_id INT PRIMARY KEY,
    task_name VARCHAR(100) NOT NULL,
    detail VARCHAR(200) NOT NULL,
    subname VARCHAR(200) NOT NULL DEFAULT '[]',
    counts VARCHAR(100) NOT NULL DEFAULT '[]'
);

CREATE TABLE inven (
	player_id INT,
    item_id INT,
    quantity INT DEFAULT 1,
    is_equipped TINYINT DEFAULT 0,
    `index` TINYINT,
    
    CONSTRAINT player_id_fk FOREIGN KEY (player_id) REFERENCES player(player_id),
    CONSTRAINT item_id_fk FOREIGN KEY (item_id) REFERENCES item(item_id)
);

CREATE TABLE doMission (
	player_id INT,
    task_id INT,
    is_completed TINYINT DEFAULT 0,
    
    CONSTRAINT dm_player_id_fk FOREIGN KEY (player_id) REFERENCES player(player_id),
    CONSTRAINT task_id_fk FOREIGN KEY (task_id) REFERENCES task(task_id)
);