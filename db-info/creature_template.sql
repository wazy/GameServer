CREATE TABLE  `gameDB`.`creature_template` (
`ID` INT( 4 ) NOT NULL ,
`Name` VARCHAR( 16 ) NOT NULL ,
`Faction` INT( 2 ) NOT NULL ,
`Image` VARCHAR( 255 ) NOT NULL ,
PRIMARY KEY (  `ID` ) ,
UNIQUE (
`Name`
)
