
CREATE TABLE Actions
( 
	action_id            tinyint IDENTITY ( 1,1 ) ,
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
	add_date             datetime  NOT NULL ,
	q_sold               bigint  NOT NULL 
)
go



ALTER TABLE Albums
	ADD CONSTRAINT XPKAlbums PRIMARY KEY  CLUSTERED (album_id ASC)
go



CREATE TABLE Albums_to_Genres
( 
	genre_id             tinyint  NOT NULL ,
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



ALTER TABLE Authors
	ADD CONSTRAINT XAK1Authors UNIQUE (name  ASC)
go



CREATE TABLE Genres
( 
	genre_id             tinyint IDENTITY ( 1,1 ) ,
	name                 varchar(32)  NOT NULL 
)
go



ALTER TABLE Genres
	ADD CONSTRAINT XPKGenres PRIMARY KEY  CLUSTERED (genre_id ASC)
go



ALTER TABLE Genres
	ADD CONSTRAINT XAK1Genres UNIQUE (name  ASC)
go



CREATE TABLE Roles
( 
	role_id              tinyint IDENTITY ( 1,1 ) ,
	role                 varchar(16)  NOT NULL 
)
go



ALTER TABLE Roles
	ADD CONSTRAINT XPKRoles PRIMARY KEY  CLUSTERED (role_id ASC)
go



ALTER TABLE Roles
	ADD CONSTRAINT XAK1Roles UNIQUE (role  ASC)
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
	action_id            tinyint  NOT NULL ,
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
		 DEFAULT  0,
	token                uniqueidentifier  NULL 
)
go



ALTER TABLE Users
	ADD CONSTRAINT XPKUsers PRIMARY KEY  CLUSTERED (user_id ASC)
go



ALTER TABLE Users
	ADD CONSTRAINT XAK1Users UNIQUE (email  ASC,login  ASC)
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
	role_id              tinyint  NOT NULL 
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


