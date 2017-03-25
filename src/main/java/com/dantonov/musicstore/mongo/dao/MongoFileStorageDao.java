package com.dantonov.musicstore.mongo.dao;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.InputStream;

/**
 * @author denis.antonov
 * @since 25.03.17.
 */
public interface MongoFileStorageDao {

    GridFSDBFile findById(String id);

    GridFSInputFile save(InputStream is, String fileName, String mimeType, DBObject metaData);

    void delete(String id);

}
