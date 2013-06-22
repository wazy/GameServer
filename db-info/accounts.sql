--
-- Database: `gameDB`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

DROP TABLE `accounts`;
CREATE TABLE `accounts` (
  `ID` int(4) NOT NULL,
  `Username` varchar(12) NOT NULL,
  `Password` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


ALTER TABLE  `accounts` CHANGE  `ID`  `ID` INT( 4 ) NOT NULL AUTO_INCREMENT;