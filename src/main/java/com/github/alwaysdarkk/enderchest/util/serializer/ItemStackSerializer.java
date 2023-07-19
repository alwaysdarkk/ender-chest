package com.github.alwaysdarkk.enderchest.util.serializer;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@UtilityClass
public class ItemStackSerializer {

    @SneakyThrows
    public String serialize(ItemStack itemStack) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

        bukkitObjectOutputStream.writeObject(itemStack);
        return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
    }

    @SneakyThrows
    public ItemStack deserialize(String string) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(string));
        final BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);

        return (ItemStack) bukkitObjectInputStream.readObject();
    }
}