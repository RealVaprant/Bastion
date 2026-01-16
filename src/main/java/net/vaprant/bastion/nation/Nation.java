package net.vaprant.bastion.nation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.nation.permission.Authority;
import net.vaprant.bastion.nation.permission.PermissionNode;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class Nation {
    public static final NationRegistry registry = new NationRegistry();

    public final UUID uuid;
    public String name;
    private  UUID ownerId;
    private final HashMap<UUID, Authority> members;
    private final Map<UUID, Integer> invitations;
    private final Map<UUID, Integer> joinRequests;
    private final Map<Authority, Set<PermissionNode>> authorityPermissions;
    public boolean isDisbanded = false;


    //TODO: Add a constructor for grabbing fields from disk.
    public Nation(String name, UUID ownerId) {
        this.uuid = UUID.randomUUID();
        this.ownerId = ownerId;
        this.name = name;
        this.members = new HashMap<>();
        this.invitations = new HashMap<>();
        this.joinRequests = new HashMap<>();

        Map<Authority, Set<PermissionNode>> defaultPermissions = new HashMap<>();
        for (Authority authority : Authority.values()) {
            defaultPermissions.put(authority, new HashSet<>(authority.getDefaultPermissions()));
        }
        this.authorityPermissions = defaultPermissions;
    }

    public void setPermission(Authority authority, PermissionNode permissionNode, boolean isPermitted) {
        if (isPermitted) {
            authorityPermissions.get(authority).add(permissionNode);
        }
        else {
            authorityPermissions.get(authority).remove(permissionNode);
        }
    }

    public void setAuthority(UUID playerId, Authority authority) {
        members.remove(playerId);
        members.put(playerId, authority);
    }

    public boolean hasPermission(UUID playerId, PermissionNode permissionNode) {
        return authorityPermissions.get(getAuthority(playerId)).contains(permissionNode);
    }

    public boolean isOwner(UUID playerId) {
        return ownerId == playerId;
    }

    public boolean hasPermission(Authority authority, PermissionNode permissionNode) {
        return authorityPermissions.get(authority).contains(permissionNode);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwner(UUID uuid) {
        members.remove(this.ownerId);
        members.put(this.ownerId, Authority.OFFICER);
        this.ownerId = uuid;
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

    public void addMember(BastionProfile bastionPlayer, Authority authority) {
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


    public Authority getAuthority(UUID playerId) {
        return this.members.get(playerId);
    }

    public BastionProfile getOwner() {
        return BastionProfile.registry.getPlayer(this.ownerId);
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

    public List<BastionProfile> getMemberProfiles(){
        List<BastionProfile> list = new ArrayList<>();
        for (UUID uuid : members.keySet()) {
            BastionProfile bp = BastionProfile.registry.getPlayer(uuid);
            if (bp != null) {
                list.add(bp);
            }
        }
        return list;
    }

    public void removeMember(BastionProfile bastionProfile) {
        bastionProfile.setNation(null);
        this.members.remove(bastionProfile.uuid);

        if (this.members.isEmpty()) {
            this.disband();
            BastionNotification.broadcast(Component.empty().append(
                    Component.text(this.name).color(NamedTextColor.YELLOW)
            ).append(Component.text(" has disbanded.")));
        }
    }

    public void setOwner(BastionProfile bastionPlayer) {
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
            BastionProfile.registry.getPlayer(player.getUniqueId()).setNation(null);
        }
        Nation.registry.removeNation(this);
        this.isDisbanded = true;
        //TODO: Delete nation from disk.
    }
}