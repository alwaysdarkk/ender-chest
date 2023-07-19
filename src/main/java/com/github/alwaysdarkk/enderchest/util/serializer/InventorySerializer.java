package com.github.alwaysdarkk.enderchest.util.serializer;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@UtilityClass
public class InventorySerializer {

    public JSONObject serialize(Inventory inventory) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", inventory.getName());
        jsonObject.put("size", inventory.getSize());

        final JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < inventory.getSize(); i++) {
            final ItemStack itemStack = inventory.getItem(i);

            if (itemStack == null || itemStack.getType().equals(Material.AIR)) continue;

            final String serialized = ItemStackSerializer.serialize(itemStack);
            final JSONObject jsonObject1 = new JSONObject();

            jsonObject1.put(i, serialized);
            jsonArray.add(jsonObject1);
        }

        jsonObject.put("items", jsonArray);
        return jsonObject;
    }

    public Inventory deserialize(JSONObject jsonObject) {
        final String name = (String) jsonObject.get("name");
        final int size = ((Long) jsonObject.get("size")).intValue();
        final Inventory inventory = Bukkit.createInventory(null, size, name);
        final JSONArray jsonArray = (JSONArray) jsonObject.get("items");

        jsonArray.forEach(object -> {
            final JSONObject jsonObject1 = (JSONObject) object;
            jsonObject1.keySet().forEach(object1 -> {
                final String key = (String) object1;
                final int slot = Integer.parseInt(key);
                final String serializedItem = (String) jsonObject1.get(object1);
                final ItemStack itemStack = ItemStackSerializer.deserialize(serializedItem);

                inventory.setItem(slot, itemStack);
            });
        });

        return inventory;
    }

    public Inventory deserialize(String string) {
        final JSONObject jsonObject = (JSONObject) JSONValue.parse(string);
        return deserialize(jsonObject);
    }
}