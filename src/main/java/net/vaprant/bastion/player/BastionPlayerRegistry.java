package net.vaprant.bastion.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class BastionPlayerRegistry {
    private final HashMap<UUID, BastionPlayer> registry;

    public BastionPlayerRegistry() {
        this.registry = new HashMap<>();
    }

    public void addPlayer(BastionPlayer bastionPlayer) {
        this.registry.put(bastionPlayer.uuid, bastionPlayer);
    }


    public void removePlayer(UUID uuid) {
        this.registry.remove(uuid);
    }

    public BastionPlayer getPlayer(UUID uuid) {
        return this.registry.get(uuid);
    }
}