package net.vaprant.bastion.event;

import net.vaprant.bastion.event.player.PlayerJoinListener;
import net.vaprant.bastion.event.player.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

public class EventInitializer {

    public EventInitializer(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
    }
}
