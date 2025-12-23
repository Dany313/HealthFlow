package com.diaia.notification_service.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.bson.UuidRepresentation;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "notification_db";
    }

    @Override
    public MongoClientSettings mongoClientSettings() {
        return MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD) // Forza la rappresentazione standard degli UUID
                .applyConnectionString(new ConnectionString("mongodb://admin:password@localhost:27017/notification_db?authSource=admin"))
                .build();
    }
}