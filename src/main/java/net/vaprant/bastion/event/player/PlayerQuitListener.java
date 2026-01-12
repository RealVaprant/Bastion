package net.vaprant.bastion.event.player;

import net.vaprant.bastion.player.BastionPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {


        BastionPlayer.registry.removePlayer(event.getPlayer().getUniqueId());

    }
}
