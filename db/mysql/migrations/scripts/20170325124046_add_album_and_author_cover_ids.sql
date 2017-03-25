-- // add album and author cover ids
-- Migration SQL that makes the change goes here.

ALTER TABLE `music_store`.`authors`
    ADD `cover_id` CHAR(24);

ALTER TABLE `music_store`.`albums`
    ADD `cover_id` CHAR(24);

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE  `music_store`.`authors`
    DROP COLUMN `cover_id`;

ALTER TABLE `music_store`.`albums`
    DROP COLUMN `cover_id`;
