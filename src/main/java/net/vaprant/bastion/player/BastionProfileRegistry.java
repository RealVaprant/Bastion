package net.vaprant.bastion.player;

import java.util.HashMap;
import java.util.UUID;

public class BastionProfileRegistry {
    private final HashMap<UUID, BastionProfile> registry;

    public BastionProfileRegistry() {
        this.registry = new HashMap<>();
    }

    public void addPlayer(BastionProfile bastionPlayer) {
        this.registry.put(bastionPlayer.uuid, bastionPlayer);
    }


    public void removePlayer(UUID uuid) {
        this.registry.remove(uuid);
    }

    public BastionProfile getPlayer(UUID uuid) {
        return this.registry.get(uuid);
    }

    public boolean containsPlayer(UUID uuid) {
        return this.registry.containsKey(uuid);
    }
}