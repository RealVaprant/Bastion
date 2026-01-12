package net.vaprant.bastion;

import net.kyori.adventure.text.Component;
import net.vaprant.bastion.command.CommandInitializer;
import net.vaprant.bastion.event.EventInitializer;
import net.vaprant.bastion.player.BastionPlayerRegistry;import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bastion extends JavaPlugin {




    @Override
    public void onEnable() {
        new CommandInitializer(this);
        new EventInitializer(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
