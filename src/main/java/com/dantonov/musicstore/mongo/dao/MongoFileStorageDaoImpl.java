package com.dantonov.musicstore.mongo.dao;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

/**
 * @author denis.antonov
 * @since 25.03.17.
 */
@Repository
public class MongoFileStorageDaoImpl implements MongoFileStorageDao {


    private GridFsTemplate gridFsTemplate;


    @Autowired
    public void setGridFsTemplate(final GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }


    @Override
    public GridFSDBFile findById(final String id) {
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
    }

    @Override
    public GridFSInputFile save(final InputStream is, final String fileName, final String mimeType, final DBObject metaData) {
        return (GridFSInputFile) gridFsTemplate.store(is, fileName, mimeType, metaData);
    }

    @Override
    public void delete(final String id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
    }

}
