
-- // add id of file for track
-- Migration SQL that makes the change goes here.

ALTER TABLE `music_store`.`tracks`
    ADD `file_id` CHAR(24);

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE `music_store`.`tracks`
    DROP COLUMN `file_id`;
