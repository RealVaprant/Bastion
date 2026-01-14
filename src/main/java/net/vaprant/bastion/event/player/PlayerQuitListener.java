package net.vaprant.bastion.event.player;

import net.vaprant.bastion.player.BastionProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {


        BastionProfile.registry.removePlayer(event.getPlayer().getUniqueId());

    }
}
