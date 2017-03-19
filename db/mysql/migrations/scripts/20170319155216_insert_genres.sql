-- // insert genres
-- Migration SQL that makes the change goes here.

INSERT INTO `genres` (`id`,`name`) VALUES (1,'Альтевнативная/инди');
INSERT INTO `genres` (`id`,`name`) VALUES (2,'Блюз');
INSERT INTO `genres` (`id`,`name`) VALUES (3,'Джаз');
INSERT INTO `genres` (`id`,`name`) VALUES (4,'Кантри');
INSERT INTO `genres` (`id`,`name`) VALUES (5,'Металл');
INSERT INTO `genres` (`id`,`name`) VALUES (6,'Поп');
INSERT INTO `genres` (`id`,`name`) VALUES (7,'Регги');
INSERT INTO `genres` (`id`,`name`) VALUES (8,'Рок');
INSERT INTO `genres` (`id`,`name`) VALUES (9,'Саундтреки');
INSERT INTO `genres` (`id`,`name`) VALUES (10,'Электронная музыка');
INSERT INTO `genres` (`id`,`name`) VALUES (11,'Фолк');
INSERT INTO `genres` (`id`,`name`) VALUES (12,'Хип-хоп/рэп');
INSERT INTO `genres` (`id`,`name`) VALUES (13,'Пост-рок');
INSERT INTO `genres` (`id`,`name`) VALUES (14,'Эмбиент');

-- //@UNDO
-- SQL to undo the change goes here.

DELETE FROM genres WHERE id IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
