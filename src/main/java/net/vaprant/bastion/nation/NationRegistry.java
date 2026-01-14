package net.vaprant.bastion.nation;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class NationRegistry {
    private final HashMap<UUID, Nation> registry;
    private final HashMap<String, Nation> nameIndex;

    public NationRegistry() {
        this.registry = new HashMap<>();
        this.nameIndex = new HashMap<>();
    }

    public void addNation(Nation nation) {
        this.registry.put(nation.getUniqueId(), nation);
        this.nameIndex.put(nation.name, nation);
    }

    public void removeNation(Nation nation) {
        this.registry.remove(nation.getUniqueId());
        this.nameIndex.remove(nation.name);
    }

    public void renameNation(Nation nation, String name) {
        if (this.nameIndex.containsKey(name)){
            throw new IllegalArgumentException("Registry was called to rename a Nation to a name that already belonged to another Nation.");
        }
        this.nameIndex.remove(nation.name);
        this.nameIndex.put(name, nation);
    }

    public Nation getNation(UUID uuid) {
        return this.registry.get(uuid);
    }

    public Nation getNation(String name) {
        return nameIndex.get(name);
    }

    public boolean isNation(String name) {
        return this.nameIndex.containsKey(name);
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