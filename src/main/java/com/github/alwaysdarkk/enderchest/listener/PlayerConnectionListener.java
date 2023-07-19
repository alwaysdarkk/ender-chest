package com.github.alwaysdarkk.enderchest.listener;

import com.github.alwaysdarkk.enderchest.cache.EnderChestCache;
import com.github.alwaysdarkk.enderchest.factory.EnderChestFactory;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final EnderChestFactory enderChestFactory;
    private final EnderChestCache enderChestCache;

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        enderChestFactory.load(player);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        enderChestCache.remove(player.getName());
    }
}