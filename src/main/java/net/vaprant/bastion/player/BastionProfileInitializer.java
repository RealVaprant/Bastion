package net.vaprant.bastion.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BastionProfileInitializer {


    public BastionProfileInitializer() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            BastionProfile bastionPlayer = new BastionProfile(player.getUniqueId());
            BastionProfile.registry.addPlayer(bastionPlayer);
        }
    }
}
