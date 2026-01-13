package net.vaprant.bastion.nation;

import net.kyori.adventure.text.Component;
import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.player.BastionPlayerRegistry;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class Nation {
    public static final NationRegistry registry = new NationRegistry();

    public final UUID uuid;
    public String name;
    public HashMap<UUID, Authority> members;

    public Nation(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.members = new HashMap<>();
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public boolean isMember(UUID uuid) {
        return this.members.get(uuid) != null;
    }

    public void addMember(BastionPlayer bastionPlayer, Authority authority) {
        members.put(bastionPlayer.getUniqueId(), authority);
        bastionPlayer.setNation(this);

        //TODO: Update the nation in disk.
    }

    public void removeMember(BastionPlayer bastionPlayer) {

        this.members.remove(bastionPlayer.getUniqueId());
        bastionPlayer.setNation(null);

        if (this.members.isEmpty()) {
            this.disband();
            Bukkit.broadcast(Component.text(""));
        }
    }

    public void disband() {
        Nation.registry.removeNation(this);
        //TODO: Delete nation from disk.
    }
}