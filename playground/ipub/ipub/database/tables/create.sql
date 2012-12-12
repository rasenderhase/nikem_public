CREATE TABLE  `reanim8or`.`ipub_publications` (
`guid` VARCHAR( 255 ) NOT NULL ,
`autor_name` VARCHAR( 255 ) NOT NULL ,
`autor_vorname` VARCHAR( 255 ) NOT NULL ,
`titel` VARCHAR( 1020 ) NOT NULL ,
`institut` VARCHAR( 255 ) NOT NULL ,
`nummer` DECIMAL NOT NULL ,
`jahr` DATE NOT NULL ,
`aend_user` VARCHAR( 100 ) NOT NULL ,
`aend_ts` TIMESTAMP NOT NULL ,
`status` VARCHAR( 10 ) NOT NULL ,
`owner_user` VARCHAR( 100 ) NOT NULL ,
`sort_num` DECIMAL NOT NULL ,
PRIMARY KEY (  `guid` )
) ENGINE = INNODB;
