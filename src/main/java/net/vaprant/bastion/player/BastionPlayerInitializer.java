package net.vaprant.bastion.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BastionPlayerInitializer {


    public BastionPlayerInitializer() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            BastionPlayer bastionPlayer = new BastionPlayer(player.getUniqueId());
            BastionPlayer.registry.addPlayer(bastionPlayer);
        }
    }
}
