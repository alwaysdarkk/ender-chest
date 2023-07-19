package com.github.alwaysdarkk.enderchest;

import com.github.alwaysdarkk.enderchest.cache.EnderChestCache;
import com.github.alwaysdarkk.enderchest.command.CustomCommand;
import com.github.alwaysdarkk.enderchest.command.impl.EnderChestCommand;
import com.github.alwaysdarkk.enderchest.connection.impl.MongoConnectionProvider;
import com.github.alwaysdarkk.enderchest.factory.EnderChestFactory;
import com.github.alwaysdarkk.enderchest.listener.InventoryCloseListener;
import com.github.alwaysdarkk.enderchest.listener.PlayerConnectionListener;
import com.github.alwaysdarkk.enderchest.listener.PlayerInteractListener;
import com.github.alwaysdarkk.enderchest.repository.EnderChestRepository;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class EnderChestPlugin extends JavaPlugin {

    private MongoConnectionProvider mongoConnectionProvider;

    @Override
    public void onEnable() {
        prepareDatastore();

        final EnderChestRepository enderChestRepository = new EnderChestRepository(mongoConnectionProvider.getDatabase("global"));
        final EnderChestCache enderChestCache = new EnderChestCache();
        final EnderChestFactory enderChestFactory = new EnderChestFactory(enderChestRepository, enderChestCache);

        Bukkit.getPluginManager()
                .registerEvents(new PlayerConnectionListener(enderChestFactory, enderChestCache), this);

        Bukkit.getPluginManager()
                .registerEvents(new InventoryCloseListener(enderChestCache, enderChestFactory), this);

        Bukkit.getPluginManager()
                .registerEvents(new PlayerInteractListener(enderChestCache, enderChestFactory), this);

        registerCommand(new EnderChestCommand(enderChestCache, enderChestFactory, enderChestRepository));
    }

    @Override
    public void onDisable() {
        if (mongoConnectionProvider != null && !mongoConnectionProvider.isClosed()) {
            mongoConnectionProvider.shutdown();
        }
    }

    private void prepareDatastore() {
        if (mongoConnectionProvider != null) return;

        mongoConnectionProvider = new MongoConnectionProvider();
        mongoConnectionProvider.prepare();
    }

    @SneakyThrows
    private void registerCommand(CustomCommand... customCommands) {
        final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);

        final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

        for (CustomCommand customCommand : customCommands)
            commandMap.register(customCommand.getName(), customCommand);

    }
}