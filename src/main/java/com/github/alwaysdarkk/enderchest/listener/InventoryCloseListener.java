package com.github.alwaysdarkk.enderchest.listener;

import com.github.alwaysdarkk.enderchest.cache.EnderChestCache;
import com.github.alwaysdarkk.enderchest.data.EnderChest;
import com.github.alwaysdarkk.enderchest.factory.EnderChestFactory;
import com.github.alwaysdarkk.enderchest.util.CooldownUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class InventoryCloseListener implements Listener {

    private final EnderChestCache enderChestCache;
    private final EnderChestFactory enderChestFactory;

    @EventHandler
    private void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        final Inventory inventory = event.getInventory();

        if (!inventory.getName().startsWith("Baú do Fim de ")) return;

        final String enderChestOwner = inventory.getName().split("de ")[1];
        final EnderChest enderChest = enderChestCache.get(enderChestOwner);

        if (enderChest == null) return;

        enderChest.setInventory(inventory);

        enderChestFactory.save(enderChest);

        CooldownUtil.create(
                player.getName(),
                "ender-chest",
                5L,
                TimeUnit.SECONDS
        );
    }
}