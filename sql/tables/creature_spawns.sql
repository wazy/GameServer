-- --------------------------------------------------------
--
-- Table structure for table `creature_spawns`
--
-- --------------------------------------------------------

DROP TABLE IF EXISTS `creature_spawns`;
CREATE TABLE `creature_spawns` (
  `GUID` int(4) NOT NULL,
  `Name` varchar(12) NOT NULL,
  `X-Pos` int(4) NOT NULL,
  `Y-Pos` int(4) NOT NULL,
  `Width` int(4) NOT NULL,
  `Height` int(4) NOT NULL,
  `Faction` int(11) NOT NULL,
  UNIQUE KEY `GUID` (`GUID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `creature_spawns` (`GUID`, `Name`, `X-Pos`, `Y-Pos`, `Width`, `Height`, `Faction`) VALUES
(1, 'Monstrous', 100, 100, 50, 75, 1),
(2, 'Construct', 200, 200, 50, 50, 1);