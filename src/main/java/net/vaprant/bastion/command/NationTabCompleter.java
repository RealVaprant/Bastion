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

public class NationTabCompleter implements TabCompleter {

    private final List<String> arguments;

    public NationTabCompleter(HashMap<String, NationSubCommand> subCommands) {
        this.arguments = new ArrayList<>(subCommands.keySet());
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return arguments;
        }
        return null;
    }
}
