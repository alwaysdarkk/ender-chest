package com.github.alwaysdarkk.enderchest.command.impl.subcommand;

import com.github.alwaysdarkk.enderchest.EnderChestConstants;
import com.github.alwaysdarkk.enderchest.command.CustomCommand;
import org.bukkit.command.CommandSender;

public class ToggleSubCommand extends CustomCommand {

    public ToggleSubCommand() {
        super("toggle", "enderchest.admin", false);
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        EnderChestConstants.setEnabled(!EnderChestConstants.isEnabled());
        commandSender.sendMessage(String.format("§aO Baú do Fim foi §f%s §acom sucesso.", (EnderChestConstants.isEnabled() ? "ativado" : "desativado")));
    }
}