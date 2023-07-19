package com.github.alwaysdarkk.enderchest.listener;

import com.github.alwaysdarkk.enderchest.EnderChestConstants;
import com.github.alwaysdarkk.enderchest.cache.EnderChestCache;
import com.github.alwaysdarkk.enderchest.data.EnderChest;
import com.github.alwaysdarkk.enderchest.factory.EnderChestFactory;
import com.github.alwaysdarkk.enderchest.util.CooldownUtil;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

    private final EnderChestCache enderChestCache;
    private final EnderChestFactory enderChestFactory;

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final EntityPlayer handle = ((CraftPlayer) player).getHandle();

        if (!handle.activeContainer.equals(handle.defaultContainer)) return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Block block = event.getClickedBlock();

        if (block.getType() == Material.AIR || block.getType() != Material.ENDER_CHEST) return;

        event.setCancelled(true);

        if (!EnderChestConstants.isEnabled()) {
            player.sendMessage("§cEsta função está desabilitada por enquanto.");
            return;
        }

        final EnderChest enderChest = enderChestCache.get(player.getName());

        if (enderChest == null) return;

        if (!CooldownUtil.isFinished(player.getName(), "ender-chest")) {
            player.sendMessage("§cAguarde para abrir o Baú do Fim novamente.");
            return;
        }

        if (enderChest.getInventory().getSize() != enderChestFactory.getEnderChestRows(player)) {
            final Inventory oldInventory = enderChest.getInventory(),
                    newInventory = enderChestFactory.createInventory(player);

            Arrays.stream(oldInventory.getContents())
                    .filter(Objects::nonNull)
                    .forEach(itemStack -> {
                        for (ItemStack rest : newInventory.addItem(itemStack).values())
                            player.getWorld().dropItemNaturally(player.getLocation(), rest);
                    });

            enderChest.setInventory(newInventory);
        }

        player.openInventory(enderChest.getInventory());
    }
}