package net.vaprant.bastion.nation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class Nation {
    public static final NationRegistry registry = new NationRegistry();

    public final UUID uuid;
    public String name;
    public UUID ownerId;
    private final HashMap<UUID, Authority> members;
    private final Map<UUID, Integer> invitations;
    private final Map<UUID, Integer> joinRequests;
    public boolean isDisbanded = false;

    public Nation(String name, UUID ownerId) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.members = new HashMap<>();
        this.invitations = new HashMap<>();
        this.joinRequests = new HashMap<>() {
        };
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public boolean isMember(UUID uuid) {
        return this.members.get(uuid) != null;
    }

    public boolean isMember(String username) {
        UUID playerId = Bukkit.getPlayerUniqueId(username);
        return (this.members.containsKey(playerId));
    }

    public void addMember(BastionPlayer bastionPlayer, Authority authority) {
        members.put(bastionPlayer.uuid, authority);
        bastionPlayer.setNation(this);

        //TODO: Update the nation in disk.
    }

    public void addInvitation(UUID uuid, int taskId){
        invitations.put(uuid, taskId);
    }

    public void removeInvitation(UUID uuid){
        invitations.remove(uuid);
    }

    public Integer getInviteTask(UUID uuid) {
        return invitations.get(uuid);
    }

    public void addJoinRequest(UUID uuid, int taskId){
        joinRequests.put(uuid, taskId);
    }

    public void removeJoinRequest(UUID uuid){
        joinRequests.remove(uuid);
    }

    public Integer getJoinRequsetTask(UUID uuid){
        return joinRequests.get(uuid);
    }


    public boolean isInvited(UUID uuid){
        return invitations.containsKey(uuid);
    }

    public boolean isRequestingJoin(UUID uuid){
        return joinRequests.containsKey(uuid);
    }


    public Authority getAuthority(BastionPlayer bastionPlayer) {
        return this.members.get(bastionPlayer.uuid);
    }

    public BastionPlayer getOwner() {
        return BastionPlayer.registry.getPlayer(this.ownerId);
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

    public Set<OfflinePlayer> getMembers(){

        Set<OfflinePlayer> offlinePlayers = new HashSet<>();
        for (UUID uuid : members.keySet()){

            offlinePlayers.add(Bukkit.getOfflinePlayer(uuid));
        }
        return offlinePlayers;
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

    public void setOwner(BastionPlayer bastionPlayer) {
        this.ownerId = bastionPlayer.uuid;
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

    public void rename(String name) {
        Nation.registry.renameNation(this, name);
        this.name = name;
    }

    public void disband() {
        for (Player player : getOnlineMembers()){
            BastionPlayer.registry.getPlayer(player.getUniqueId()).setNation(null);
        }
        Nation.registry.removeNation(this);
        this.isDisbanded = true;
        //TODO: Delete nation from disk.
    }
}