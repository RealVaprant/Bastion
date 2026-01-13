package net.vaprant.bastion.event.player;

import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        BastionPlayer bastionPlayer = new BastionPlayer(event.getPlayer().getUniqueId());

        BastionPlayer.registry.addPlayer(bastionPlayer);
    }
}
