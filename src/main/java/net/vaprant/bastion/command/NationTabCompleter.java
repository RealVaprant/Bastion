package net.vaprant.bastion.command;

import net.vaprant.bastion.command.subcommand.NationSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NationTabCompleter implements TabCompleter {

    private final Map<String, NationSubCommand> subCommands;

    public NationTabCompleter(Map<String, NationSubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return new ArrayList<>(subCommands.keySet());
        }

        NationSubCommand subCommand = subCommands.get(args[0]);
        if (subCommand != null){
            return subCommand.onTabComplete(commandSender, args);
        }

        return List.of();
    }
}
