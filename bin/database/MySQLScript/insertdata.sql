
INSERT INTO accounts(email, username, password, nickname, playerID) VALUES
("test1@gmail.com","admin","admin","BuonNguQua","[321]");
CALL getAccounts();

INSERT INTO `task` (`id`, `tasks`, `mapTasks`) VALUES
(0, '[0,-2,0]', '[0,-2,0]'),
(1, '[0,-2,0]', '[0,-2,0]'),
(2, '[0,-2,0]', '[0,-2,0]');
INSERT INTO `tasktemplate` (`taskId`, `name`, `detail`, `subNames`, `counts`) VALUES
(0, 'Mission kill NightBorne', 'ốc sên (Map1)', '[\"talk to the blue wizard\",\"kill NightBorne 0\",\"talk to the blue wizard\"]', '[-1,2,-1]'),
(1, 'Mission kill NightBorne', 'ốc sên (Map1)', '[\"talk to the blue wizard\",\"kill NightBorne\",\"talk to the blue wizard\"]', '[-1,2,-1]'),
(2, 'Mission kill NightBorne', 'NightBorne', '[\"talk to the blue wizard\",\"kill NightBorne 2\",\"talk to the blue wizard\"]', '[-1,2,-1]');
INSERT INTO `player` (`playerId`, `ctaskId`, `ctaskIndex`, `ctaskCount`, `cspeed`, `cName`, `cEXP`, `cExpDown`, `cLevel`, `xu`, `InfoMap`, `vip`) VALUES
(321,0,-1,1,2,'admin',0,0,1,0,'[0,400,500]',0);
INSERT INTO `mobtemplate` (`mobTemplateId`, `type`, `name`, `hp`, `isBoss`, `rangeMove`, `speed`, `isAttack`) VALUES
(0, 0, 'NightBorne', 15, 0, 0, 1, 0);
INSERT INTO `npctemplate` (`id`, `name`, `menu`) VALUES
(0, 'NPC_Wizard1', '[[\"Nói chuyện\"]]');
CALL getAccountsInf();
CALL getAccounts();
CALL getClientInf();
SELECT * FROM tasktemplate;
SELECT * FROM player;
SELECT * FROM mobtemplate;
SELECT * FROM npctemplate;




