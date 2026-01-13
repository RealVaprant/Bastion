package net.vaprant.bastion.nation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.player.BastionPlayerRegistry;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Nation {
    public static final NationRegistry registry = new NationRegistry();

    public final UUID uuid;
    public String name;
    private final HashMap<UUID, Authority> members;
    public boolean isDisbaned = false;

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
        members.put(bastionPlayer.uuid, authority);
        bastionPlayer.setNation(this);

        //TODO: Update the nation in disk.
    }

    public Authority getAuthority(BastionPlayer bastionPlayer) {
        return this.members.get(bastionPlayer.uuid);
    }

    public List<Player> getOnlineMembers(){

        List<Player> onlinePlayers = new ArrayList<>();
        for (UUID uuid : members.keySet()){

            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    public void removeMember(BastionPlayer bastionPlayer) {

        this.members.remove(bastionPlayer.uuid);
        bastionPlayer.setNation(null);

        if (this.members.isEmpty()) {
            this.disband();
            BastionNotification.broadcast(
                    Component.text(this.name).color(NamedTextColor.YELLOW)
                    .append(Component.text(" has been disbanded.")
                            .color(NamedTextColor.RED)));
        }

    }

    public void broadcastActionBar(Component message) {
        for (Player player : getOnlineMembers()) {
            player.sendActionBar(message);
        }
    }

    public void broadcast(Component message) {
        for (Player player : getOnlineMembers()) {
            player.sendMessage(message);
        }
    }

    public void disband() {
        Nation.registry.removeNation(this);
        this.isDisbaned = true;
        //TODO: Delete nation from disk.
    }
}