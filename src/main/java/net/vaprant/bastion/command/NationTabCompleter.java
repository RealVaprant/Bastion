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

            Map<String, NationSubCommand> availableSubCommands = new HashMap<>();
            for (Map.Entry<String, NationSubCommand> entry : subCommands.entrySet()) {
                if (entry.getValue().isAccessible(commandSender)) {
                    availableSubCommands.put(entry.getKey(), entry.getValue());
                }
            }

            return filterCompletions(
                    new ArrayList<>(availableSubCommands.keySet())
                    , args[0]
            );
        }

        NationSubCommand subCommand = subCommands.get(args[0]);
        if (subCommand != null && subCommand.isAccessible(commandSender)){
            return subCommand.onTabComplete(commandSender, args);
        }


        return List.of();
    }

    public static List<String> filterCompletions(List<String> completions, String filter) {
        List<String> matches = new ArrayList<>();
        for (String entry : completions) {
            if (entry.toLowerCase().startsWith(filter.toLowerCase())){
                matches.add(entry);
            }
        }
        return matches;
    }
}
