package com.dantonov.musicstore.service;

import com.dantonov.musicstore.mongo.dao.MongoFileStorageDao;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author denis.antonov
 * @since 25.03.17.
 */
@Service
public class MongoDataStorageServiceImpl implements MongoDataStorageService {

    private MongoFileStorageDao fileStorageDao;


    @Autowired
    public void setFileStorageDao(final MongoFileStorageDao fileStorageDao) {
        this.fileStorageDao = fileStorageDao;
    }


    @Override
    public GridFSDBFile findById(final String id) {
        return fileStorageDao.findById(id);
    }

    @Override
    public GridFSInputFile save(final InputStream is, final String fileName, final String mimeType, final DBObject metaData) {
        return fileStorageDao.save(is, fileName, mimeType, metaData);
    }

    @Override
    public void delete(final String id) {
        fileStorageDao.delete(id);
    }

}
