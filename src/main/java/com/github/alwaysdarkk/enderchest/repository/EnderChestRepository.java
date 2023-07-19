package com.github.alwaysdarkk.enderchest.repository;

import com.github.alwaysdarkk.enderchest.data.EnderChest;
import com.github.alwaysdarkk.enderchest.repository.codec.EnderChestCodec;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.codecs.configuration.CodecRegistries;

public class EnderChestRepository {

    private final MongoCollection<EnderChest> mongoCollection;

    public EnderChestRepository(MongoDatabase mongoDatabase) {
        this.mongoCollection = mongoDatabase
                .getCollection("ender-chest", EnderChest.class)
                .withCodecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromCodecs(new EnderChestCodec()),
                        MongoClientSettings.getDefaultCodecRegistry()));
    }

    public void replaceOne(EnderChest enderChest) {
        this.mongoCollection.replaceOne(Filters.eq("owner", enderChest.getOwner()), enderChest, new ReplaceOptions().upsert(true));
    }

    public EnderChest findByOwner(String owner) {
        return this.mongoCollection.find(Filters.eq("owner", owner))
                .first();
    }
}