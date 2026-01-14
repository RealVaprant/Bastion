package net.vaprant.bastion.event.player;

import net.vaprant.bastion.player.BastionProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BastionProfile bastionPlayer = new BastionProfile(event.getPlayer().getUniqueId());
        BastionProfile.registry.addPlayer(bastionPlayer);
    }
}
