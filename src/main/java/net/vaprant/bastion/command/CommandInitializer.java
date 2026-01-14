package net.vaprant.bastion.command;


import net.vaprant.bastion.command.subcommand.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public class CommandInitializer {

    public CommandInitializer(JavaPlugin plugin) {
        HashMap<String, NationSubCommand> subCommands = new HashMap<>();
        subCommands.put("create", new CreateNation());
        subCommands.put("disband", new DisbandNation());
        subCommands.put("leave", new LeaveNation());
        subCommands.put("rename", new RenameNation());
        subCommands.put("invite", new NationInvite());
        subCommands.put("join", new JoinNation());

        Objects.requireNonNull(plugin.getCommand("nation")).setExecutor(new NationCommand(subCommands));
        Objects.requireNonNull(plugin.getCommand("nation")).setTabCompleter(new NationTabCompleter(subCommands));

    }
}
