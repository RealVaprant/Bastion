package net.vaprant.bastion;

import net.vaprant.bastion.command.CommandInitializer;
import net.vaprant.bastion.event.EventInitializer;
import net.vaprant.bastion.player.BastionProfileInitializer;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bastion extends JavaPlugin {

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        new CommandInitializer(this);
        new EventInitializer(this);
        new BastionProfileInitializer();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getInstance(){
        return plugin;
    }
}
