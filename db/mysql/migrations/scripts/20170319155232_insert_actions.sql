-- // insert actions
-- Migration SQL that makes the change goes here.

INSERT INTO `actions` (`id`,`description`) VALUES (1,'Покупка альбома');
INSERT INTO `actions` (`id`,`description`) VALUES (2,'Перевод денег на счет');
INSERT INTO `actions` (`id`,`description`) VALUES (3,'Перевод денег со счета');

-- //@UNDO
-- SQL to undo the change goes here.

DELETE FROM actions WHERE id IN (1, 2, 3);
