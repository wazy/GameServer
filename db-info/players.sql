--
-- Database: `gameDB`
--

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

DROP TABLE `players`;
CREATE TABLE `players` (
  `Id`   int(2) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Level` int(11) NOT NULL,
  `Class` varchar(15) NOT NULL,
  `X-Pos` int(3) NOT NULL,
  `Y-Pos` int(3) NOT NULL,
  `Online` int(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

ALTER TABLE  `players` CHANGE  `Id`  `Id` INT( 4 ) NOT NULL AUTO_INCREMENT;

