package net.vaprant.bastion.nation;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class NationRegistry {
    private final HashMap<UUID, Nation> registry;

    public NationRegistry() {
        this.registry = new HashMap<>();
    }

    public void addNation(Nation nation) {
        this.registry.put(nation.getUniqueId(), nation);
    }

    public void removeNation(Nation nation) {
        this.registry.remove(nation.getUniqueId());
    }

    public Nation getNation(UUID uuid) {
        return this.registry.get(uuid);
    }

    public Nation getNationByMember(UUID uuid) {
        for (Nation nation : registry.values()) {
            if (nation.isMember(uuid)) {
                return nation;
            }
        }
        return null;
    }
}