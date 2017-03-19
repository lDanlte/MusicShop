-- // alter length of users pass
-- Migration SQL that makes the change goes here.

ALTER TABLE `music_store`.`users`
  CHANGE COLUMN `pass` `pass` VARCHAR(64) NOT NULL;

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE `music_store`.`users`
  CHANGE COLUMN `pass` `pass` VARCHAR(32) NOT NULL;
