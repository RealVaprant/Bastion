package net.vaprant.bastion.nation;

import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.player.BastionPlayerRegistry;

import java.util.HashMap;
import java.util.UUID;

public class Nation {
    public static final NationRegistry registry = new NationRegistry();

    public final UUID uuid;
    public String name;
    public HashMap<UUID, Authority> members;

    public Nation(UUID uuid, String name, HashMap<UUID, Authority> members) {
        this.uuid = uuid;
        this.name = name;
        this.members = members;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public boolean isMember(UUID uuid) {
        return this.members.get(uuid) != null;
    }

}