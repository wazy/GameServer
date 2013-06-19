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

-- some example players
INSERT INTO `gameDB`.`players` (`Id`, `Name`, `Level`, `Class`, `X-Pos`, `Y-Pos`, `Online`) VALUES (1, 'Player1', '2', 'Rog', '100', '0', '1');
INSERT INTO `gameDB`.`players` (`Id`, `Name`, `Level`, `Class`, `X-Pos`, `Y-Pos`, `Online`) VALUES (2, 'Player2', '4', 'Wer', '50', '25', '0');
INSERT INTO `gameDB`.`players` (`Id`, `Name`, `Level`, `Class`, `X-Pos`, `Y-Pos`, `Online`) VALUES (3, 'Player3', '15', 'Ret', '84', '26', '1');
INSERT INTO `gameDB`.`players` (`Id`, `Name`, `Level`, `Class`, `X-Pos`, `Y-Pos`, `Online`) VALUES (4, 'Player4', '15', 'Ret', '84', '26', '1');
INSERT INTO `gameDB`.`players` (`Id`, `Name`, `Level`, `Class`, `X-Pos`, `Y-Pos`, `Online`) VALUES (5, 'Player5', '15', 'Ret', '40', '36', '1');
INSERT INTO `gameDB`.`players` (`Id`, `Name`, `Level`, `Class`, `X-Pos`, `Y-Pos`, `Online`) VALUES (6, 'Player6', '15', 'Ret', '74', '16', '1');
