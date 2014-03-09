-- --------------------------------------------------------
--
-- Table structure for table `creature_template`
--
-- --------------------------------------------------------

DROP TABLE IF EXISTS `creature_template`;
CREATE TABLE `creature_template` (
  `ID` int(4) NOT NULL,
  `Name` varchar(16) NOT NULL,
  `Faction` int(2) NOT NULL,
  `Image` varchar(255) NOT NULL,
  PRIMARY KEY ( `ID`),
  UNIQUE (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
