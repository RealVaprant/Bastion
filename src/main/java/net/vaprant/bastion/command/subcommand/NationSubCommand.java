package net.vaprant.bastion.command.subcommand;

import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface NationSubCommand {
    void execute(CommandSender sender, String[] args);

    default List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    default boolean isAccessible(CommandSender sender) {
        return true;
    }
}