
CREATE TABLE Actions
( 
	action_id            integer IDENTITY ( 1,1 ) ,
	description          varchar(32)  NOT NULL 
)
go



ALTER TABLE Actions
	ADD CONSTRAINT XPKActions PRIMARY KEY  CLUSTERED (action_id ASC)
go



CREATE TABLE Albums
( 
	album_id             uniqueidentifier  NOT NULL 
	CONSTRAINT UUID_2036939779
		 DEFAULT  NEWID(),
	title                varchar(48)  NOT NULL ,
	price                money  NOT NULL ,
	release_date         datetime  NOT NULL ,
	description          text  NOT NULL ,
	author_id            uniqueidentifier  NULL ,
	add_date             datetime  NOT NULL 
)
go



ALTER TABLE Albums
	ADD CONSTRAINT XPKAlbums PRIMARY KEY  CLUSTERED (album_id ASC)
go



CREATE TABLE Albums_to_Genres
( 
	genre_id             integer  NOT NULL ,
	album_id             uniqueidentifier  NOT NULL 
)
go



CREATE TABLE Authors
( 
	author_id            uniqueidentifier  NOT NULL 
	CONSTRAINT UUID_300875039
		 DEFAULT  NEWID(),
	name                 varchar(48)  NOT NULL ,
	user_id              uniqueidentifier  NOT NULL ,
	description          text  NOT NULL 
)
go



ALTER TABLE Authors
	ADD CONSTRAINT XPKAuthors PRIMARY KEY  CLUSTERED (author_id ASC)
go



CREATE TABLE Genres
( 
	genre_id             integer IDENTITY ( 1,1 ) ,
	name                 varchar(32)  NOT NULL 
)
go



ALTER TABLE Genres
	ADD CONSTRAINT XPKGenres PRIMARY KEY  CLUSTERED (genre_id ASC)
go



CREATE TABLE Roles
( 
	role_id              integer IDENTITY ( 1,1 ) ,
	role                 varchar(16)  NOT NULL 
)
go



ALTER TABLE Roles
	ADD CONSTRAINT XPKRoles PRIMARY KEY  CLUSTERED (role_id ASC)
go



CREATE TABLE Tracks
( 
	track_id             uniqueidentifier  NOT NULL 
	CONSTRAINT UUID_1769286671
		 DEFAULT  NEWID(),
	duration             smallint  NOT NULL ,
	size                 integer  NOT NULL ,
	album_id             uniqueidentifier  NOT NULL ,
	name                 varchar(64)  NOT NULL ,
	bitrate              integer  NOT NULL ,
	pos                  tinyint  NOT NULL 
)
go



ALTER TABLE Tracks
	ADD CONSTRAINT XPKTracks PRIMARY KEY  CLUSTERED (track_id ASC)
go



CREATE TABLE Trade_History
( 
	history_id           uniqueidentifier  NOT NULL 
	CONSTRAINT UUID_2100309859
		 DEFAULT  NEWID(),
	price                money  NOT NULL ,
	datetime             datetime  NOT NULL 
	CONSTRAINT CURRENT_TIMESTAMP_626505119
		 DEFAULT  CURRENT_TIMESTAMP,
	action_id            integer  NOT NULL ,
	user_id              uniqueidentifier  NOT NULL ,
	album_id             uniqueidentifier  NULL 
)
go



ALTER TABLE Trade_History
	ADD CONSTRAINT XPKTrade_History PRIMARY KEY  CLUSTERED (history_id ASC)
go



CREATE TABLE Users
( 
	user_id              uniqueidentifier  NOT NULL 
	CONSTRAINT UUID_1868723328
		 DEFAULT  NEWID(),
	login                varchar(24)  NOT NULL ,
	pass                 varchar(32)  NOT NULL ,
	email                varchar(32)  NOT NULL ,
	wallet               money  NOT NULL 
	CONSTRAINT new_wallet_872884848
		 DEFAULT  0
)
go



ALTER TABLE Users
	ADD CONSTRAINT XPKUsers PRIMARY KEY  CLUSTERED (user_id ASC)
go



CREATE TABLE Users_to_Albums
( 
	user_id              uniqueidentifier  NOT NULL ,
	album_id             uniqueidentifier  NOT NULL 
)
go



CREATE TABLE Users_to_Roles
( 
	user_id              uniqueidentifier  NOT NULL ,
	role_id              integer  NOT NULL 
)
go




ALTER TABLE Albums
	ADD CONSTRAINT R_25 FOREIGN KEY (author_id) REFERENCES Authors(author_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Albums_to_Genres
	ADD CONSTRAINT R_19 FOREIGN KEY (genre_id) REFERENCES Genres(genre_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Albums_to_Genres
	ADD CONSTRAINT R_20 FOREIGN KEY (album_id) REFERENCES Albums(album_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Authors
	ADD CONSTRAINT R_11 FOREIGN KEY (user_id) REFERENCES Users(user_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Tracks
	ADD CONSTRAINT R_24 FOREIGN KEY (album_id) REFERENCES Albums(album_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Trade_History
	ADD CONSTRAINT R_12 FOREIGN KEY (action_id) REFERENCES Actions(action_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Trade_History
	ADD CONSTRAINT R_13 FOREIGN KEY (user_id) REFERENCES Users(user_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Trade_History
	ADD CONSTRAINT R_18 FOREIGN KEY (album_id) REFERENCES Albums(album_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Users_to_Albums
	ADD CONSTRAINT R_14 FOREIGN KEY (user_id) REFERENCES Users(user_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Users_to_Albums
	ADD CONSTRAINT R_15 FOREIGN KEY (album_id) REFERENCES Albums(album_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Users_to_Roles
	ADD CONSTRAINT R_16 FOREIGN KEY (user_id) REFERENCES Users(user_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




ALTER TABLE Users_to_Roles
	ADD CONSTRAINT R_17 FOREIGN KEY (role_id) REFERENCES Roles(role_id)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




CREATE TRIGGER tD_Actions ON Actions FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Actions */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Actions  Trade_History on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="0000f7c3", PARENT_OWNER="", PARENT_TABLE="Actions"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_12", FK_COLUMNS="action_id" */
    IF EXISTS (
      SELECT * FROM deleted,Trade_History
      WHERE
        /*  %JoinFKPK(Trade_History,deleted," = "," AND") */
        Trade_History.action_id = deleted.action_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Actions because Trade_History exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Actions ON Actions FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Actions */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           @insaction_id integer,
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Actions  Trade_History on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00011956", PARENT_OWNER="", PARENT_TABLE="Actions"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_12", FK_COLUMNS="action_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(action_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Trade_History
      WHERE
        /*  %JoinFKPK(Trade_History,deleted," = "," AND") */
        Trade_History.action_id = deleted.action_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Actions because Trade_History exists.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Albums ON Albums FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Albums */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Albums  Users_to_Albums on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="0004d878", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_15", FK_COLUMNS="album_id" */
    IF EXISTS (
      SELECT * FROM deleted,Users_to_Albums
      WHERE
        /*  %JoinFKPK(Users_to_Albums,deleted," = "," AND") */
        Users_to_Albums.album_id = deleted.album_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Albums because Users_to_Albums exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Albums  Trade_History on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_18", FK_COLUMNS="album_id" */
    IF EXISTS (
      SELECT * FROM deleted,Trade_History
      WHERE
        /*  %JoinFKPK(Trade_History,deleted," = "," AND") */
        Trade_History.album_id = deleted.album_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Albums because Trade_History exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Albums  Albums_to_Genres on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Albums_to_Genres"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_20", FK_COLUMNS="album_id" */
    IF EXISTS (
      SELECT * FROM deleted,Albums_to_Genres
      WHERE
        /*  %JoinFKPK(Albums_to_Genres,deleted," = "," AND") */
        Albums_to_Genres.album_id = deleted.album_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Albums because Albums_to_Genres exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Albums  Tracks on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Tracks"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_24", FK_COLUMNS="album_id" */
    IF EXISTS (
      SELECT * FROM deleted,Tracks
      WHERE
        /*  %JoinFKPK(Tracks,deleted," = "," AND") */
        Tracks.album_id = deleted.album_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Albums because Tracks exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Authors  Albums on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Authors"
    CHILD_OWNER="", CHILD_TABLE="Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_25", FK_COLUMNS="author_id" */
    IF EXISTS (SELECT * FROM deleted,Authors
      WHERE
        /* %JoinFKPK(deleted,Authors," = "," AND") */
        deleted.author_id = Authors.author_id AND
        NOT EXISTS (
          SELECT * FROM Albums
          WHERE
            /* %JoinFKPK(Albums,Authors," = "," AND") */
            Albums.author_id = Authors.author_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Albums because Authors exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Albums ON Albums FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Albums */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           @insalbum_id uniqueidentifier,
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Albums  Users_to_Albums on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="0005af79", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_15", FK_COLUMNS="album_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(album_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Users_to_Albums
      WHERE
        /*  %JoinFKPK(Users_to_Albums,deleted," = "," AND") */
        Users_to_Albums.album_id = deleted.album_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Albums because Users_to_Albums exists.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Albums  Trade_History on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_18", FK_COLUMNS="album_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(album_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Trade_History
      WHERE
        /*  %JoinFKPK(Trade_History,deleted," = "," AND") */
        Trade_History.album_id = deleted.album_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Albums because Trade_History exists.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Albums  Albums_to_Genres on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Albums_to_Genres"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_20", FK_COLUMNS="album_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(album_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Albums_to_Genres
      WHERE
        /*  %JoinFKPK(Albums_to_Genres,deleted," = "," AND") */
        Albums_to_Genres.album_id = deleted.album_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Albums because Albums_to_Genres exists.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Albums  Tracks on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Tracks"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_24", FK_COLUMNS="album_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(album_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Tracks
      WHERE
        /*  %JoinFKPK(Tracks,deleted," = "," AND") */
        Tracks.album_id = deleted.album_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Albums because Tracks exists.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Authors  Albums on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Authors"
    CHILD_OWNER="", CHILD_TABLE="Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_25", FK_COLUMNS="author_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(author_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Authors
        WHERE
          /* %JoinFKPK(inserted,Authors) */
          inserted.author_id = Authors.author_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.author_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Albums because Authors does not exist.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Albums_to_Genres ON Albums_to_Genres FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Albums_to_Genres */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Genres  Albums_to_Genres on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00024e34", PARENT_OWNER="", PARENT_TABLE="Genres"
    CHILD_OWNER="", CHILD_TABLE="Albums_to_Genres"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_19", FK_COLUMNS="genre_id" */
    IF EXISTS (SELECT * FROM deleted,Genres
      WHERE
        /* %JoinFKPK(deleted,Genres," = "," AND") */
        deleted.genre_id = Genres.genre_id AND
        NOT EXISTS (
          SELECT * FROM Albums_to_Genres
          WHERE
            /* %JoinFKPK(Albums_to_Genres,Genres," = "," AND") */
            Albums_to_Genres.genre_id = Genres.genre_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Albums_to_Genres because Genres exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Albums  Albums_to_Genres on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Albums_to_Genres"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_20", FK_COLUMNS="album_id" */
    IF EXISTS (SELECT * FROM deleted,Albums
      WHERE
        /* %JoinFKPK(deleted,Albums," = "," AND") */
        deleted.album_id = Albums.album_id AND
        NOT EXISTS (
          SELECT * FROM Albums_to_Genres
          WHERE
            /* %JoinFKPK(Albums_to_Genres,Albums," = "," AND") */
            Albums_to_Genres.album_id = Albums.album_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Albums_to_Genres because Albums exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Albums_to_Genres ON Albums_to_Genres FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Albums_to_Genres */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Genres  Albums_to_Genres on child update no action */
  /* ERWIN_RELATION:CHECKSUM="0002d72a", PARENT_OWNER="", PARENT_TABLE="Genres"
    CHILD_OWNER="", CHILD_TABLE="Albums_to_Genres"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_19", FK_COLUMNS="genre_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(genre_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Genres
        WHERE
          /* %JoinFKPK(inserted,Genres) */
          inserted.genre_id = Genres.genre_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.genre_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Albums_to_Genres because Genres does not exist.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Albums  Albums_to_Genres on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Albums_to_Genres"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_20", FK_COLUMNS="album_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(album_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Albums
        WHERE
          /* %JoinFKPK(inserted,Albums) */
          inserted.album_id = Albums.album_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.album_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Albums_to_Genres because Albums does not exist.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Authors ON Authors FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Authors */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Authors  Albums on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="000201db", PARENT_OWNER="", PARENT_TABLE="Authors"
    CHILD_OWNER="", CHILD_TABLE="Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_25", FK_COLUMNS="author_id" */
    IF EXISTS (
      SELECT * FROM deleted,Albums
      WHERE
        /*  %JoinFKPK(Albums,deleted," = "," AND") */
        Albums.author_id = deleted.author_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Authors because Albums exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Users  Authors on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Authors"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_11", FK_COLUMNS="user_id" */
    IF EXISTS (SELECT * FROM deleted,Users
      WHERE
        /* %JoinFKPK(deleted,Users," = "," AND") */
        deleted.user_id = Users.user_id AND
        NOT EXISTS (
          SELECT * FROM Authors
          WHERE
            /* %JoinFKPK(Authors,Users," = "," AND") */
            Authors.user_id = Users.user_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Authors because Users exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Authors ON Authors FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Authors */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           @insauthor_id uniqueidentifier,
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Authors  Albums on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="0002515f", PARENT_OWNER="", PARENT_TABLE="Authors"
    CHILD_OWNER="", CHILD_TABLE="Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_25", FK_COLUMNS="author_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(author_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Albums
      WHERE
        /*  %JoinFKPK(Albums,deleted," = "," AND") */
        Albums.author_id = deleted.author_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Authors because Albums exists.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Users  Authors on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Authors"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_11", FK_COLUMNS="user_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(user_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Users
        WHERE
          /* %JoinFKPK(inserted,Users) */
          inserted.user_id = Users.user_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.user_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Authors because Users does not exist.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Genres ON Genres FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Genres */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Genres  Albums_to_Genres on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="0000fd92", PARENT_OWNER="", PARENT_TABLE="Genres"
    CHILD_OWNER="", CHILD_TABLE="Albums_to_Genres"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_19", FK_COLUMNS="genre_id" */
    IF EXISTS (
      SELECT * FROM deleted,Albums_to_Genres
      WHERE
        /*  %JoinFKPK(Albums_to_Genres,deleted," = "," AND") */
        Albums_to_Genres.genre_id = deleted.genre_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Genres because Albums_to_Genres exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Genres ON Genres FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Genres */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           @insgenre_id integer,
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Genres  Albums_to_Genres on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00012517", PARENT_OWNER="", PARENT_TABLE="Genres"
    CHILD_OWNER="", CHILD_TABLE="Albums_to_Genres"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_19", FK_COLUMNS="genre_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(genre_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Albums_to_Genres
      WHERE
        /*  %JoinFKPK(Albums_to_Genres,deleted," = "," AND") */
        Albums_to_Genres.genre_id = deleted.genre_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Genres because Albums_to_Genres exists.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Roles ON Roles FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Roles */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Roles  Users_to_Roles on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="0000f485", PARENT_OWNER="", PARENT_TABLE="Roles"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Roles"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_17", FK_COLUMNS="role_id" */
    IF EXISTS (
      SELECT * FROM deleted,Users_to_Roles
      WHERE
        /*  %JoinFKPK(Users_to_Roles,deleted," = "," AND") */
        Users_to_Roles.role_id = deleted.role_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Roles because Users_to_Roles exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Roles ON Roles FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Roles */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           @insrole_id integer,
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Roles  Users_to_Roles on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00010fad", PARENT_OWNER="", PARENT_TABLE="Roles"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Roles"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_17", FK_COLUMNS="role_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(role_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Users_to_Roles
      WHERE
        /*  %JoinFKPK(Users_to_Roles,deleted," = "," AND") */
        Users_to_Roles.role_id = deleted.role_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Roles because Users_to_Roles exists.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Tracks ON Tracks FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Tracks */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Albums  Tracks on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="000124cb", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Tracks"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_24", FK_COLUMNS="album_id" */
    IF EXISTS (SELECT * FROM deleted,Albums
      WHERE
        /* %JoinFKPK(deleted,Albums," = "," AND") */
        deleted.album_id = Albums.album_id AND
        NOT EXISTS (
          SELECT * FROM Tracks
          WHERE
            /* %JoinFKPK(Tracks,Albums," = "," AND") */
            Tracks.album_id = Albums.album_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Tracks because Albums exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Tracks ON Tracks FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Tracks */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           @instrack_id uniqueidentifier,
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Albums  Tracks on child update no action */
  /* ERWIN_RELATION:CHECKSUM="000163e2", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Tracks"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_24", FK_COLUMNS="album_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(album_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Albums
        WHERE
          /* %JoinFKPK(inserted,Albums) */
          inserted.album_id = Albums.album_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.album_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Tracks because Albums does not exist.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Trade_History ON Trade_History FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Trade_History */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Actions  Trade_History on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="000373dc", PARENT_OWNER="", PARENT_TABLE="Actions"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_12", FK_COLUMNS="action_id" */
    IF EXISTS (SELECT * FROM deleted,Actions
      WHERE
        /* %JoinFKPK(deleted,Actions," = "," AND") */
        deleted.action_id = Actions.action_id AND
        NOT EXISTS (
          SELECT * FROM Trade_History
          WHERE
            /* %JoinFKPK(Trade_History,Actions," = "," AND") */
            Trade_History.action_id = Actions.action_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Trade_History because Actions exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Users  Trade_History on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_13", FK_COLUMNS="user_id" */
    IF EXISTS (SELECT * FROM deleted,Users
      WHERE
        /* %JoinFKPK(deleted,Users," = "," AND") */
        deleted.user_id = Users.user_id AND
        NOT EXISTS (
          SELECT * FROM Trade_History
          WHERE
            /* %JoinFKPK(Trade_History,Users," = "," AND") */
            Trade_History.user_id = Users.user_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Trade_History because Users exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Albums  Trade_History on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_18", FK_COLUMNS="album_id" */
    IF EXISTS (SELECT * FROM deleted,Albums
      WHERE
        /* %JoinFKPK(deleted,Albums," = "," AND") */
        deleted.album_id = Albums.album_id AND
        NOT EXISTS (
          SELECT * FROM Trade_History
          WHERE
            /* %JoinFKPK(Trade_History,Albums," = "," AND") */
            Trade_History.album_id = Albums.album_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Trade_History because Albums exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Trade_History ON Trade_History FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Trade_History */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           @inshistory_id uniqueidentifier,
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Actions  Trade_History on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00044206", PARENT_OWNER="", PARENT_TABLE="Actions"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_12", FK_COLUMNS="action_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(action_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Actions
        WHERE
          /* %JoinFKPK(inserted,Actions) */
          inserted.action_id = Actions.action_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.action_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Trade_History because Actions does not exist.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Users  Trade_History on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_13", FK_COLUMNS="user_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(user_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Users
        WHERE
          /* %JoinFKPK(inserted,Users) */
          inserted.user_id = Users.user_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.user_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Trade_History because Users does not exist.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Albums  Trade_History on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_18", FK_COLUMNS="album_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(album_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Albums
        WHERE
          /* %JoinFKPK(inserted,Albums) */
          inserted.album_id = Albums.album_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.album_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Trade_History because Albums does not exist.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Users ON Users FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Users */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Users  Authors on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="0003cbfa", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Authors"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_11", FK_COLUMNS="user_id" */
    IF EXISTS (
      SELECT * FROM deleted,Authors
      WHERE
        /*  %JoinFKPK(Authors,deleted," = "," AND") */
        Authors.user_id = deleted.user_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Users because Authors exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Users  Trade_History on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_13", FK_COLUMNS="user_id" */
    IF EXISTS (
      SELECT * FROM deleted,Trade_History
      WHERE
        /*  %JoinFKPK(Trade_History,deleted," = "," AND") */
        Trade_History.user_id = deleted.user_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Users because Trade_History exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Users  Users_to_Albums on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_14", FK_COLUMNS="user_id" */
    IF EXISTS (
      SELECT * FROM deleted,Users_to_Albums
      WHERE
        /*  %JoinFKPK(Users_to_Albums,deleted," = "," AND") */
        Users_to_Albums.user_id = deleted.user_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Users because Users_to_Albums exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Users  Users_to_Roles on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Roles"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_16", FK_COLUMNS="user_id" */
    IF EXISTS (
      SELECT * FROM deleted,Users_to_Roles
      WHERE
        /*  %JoinFKPK(Users_to_Roles,deleted," = "," AND") */
        Users_to_Roles.user_id = deleted.user_id
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Users because Users_to_Roles exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Users ON Users FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Users */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           @insuser_id uniqueidentifier,
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Users  Authors on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00042cb9", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Authors"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_11", FK_COLUMNS="user_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(user_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Authors
      WHERE
        /*  %JoinFKPK(Authors,deleted," = "," AND") */
        Authors.user_id = deleted.user_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Users because Authors exists.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Users  Trade_History on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Trade_History"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_13", FK_COLUMNS="user_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(user_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Trade_History
      WHERE
        /*  %JoinFKPK(Trade_History,deleted," = "," AND") */
        Trade_History.user_id = deleted.user_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Users because Trade_History exists.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Users  Users_to_Albums on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_14", FK_COLUMNS="user_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(user_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Users_to_Albums
      WHERE
        /*  %JoinFKPK(Users_to_Albums,deleted," = "," AND") */
        Users_to_Albums.user_id = deleted.user_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Users because Users_to_Albums exists.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Users  Users_to_Roles on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Roles"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_16", FK_COLUMNS="user_id" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(user_id)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Users_to_Roles
      WHERE
        /*  %JoinFKPK(Users_to_Roles,deleted," = "," AND") */
        Users_to_Roles.user_id = deleted.user_id
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Users because Users_to_Roles exists.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Users_to_Albums ON Users_to_Albums FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Users_to_Albums */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Users  Users_to_Albums on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00025e7b", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_14", FK_COLUMNS="user_id" */
    IF EXISTS (SELECT * FROM deleted,Users
      WHERE
        /* %JoinFKPK(deleted,Users," = "," AND") */
        deleted.user_id = Users.user_id AND
        NOT EXISTS (
          SELECT * FROM Users_to_Albums
          WHERE
            /* %JoinFKPK(Users_to_Albums,Users," = "," AND") */
            Users_to_Albums.user_id = Users.user_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Users_to_Albums because Users exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Albums  Users_to_Albums on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_15", FK_COLUMNS="album_id" */
    IF EXISTS (SELECT * FROM deleted,Albums
      WHERE
        /* %JoinFKPK(deleted,Albums," = "," AND") */
        deleted.album_id = Albums.album_id AND
        NOT EXISTS (
          SELECT * FROM Users_to_Albums
          WHERE
            /* %JoinFKPK(Users_to_Albums,Albums," = "," AND") */
            Users_to_Albums.album_id = Albums.album_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Users_to_Albums because Albums exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Users_to_Albums ON Users_to_Albums FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Users_to_Albums */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Users  Users_to_Albums on child update no action */
  /* ERWIN_RELATION:CHECKSUM="0002cb93", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_14", FK_COLUMNS="user_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(user_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Users
        WHERE
          /* %JoinFKPK(inserted,Users) */
          inserted.user_id = Users.user_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.user_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Users_to_Albums because Users does not exist.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Albums  Users_to_Albums on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Albums"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Albums"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_15", FK_COLUMNS="album_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(album_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Albums
        WHERE
          /* %JoinFKPK(inserted,Albums) */
          inserted.album_id = Albums.album_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.album_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Users_to_Albums because Albums does not exist.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go




CREATE TRIGGER tD_Users_to_Roles ON Users_to_Roles FOR DELETE AS
/* ERwin Builtin Trigger */
/* DELETE trigger on Users_to_Roles */
BEGIN
  DECLARE  @errno   int,
           @errmsg  varchar(255)
    /* ERwin Builtin Trigger */
    /* Users  Users_to_Roles on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00024e8a", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Roles"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_16", FK_COLUMNS="user_id" */
    IF EXISTS (SELECT * FROM deleted,Users
      WHERE
        /* %JoinFKPK(deleted,Users," = "," AND") */
        deleted.user_id = Users.user_id AND
        NOT EXISTS (
          SELECT * FROM Users_to_Roles
          WHERE
            /* %JoinFKPK(Users_to_Roles,Users," = "," AND") */
            Users_to_Roles.user_id = Users.user_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Users_to_Roles because Users exists.'
      GOTO ERROR
    END

    /* ERwin Builtin Trigger */
    /* Roles  Users_to_Roles on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Roles"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Roles"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_17", FK_COLUMNS="role_id" */
    IF EXISTS (SELECT * FROM deleted,Roles
      WHERE
        /* %JoinFKPK(deleted,Roles," = "," AND") */
        deleted.role_id = Roles.role_id AND
        NOT EXISTS (
          SELECT * FROM Users_to_Roles
          WHERE
            /* %JoinFKPK(Users_to_Roles,Roles," = "," AND") */
            Users_to_Roles.role_id = Roles.role_id
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Users_to_Roles because Roles exists.'
      GOTO ERROR
    END


    /* ERwin Builtin Trigger */
    RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


CREATE TRIGGER tU_Users_to_Roles ON Users_to_Roles FOR UPDATE AS
/* ERwin Builtin Trigger */
/* UPDATE trigger on Users_to_Roles */
BEGIN
  DECLARE  @NUMROWS int,
           @nullcnt int,
           @validcnt int,
           
           @errno   int,
           @errmsg  varchar(255)

  SELECT @NUMROWS = @@rowcount
  /* ERwin Builtin Trigger */
  /* Users  Users_to_Roles on child update no action */
  /* ERWIN_RELATION:CHECKSUM="0002c5ec", PARENT_OWNER="", PARENT_TABLE="Users"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Roles"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_16", FK_COLUMNS="user_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(user_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Users
        WHERE
          /* %JoinFKPK(inserted,Users) */
          inserted.user_id = Users.user_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.user_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Users_to_Roles because Users does not exist.'
      GOTO ERROR
    END
  END

  /* ERwin Builtin Trigger */
  /* Roles  Users_to_Roles on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Roles"
    CHILD_OWNER="", CHILD_TABLE="Users_to_Roles"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_17", FK_COLUMNS="role_id" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(role_id)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Roles
        WHERE
          /* %JoinFKPK(inserted,Roles) */
          inserted.role_id = Roles.role_id
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.role_id IS NULL
    IF @validcnt + @nullcnt != @NUMROWS
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Users_to_Roles because Roles does not exist.'
      GOTO ERROR
    END
  END


  /* ERwin Builtin Trigger */
  RETURN
ERROR:
    raiserror (@errmsg,16,1)
    rollback transaction
END

go


