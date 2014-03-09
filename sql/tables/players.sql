-- --------------------------------------------------------
--
-- Table structure for table `players`
--
-- --------------------------------------------------------

DROP TABLE IF EXISTS `players`;
CREATE TABLE `players` (
  `Id`   int(2) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Level` int(11) NOT NULL,
  `Class` varchar(15) NOT NULL,
  `X-Pos` int(3) NOT NULL,
  `Y-Pos` int(3) NOT NULL,
  `Online` int(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
