package com.github.alwaysdarkk.enderchest.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;

public abstract class CustomCommand extends Command {

    @Getter
    private final Set<CustomCommand> subCommands = Sets.newHashSet();
    private final String permission;
    private final boolean playerOnly;

    public CustomCommand(String name, String permission, Boolean playerOnly, String... aliases) {
        super(name);

        this.permission = permission;
        this.playerOnly = playerOnly;

        if (aliases.length > 0)
            setAliases(Lists.newArrayList(aliases));

        if (permission != null) {
            setPermission(permission);
            setPermissionMessage("§cVocê não tem permissão para usar este comando.");
        }

        setUsage("§cUtilize /" + name);
    }

    protected abstract void onCommand(CommandSender commandSender, String[] arguments);

    public void executeRaw(CommandSender commandSender, String s, String[] strings) {

        if (!(commandSender instanceof Player) && playerOnly) {
            commandSender.sendMessage("§cApenas jogadores podem usar este comando.");
            return;
        }

        if (permission != null && !testPermissionSilent(commandSender)) {
            commandSender.sendMessage("§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (strings.length > 0) {

            CustomCommand subCommandFound = null;

            for (CustomCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(strings[0]) || subCommand.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                    subCommandFound = subCommand;
                    break;
                }
            }

            if (subCommandFound != null) {
                subCommandFound.executeRaw(commandSender, s, Arrays.copyOfRange(strings, 1, strings.length));
                return;
            }
        }

        this.onCommand(commandSender, strings);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        executeRaw(commandSender, s, strings);
        return true;
    }

    public void registerSubCommands(CustomCommand... customCommands) {
        subCommands.addAll(Arrays.asList(customCommands));
    }
}