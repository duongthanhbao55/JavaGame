CREATE DATABASE TEST1;
USE TEST1;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE TABLE `user` (
  `userid` int(11) NOT NULL,
  `user` varchar(20) COLLATE utf8_bin NOT NULL,
  `status` tinyint(11) NOT NULL DEFAULT '1',
  `password` varchar(30) COLLATE utf8_bin NOT NULL,
  `player` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `admin` int(11) NOT NULL DEFAULT '0',
  `created_at` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `tester` tinyint(4) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

INSERT INTO `user` (`userid`, `user`, `status`, `password`, `player`, `admin`, `created_at`, `email`, `tester`) VALUES
(1, 'admin', 1, 'admin', '[321]', 0, '2023-03-11 04:22:05', '', 0);

CREATE TABLE `task` (
  `id` tinyint(4) NOT NULL,
  `tasks` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `mapTasks` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '[]'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE `tasktemplate`;
--
-- Đang đổ dữ liệu cho bảng `task`
--

INSERT INTO `task` (`id`, `tasks`, `mapTasks`) VALUES
(0, '[0,-2,0]', '[0,-2,0]'),
(1, '[0,-2,0]', '[0,-2,0]'),
(2, '[0,-2,0]', '[0,-2,0]');

CREATE TABLE `tasktemplate` (
  `taskId` smallint(6) NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `detail` varchar(500) COLLATE utf8_bin NOT NULL,
  `subNames` varchar(5000) COLLATE utf8_bin NOT NULL DEFAULT '[]',
  `counts` varchar(5000) COLLATE utf8_bin NOT NULL DEFAULT '[]'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Đang đổ dữ liệu cho bảng `tasktemplate`
--
-- DROP TABLE `player`;
INSERT INTO `tasktemplate` (`taskId`, `name`, `detail`, `subNames`, `counts`) VALUES
(0, 'Mission kill NightBorne', 'ốc sên (Map1)', '[\"talk to the blue wizard\",\"kill NightBorne 0\",\"talk to the blue wizard\"]', '[-1,2,-1]'),
(1, 'Mission kill NightBorne', 'ốc sên (Map1)', '[\"talk to the blue wizard\",\"kill NightBorne\",\"talk to the blue wizard\"]', '[-1,2,-1]'),
(2, 'Mission kill NightBorne', 'NightBorne', '[\"talk to the blue wizard\",\"kill NightBorne 2\",\"talk to the blue wizard\"]', '[-1,2,-1]');

CREATE TABLE `player` (
  `playerId` int(11) NOT NULL,
  `ctaskId` tinyint(4) NOT NULL DEFAULT '0',
  `ctaskIndex` tinyint(4) NOT NULL DEFAULT '-1',
  `ctaskCount` smallint(6) NOT NULL DEFAULT '0',
  `cspeed` tinyint(1) NOT NULL DEFAULT '4',
  `cName` varchar(20) COLLATE utf8_bin NOT NULL,
  `cEXP` bigint(20) NOT NULL DEFAULT '0',
  `cExpDown` bigint(20) NOT NULL DEFAULT '0',
  `cLevel` int(10) NOT NULL DEFAULT '1',
  `xu` int(11) NOT NULL DEFAULT '0',
  `InfoMap` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '[22,400,500]',
  `vip` tinyint(4) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

INSERT INTO `player` (`playerId`, `ctaskId`, `ctaskIndex`, `ctaskCount`, `cspeed`, `cName`, `cEXP`, `cExpDown`, `cLevel`, `xu`, `InfoMap`, `vip`) VALUES
(321,0,-1,1,2,'admin',0,0,1,0,'[0,400,500]',0);

CREATE TABLE `mobtemplate` (
  `mobTemplateId` smallint(6) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `hp` int(11) NOT NULL,
  `isBoss` tinyint(1) NOT NULL,
  `rangeMove` tinyint(4) NOT NULL,
  `speed` tinyint(4) NOT NULL,
  `isAttack` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

INSERT INTO `mobtemplate` (`mobTemplateId`, `type`, `name`, `hp`, `isBoss`, `rangeMove`, `speed`, `isAttack`) VALUES
(0, 0, 'NightBorne', 15, 0, 0, 1, 0);