package net.vaprant.bastion.command;

import net.kyori.adventure.text.Component;
import net.vaprant.bastion.command.subcommand.CreateNation;
import net.vaprant.bastion.command.subcommand.NationSubCommand;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class NationCommand implements CommandExecutor {

    private final HashMap<String, NationSubCommand> subCommands;

    public NationCommand(HashMap<String, NationSubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        Map<String, NationSubCommand> availableSubCommands = new HashMap<>();
        for (Map.Entry<String, NationSubCommand> entry : subCommands.entrySet()) {
            if (entry.getValue().isAccessible(commandSender)) {
                availableSubCommands.put(entry.getKey(), entry.getValue());
            }
        }
        String availableArguements =  "( " + String.join(" | ", availableSubCommands.keySet()) + " )";

        if (args.length == 0) {
            BastionNotification.error(commandSender, "/" + command.getName() + " " + availableArguements);
            return true;
        }

        NationSubCommand nationSubCommand = subCommands.get(args[0]);

        if (nationSubCommand == null) {
            BastionNotification.error(commandSender, "/" + command.getName() + " " + availableArguements);
            return true;
        }

        nationSubCommand.execute(commandSender, args);
        return true;
    }
}
