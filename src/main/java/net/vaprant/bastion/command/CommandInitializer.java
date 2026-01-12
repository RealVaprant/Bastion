package net.vaprant.bastion.command;


import net.vaprant.bastion.command.subcommand.CreateNation;
import net.vaprant.bastion.command.subcommand.NationSubCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public class CommandInitializer {

    public CommandInitializer(JavaPlugin plugin) {
        HashMap<String, NationSubCommand> subCommands = new HashMap<>();
        subCommands.put("create", new CreateNation());

        Objects.requireNonNull(plugin.getCommand("nation")).setExecutor(new NationCommand(subCommands));
        Objects.requireNonNull(plugin.getCommand("nation")).setTabCompleter(new NationTabCompleter(subCommands));

    }
}
