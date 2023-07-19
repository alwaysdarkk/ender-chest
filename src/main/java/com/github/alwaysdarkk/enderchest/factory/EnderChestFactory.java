package com.github.alwaysdarkk.enderchest.factory;

import com.github.alwaysdarkk.enderchest.cache.EnderChestCache;
import com.github.alwaysdarkk.enderchest.data.EnderChest;
import com.github.alwaysdarkk.enderchest.repository.EnderChestRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class EnderChestFactory {

    private final EnderChestRepository enderChestRepository;
    private final EnderChestCache enderChestCache;

    public void load(Player player) {
        EnderChest enderChest = enderChestRepository.findByOwner(player.getName());

        if (enderChest == null) {
            enderChest = createDefault(player);
            enderChestRepository.replaceOne(enderChest);
        }

        enderChestCache.put(player.getName(), enderChest);
    }

    public void save(EnderChest enderChest) {
        enderChestRepository.replaceOne(enderChest);
    }

    public int getEnderChestRows(Player player) {
        if (player.hasPermission("enderchest.6")) return 9 * 6;
        if (player.hasPermission("enderchest.5")) return 9 * 5;
        if (player.hasPermission("enderchest.4")) return 9 * 4;

        return 9 * 3;
    }

    public Inventory createInventory(Player player) {
        return Bukkit.createInventory(
                null,
                getEnderChestRows(player),
                "Baú do Fim de " + player.getName()
        );
    }

    private EnderChest createDefault(Player player) {
        return EnderChest.builder()
                .owner(player.getName())
                .inventory(createInventory(player))
                .build();
    }
}