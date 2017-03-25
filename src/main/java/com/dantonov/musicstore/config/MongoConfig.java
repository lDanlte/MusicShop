package com.dantonov.musicstore.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.annotation.Resource;

/**
 * @author denis.antonov
 * @since 23.03.17.
 */
@Configuration
@PropertySource("classpath:new_props.properties")
@ComponentScan(basePackages = {"com.dantonov.musicstore.mongo.dao"})
public class MongoConfig extends AbstractMongoConfiguration {


    @Resource
    private Environment env;


    @Override
    protected String getDatabaseName() {
        return "music_store";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(new MongoClientURI(env.getRequiredProperty("mongo.url")));
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

}
