package com.github.alwaysdarkk.enderchest.repository.codec;

import com.github.alwaysdarkk.enderchest.data.EnderChest;
import com.github.alwaysdarkk.enderchest.util.serializer.InventorySerializer;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.inventory.Inventory;

public class EnderChestCodec implements Codec<EnderChest> {

    @Override
    public EnderChest decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readObjectId("_id");

        final String owner = reader.readString("owner");
        final Inventory inventory = InventorySerializer.deserialize(reader.readString("inventory"));

        reader.readEndDocument();

        return EnderChest.builder()
                .owner(owner)
                .inventory(inventory)
                .build();
    }

    @Override
    public void encode(BsonWriter writer, EnderChest value, EncoderContext encoderContext) {
        if (value == null) return;

        writer.writeStartDocument();

        writer.writeString("owner", value.getOwner());
        writer.writeString("inventory", InventorySerializer.serialize(value.getInventory()).toString());

        writer.writeEndDocument();
    }

    @Override
    public Class<EnderChest> getEncoderClass() {
        return EnderChest.class;
    }
}