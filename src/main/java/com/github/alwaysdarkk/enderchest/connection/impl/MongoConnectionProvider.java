package com.github.alwaysdarkk.enderchest.connection.impl;

import com.github.alwaysdarkk.enderchest.connection.ConnectionProvider;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.experimental.Delegate;
import org.bson.UuidRepresentation;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MongoConnectionProvider implements ConnectionProvider<MongoClient> {

    private static final String CONNECTION_URL = "mongodb://localhost:27017";

    @Delegate
    private MongoClient mongoClient;

    @Override
    public void prepare() {
        final ConnectionString connectionString = new ConnectionString(CONNECTION_URL);
        final MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .applyToConnectionPoolSettings(builder ->
                        builder.maxSize(20).minSize(1).maxConnecting(3).maxConnectionIdleTime(0, MILLISECONDS))
                .retryWrites(true)
                .build();

        this.mongoClient = MongoClients.create(clientSettings);
    }

    @Override
    public void shutdown() {
        if (mongoClient != null) {
            this.mongoClient.close();
            this.mongoClient = null;
        }
    }

    @Override
    public boolean isClosed() {
        return this.mongoClient == null;
    }
}