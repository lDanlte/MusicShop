-- // init persistent logins
-- Migration SQL that makes the change goes here.

create table persistent_logins (
  username  varchar(64) not null,
  series    varchar(64) primary key,
  token     varchar(64) not null,
  last_used timestamp   not null
);

-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE persistent_logins;
