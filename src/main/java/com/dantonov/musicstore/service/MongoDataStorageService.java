package com.dantonov.musicstore.service;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.InputStream;

/**
 * @author denis.antonov
 * @since 25.03.17.
 */
public interface MongoDataStorageService {

    GridFSDBFile findById(String id);

    GridFSInputFile save(InputStream is, String fileName, String mimeType, DBObject metaData);

    void delete(String id);

}
