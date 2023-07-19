package com.github.alwaysdarkk.enderchest.command.impl;

import com.github.alwaysdarkk.enderchest.EnderChestConstants;
import com.github.alwaysdarkk.enderchest.cache.EnderChestCache;
import com.github.alwaysdarkk.enderchest.command.CustomCommand;
import com.github.alwaysdarkk.enderchest.command.impl.subcommand.ToggleSubCommand;
import com.github.alwaysdarkk.enderchest.data.EnderChest;
import com.github.alwaysdarkk.enderchest.factory.EnderChestFactory;
import com.github.alwaysdarkk.enderchest.repository.EnderChestRepository;
import com.github.alwaysdarkk.enderchest.util.CooldownUtil;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class EnderChestCommand extends CustomCommand {

    private final EnderChestCache enderChestCache;
    private final EnderChestFactory enderChestFactory;
    private final EnderChestRepository enderChestRepository;

    public EnderChestCommand(EnderChestCache enderChestCache, EnderChestFactory enderChestFactory, EnderChestRepository enderChestRepository) {
        super("enderchest", null, false, "ec");

        registerSubCommands(new ToggleSubCommand());

        this.enderChestCache = enderChestCache;
        this.enderChestFactory = enderChestFactory;
        this.enderChestRepository = enderChestRepository;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        if (!(commandSender instanceof Player)) return;

        final Player player = (Player) commandSender;
        final EntityPlayer handle = ((CraftPlayer) player).getHandle();

        if (!handle.activeContainer.equals(handle.defaultContainer)) return;

        if (!EnderChestConstants.isEnabled()) {
            player.sendMessage("§cEsta função está desabilitada por enquanto.");
            return;
        }

        if (arguments.length == 0) {
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
            return;
        }

        if (arguments.length == 1) {
            if (!player.hasPermission("enderchest.admin")) {
                player.sendMessage("§cVocê não tem permissão para executar este comando.");
                return;
            }

            final EnderChest enderChest = enderChestRepository.findByOwner(arguments[0]);

            if (enderChest == null) {
                player.sendMessage("§cEste jogador não foi encontrado.");
                return;
            }

            player.openInventory(enderChest.getInventory());
        }
    }
}