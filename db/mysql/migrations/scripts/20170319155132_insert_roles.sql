-- // insert roles
-- Migration SQL that makes the change goes here.


INSERT INTO `roles` (`id`,`role`) VALUES (1,'Admin');
INSERT INTO `roles` (`id`,`role`) VALUES (2,'Author');
INSERT INTO `roles` (`id`,`role`) VALUES (3,'User');


-- //@UNDO
-- SQL to undo the change goes here.

DELETE FROM roles WHERE id in (1, 2, 3);
