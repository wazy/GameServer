-- --------------------------------------------------------
--
-- Table structure for table `accounts`
--
-- --------------------------------------------------------

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `Username` varchar(12) NOT NULL,
  `Password` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
