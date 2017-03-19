-- // drop user token column
-- Migration SQL that makes the change goes here.

ALTER TABLE `music_store`.`users`
  DROP COLUMN `token`;

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE `music_store`.`users`
  ADD COLUMN `token` BINARY(16);
