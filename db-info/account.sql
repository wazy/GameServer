--
-- Database: `gameDB`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE IF NOT EXISTS `account` (
  `ID` int(4) NOT NULL,
  `Username` varchar(12) NOT NULL,
  `Password` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`ID`, `Username`, `Password`) VALUES
(1, 'user', '$2a$10$8iH4Y/JPBgcmZ0hbFIJMz.YpuEhVqhCZRyCvtaEDFFdx4eNCW/EEu');
